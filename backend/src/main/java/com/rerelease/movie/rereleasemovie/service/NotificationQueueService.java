package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.model.NotificationLog;
import com.rerelease.movie.rereleasemovie.model.NotificationQueue;
import com.rerelease.movie.rereleasemovie.model.UserMovieAlert;
import com.rerelease.movie.rereleasemovie.repository.NotificationLogRepository;
import com.rerelease.movie.rereleasemovie.repository.NotificationQueueRepository;
import com.rerelease.movie.rereleasemovie.repository.UserMovieAlertRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueueService {

    private final NotificationQueueRepository notificationQueueRepository;
    private final UserMovieAlertRepository userMovieAlertRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final JavaMailSender mailSender;

    /**
     * 알림을 큐에 추가하는 메서드
     * - 사용자가 영화에 대해 알림을 등록하면 NotificationQueue 테이블에 추가됨
     *
     * @param userMovieAlertId 사용자 알림 등록 ID
     */
    @Transactional
    public void addAlertToQueue(Long userMovieAlertId) {
        // 등록된 알림 ID로 UserMovieAlert 조회 (없으면 예외)
        UserMovieAlert userMovieAlert = userMovieAlertRepository.findById(userMovieAlertId)
                                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                        "해당 알림이 존재하지 않습니다."));

        // 전송 대기 상태(0)로 알림 큐에 추가
        NotificationQueue notificationQueue = NotificationQueue.builder()
                                                               .userMovieAlert(userMovieAlert)
                                                               .scheduledTime(LocalDateTime.now()) // 즉시 전송 예정
                                                               .retryCount(0)
                                                               .status(0)  // 0: 대기 상태
                                                               .createdAt(LocalDateTime.now())
                                                               .build();

        notificationQueueRepository.save(notificationQueue);
    }

    /**
     * 이메일 알림을 전송하는 메서드
     * - 전송 성공 시 상태를 "1(성공)"으로 변경하고 로그를 기록
     * - 실패 시 재시도 횟수를 증가시키고, 상태를 "0(재대기)" 또는 "2(실패)"로 설정
     *
     * @param queue 알림 대기열 정보 (NotificationQueue 엔티티)
     */
    @Transactional
    public void sendNotification(NotificationQueue queue) {
        UserMovieAlert alert = queue.getUserMovieAlert();

        try {
            // 영화 제목을 인코딩하여 검색 URL 생성
            String movieTitle = alert.getMovieTitle(); // 영화 제목 추출
            String searchKeyword = "영화 " + movieTitle; // '영화' 키워드 추가
            String encodedTitle = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8); // URL 인코딩
            String naverSearchUrl = "https://search.naver.com/search.naver?query=" + encodedTitle; // 네이버 검색 URL 생성

            // HTML 이메일 전송을 위한 MimeMessage 생성
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String subject = "(재)개봉 알림: " + movieTitle;

            /*
             * 포스터 이미지 URL 구성 (TMDB 이미지 서버 사용)
             * - 전체 이미지 URL = TMDB 기본 URL + 포스터의 상대 경로
             * - 기본 URL: https://image.tmdb.org/t/p/w500
             *   → w500은 너비 500px짜리 이미지 요청 의미
             * - alert.getPosterPath(): /로 시작하는 상대 경로 (예: /abc123.jpg)
             */
            String imageUrl = "https://image.tmdb.org/t/p/w500" + alert.getPosterPath();

            // 이메일 HTML 본문 구성
            String htmlContent = "<div style=\"font-family: Arial, sans-serif; line-height: 1.6;\">" +
                    "<h2>🎬 영화 (재)개봉 알림 🎉</h2>" +
                    "<p><strong>“" + movieTitle + "”</strong>의 재개봉 소식을 전해드립니다!</p>" +
                    "<img src=\"" + imageUrl + "\" alt=\"포스터 이미지\" " +
                    "style=\"max-width:300px; border-radius:8px; margin:20px 0;\" />" +
                    "<p>" +
                    "<a href=\"" + naverSearchUrl + "\" target=\"_blank\" " +
                    "style=\"color: #1e90ff; text-decoration: none; font-weight: bold;\">" +
                    "네이버에서 \"" + movieTitle + "\" 검색하기 🔍" +
                    "</a>" +
                    "</p>" +
                    "</div>";

            helper.setTo(alert.getUser()
                              .getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true → HTML 포맷 사용

            mailSender.send(mimeMessage);

            // 전송 성공 상태로 큐 상태 업데이트
            queue.updateStatus(1, null, 0);

            // 전송 성공 로그 저장
            notificationLogRepository.save(NotificationLog.builder()
                                                          .user(alert.getUser())
                                                          .movieId(alert.getMovieId())
                                                          .notificationType("EMAIL")
                                                          .status(1) // 성공
                                                          .sentAt(LocalDateTime.now())
                                                          .build());

        } catch (Exception e) {
            // 전송 실패 시 재시도 횟수 증가 및 상태 업데이트
            int updatedRetryCount = queue.getRetryCount() + 1;
            int updatedStatus = (updatedRetryCount >= 3) ? 2 : 0;

            queue.updateStatus(updatedStatus, e.getMessage(), updatedRetryCount);

            // 실패 로그 저장
            notificationLogRepository.save(NotificationLog.builder()
                                                          .user(alert.getUser())
                                                          .movieId(alert.getMovieId())
                                                          .notificationType("EMAIL")
                                                          .status(2) // 실패
                                                          .errorMessage(e.getMessage())
                                                          .sentAt(LocalDateTime.now())
                                                          .build());
        }
    }
}
