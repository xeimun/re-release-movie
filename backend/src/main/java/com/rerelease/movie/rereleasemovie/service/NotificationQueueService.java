package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.model.NotificationLog;
import com.rerelease.movie.rereleasemovie.model.NotificationQueue;
import com.rerelease.movie.rereleasemovie.model.UserMovieAlert;
import com.rerelease.movie.rereleasemovie.repository.NotificationLogRepository;
import com.rerelease.movie.rereleasemovie.repository.NotificationQueueRepository;
import com.rerelease.movie.rereleasemovie.repository.UserMovieAlertRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
                                                               .scheduledTime(LocalDateTime.now())
                                                               .retryCount(0)
                                                               .status(0)
                                                               .createdAt(LocalDateTime.now())
                                                               .build();

        notificationQueueRepository.save(notificationQueue);
    }

    public void sendNotification(UserMovieAlert alert) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(alert.getUser()
                               .getEmail());
            message.setSubject("[재개봉 알림] - 영화 알림");
            message.setText("등록하신 영화가 재개봉되었습니다. 영화 ID: " + alert.getMovieId());

            mailSender.send(message);

            UserMovieAlert updatedAlert = alert.changeStatus(1);  // 상태 변경 메서드 사용 (1: 전송 완료)
            userMovieAlertRepository.save(updatedAlert);

            notificationLogRepository.save(NotificationLog.builder()
                                                          .user(alert.getUser())
                                                          .movieId(alert.getMovieId())
                                                          .notificationType("EMAIL")
                                                          .status(1)
                                                          .sentAt(LocalDateTime.now())
                                                          .build());

        } catch (Exception e) {

            UserMovieAlert updatedAlert = alert.changeStatus(2);  // 상태 변경 메서드 사용 (2: 전송 실패)
            userMovieAlertRepository.save(updatedAlert);

            notificationLogRepository.save(NotificationLog.builder()
                                                          .user(alert.getUser())
                                                          .movieId(alert.getMovieId())
                                                          .notificationType("EMAIL")
                                                          .status(2)
                                                          .errorMessage(e.getMessage())
                                                          .sentAt(LocalDateTime.now())
                                                          .build());
            throw new RuntimeException(e);
        }
    }
}
