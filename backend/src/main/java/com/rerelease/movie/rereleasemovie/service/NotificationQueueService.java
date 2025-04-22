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
            sendEmail(alert);

            // 전송 성공 로그 저장
            saveNotificationLog(alert, 1, null);

            // 성공하면 알림 삭제 → NotificationQueue도 CASCADE로 삭제
            userMovieAlertRepository.delete(alert);

        } catch (Exception e) {
            int updatedRetryCount = queue.getRetryCount() + 1;
            queue.updateRetryCount(updatedRetryCount);
            notificationQueueRepository.save(queue); // retryCount 업데이트 반영

            if (updatedRetryCount >= 3) {
                String errorSummary = e.getClass()
                                       .getSimpleName();

                // 3회 실패하면 실패 로그 저장
                saveNotificationLog(alert, 2, errorSummary);

                // 실패하면 알림 삭제 → NotificationQueue도 CASCADE로 삭제
                userMovieAlertRepository.delete(alert);
            }
        }
    }

    private void sendEmail(UserMovieAlert alert) throws Exception {
        String movieTitle = alert.getMovieTitle();
        String searchKeyword = "영화 " + movieTitle;
        String encodedTitle = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        String naverSearchUrl = "https://search.naver.com/search.naver?query=" + encodedTitle;
        String imageUrl = "https://image.tmdb.org/t/p/w500" + alert.getPosterPath();
        String subject = "(재)개봉 알림: " + movieTitle;

        String htmlContent = """
                <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                    <h2>🎬 영화 (재)개봉 알림 🎉</h2>
                    <p><strong>“%s”</strong>의 (재)개봉 소식을 전해드립니다!</p>
                    <img src="%s" alt="포스터 이미지" style="max-width:300px; border-radius:8px; margin:20px 0;" />
                    <p>
                        <a href="%s" target="_blank" style="color: #1e90ff; text-decoration: none; font-weight: bold;">
                            네이버에서 "%s" 검색하기 🔍
                        </a>
                    </p>
                </div>
                """.formatted(movieTitle, imageUrl, naverSearchUrl, movieTitle);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(alert.getUser()
                          .getEmail());
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    private void saveNotificationLog(UserMovieAlert alert, int status, String errorMessage) {
        notificationLogRepository.save(NotificationLog.builder()
                                                      .user(alert.getUser())
                                                      .movieId(alert.getMovieId())
                                                      .movieTitle(alert.getMovieTitle())
                                                      .posterPath(alert.getPosterPath())
                                                      .notificationType("EMAIL")
                                                      .status(status) // 1 = 성공, 2 = 실패
                                                      .errorMessage(errorMessage)
                                                      .registeredAt(alert.getCreatedAt())
                                                      .build());
    }
}
