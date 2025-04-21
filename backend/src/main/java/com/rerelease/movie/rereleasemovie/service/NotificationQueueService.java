package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.model.NotificationLog;
import com.rerelease.movie.rereleasemovie.model.NotificationQueue;
import com.rerelease.movie.rereleasemovie.model.UserMovieAlert;
import com.rerelease.movie.rereleasemovie.repository.NotificationLogRepository;
import com.rerelease.movie.rereleasemovie.repository.NotificationQueueRepository;
import com.rerelease.movie.rereleasemovie.repository.UserMovieAlertRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.transaction.annotation.Transactional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Transactional
    public void addAlertToQueue(Long userMovieAlertId) {
        UserMovieAlert userMovieAlert = userMovieAlertRepository.findById(userMovieAlertId)
                                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                        "해당 알림이 존재하지 않습니다."));

        NotificationQueue notificationQueue = NotificationQueue.builder()
                                                               .userMovieAlert(userMovieAlert)
                                                               .retryCount(0)
                                                               .build();

        notificationQueueRepository.save(notificationQueue);
    }

    @Transactional
    public void sendNotification(NotificationQueue queue) {
        UserMovieAlert alert = queue.getUserMovieAlert();

        try {
            // 영화 제목을 인코딩하여 검색 URL 생성
            String movieTitle = alert.getMovieTitle();
            String searchKeyword = "영화 " + movieTitle;
            String encodedTitle = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
            String naverSearchUrl = "https://search.naver.com/search.naver?query=" + encodedTitle;

            // 포스터 이미지 URL 구성 (TMDB 이미지 서버 사용)
            String imageUrl = "https://image.tmdb.org/t/p/w500" + alert.getPosterPath();

            // 이메일 제목 구성
            String subject = "(재)개봉 알림: " + movieTitle;

            // 이메일 HTML 본문 구성
            String htmlContent = "<div style=\"font-family: Arial, sans-serif; line-height: 1.6;\">" +
                    "<h2>🎬 영화 (재)개봉 알림 🎉</h2>" +
                    "<p><strong>“" + movieTitle + "”</strong>의 (재)개봉 소식을 전해드립니다!</p>" +
                    "<img src=\"" + imageUrl + "\" alt=\"포스터 이미지\" " +
                    "style=\"max-width:300px; border-radius:8px; margin:20px 0;\" />" +
                    "<p>" +
                    "<a href=\"" + naverSearchUrl + "\" target=\"_blank\" " +
                    "style=\"color: #1e90ff; text-decoration: none; font-weight: bold;\">" +
                    "네이버에서 \"" + movieTitle + "\" 검색하기 🔍" +
                    "</a>" +
                    "</p>" +
                    "</div>";

            // HTML 이메일 전송을 위한 MimeMessage 생성
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(alert.getUser()
                              .getEmail());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            // 전송 성공 로그 저장
            notificationLogRepository.save(NotificationLog.builder()
                                                          .user(alert.getUser())
                                                          .movieId(alert.getMovieId())
                                                          .movieTitle(alert.getMovieTitle())
                                                          .posterPath(alert.getPosterPath())
                                                          .notificationType("EMAIL")
                                                          .status(1) // 성공
                                                          .registeredAt(alert.getCreatedAt())
                                                          .build());

            // 성공했으면 UserMovieAlert 삭제 → NotificationQueue도 DB에서 자동 삭제 (ON DELETE CASCADE)
            userMovieAlertRepository.delete(alert);

        } catch (Exception e) {
            int updatedRetryCount = queue.getRetryCount() + 1;
            queue.updateRetryCount(updatedRetryCount);

            String errorSummary = e.getClass()
                                   .getSimpleName();

            if (updatedRetryCount >= 3) {
                // 3회 이상 실패 시 로그 저장
                notificationLogRepository.save(NotificationLog.builder()
                                                              .user(alert.getUser())
                                                              .movieId(alert.getMovieId())
                                                              .movieTitle(alert.getMovieTitle())
                                                              .posterPath(alert.getPosterPath())
                                                              .notificationType("EMAIL")
                                                              .status(2) // 실패
                                                              .errorMessage(errorSummary)
                                                              .registeredAt(alert.getCreatedAt())
                                                              .build());

                // 최종 실패했으면 UserMovieAlert 삭제 → NotificationQueue도 자동 삭제 (ON DELETE CASCADE)
                userMovieAlertRepository.delete(alert);
            }
        }
    }
}
