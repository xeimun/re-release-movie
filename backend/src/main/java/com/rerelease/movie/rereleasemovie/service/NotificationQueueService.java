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

    /**
     * 알림을 큐에 추가하는 메서드
     * - 사용자가 알림을 등록하면 NotificationQueue 테이블에 저장된다.
     */
    @Transactional
    public void addAlertToQueue(Long userMovieAlertId) {
        UserMovieAlert userMovieAlert = userMovieAlertRepository.findById(userMovieAlertId)
                                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                        "해당 알림이 존재하지 않습니다."));

        NotificationQueue notificationQueue = NotificationQueue.builder()
                                                               .userMovieAlert(userMovieAlert)
                                                               .scheduledTime(LocalDateTime.now())
                                                               .retryCount(0)
                                                               .status(0)  // 전송 대기 상태
                                                               .createdAt(LocalDateTime.now())
                                                               .build();

        notificationQueueRepository.save(notificationQueue);
    }

    /**
     * 이메일 알림을 전송하는 메서드 (전송 성공 또는 실패 시 상태 변경)
     */
    @Transactional
    public void sendNotification(NotificationQueue queue) {
        try {
            UserMovieAlert alert = queue.getUserMovieAlert();

            // 이메일 메시지 생성
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(alert.getUser()
                               .getEmail());
            message.setSubject("[재개봉 알림] - 영화 알림");
            message.setText("등록하신 영화가 재개봉되었습니다. 영화 ID: " + alert.getMovieId());

            // 이메일 전송
            mailSender.send(message);

            // 전송 성공 시 상태 변경 (1: 전송 성공)
            UserMovieAlert updatedAlert = alert.changeStatus(1);
            NotificationQueue updatedQueue = queue.changeStatus(1, null, 0);

            userMovieAlertRepository.save(updatedAlert);
            notificationQueueRepository.save(updatedQueue);

            // 로그 저장 (전송 성공)
            notificationLogRepository.save(NotificationLog.builder()
                                                          .user(alert.getUser())
                                                          .movieId(alert.getMovieId())
                                                          .notificationType("EMAIL")
                                                          .status(1)
                                                          .sentAt(LocalDateTime.now())
                                                          .build());

        } catch (Exception e) {
            // 전송 실패 시 재시도 횟수 증가 및 상태 업데이트
            int updatedRetryCount = queue.getRetryCount() + 1;
            int updatedStatus = (updatedRetryCount >= 3) ? 2 : 0;  // 3회 실패 시 상태 2 (완전 실패)

            NotificationQueue failedQueue = queue.changeStatus(updatedStatus, e.getMessage(), updatedRetryCount);
            notificationQueueRepository.save(failedQueue);

            // 로그 저장 (전송 실패)
            notificationLogRepository.save(NotificationLog.builder()
                                                          .user(queue.getUserMovieAlert()
                                                                     .getUser())
                                                          .movieId(queue.getUserMovieAlert()
                                                                        .getMovieId())
                                                          .notificationType("EMAIL")
                                                          .status(2)
                                                          .errorMessage(e.getMessage())
                                                          .sentAt(LocalDateTime.now())
                                                          .build());
        }
    }
}
