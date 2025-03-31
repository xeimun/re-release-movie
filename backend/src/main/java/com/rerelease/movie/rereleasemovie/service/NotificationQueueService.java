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
        try {
            UserMovieAlert alert = queue.getUserMovieAlert();

            // 이메일 메시지 구성
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(alert.getUser()
                               .getEmail());
            message.setSubject("[재개봉 알림] - 영화 알림");
            message.setText("등록하신 영화가 재개봉되었습니다. 영화 ID: " + alert.getMovieId());

            // 이메일 발송
            mailSender.send(message);

            // [성공 처리]
            queue.updateStatus(1, null, 0); // NotificationQueue 상태: 전송 성공 (에러 메시지 없음, 재시도 횟수 0)

            // 알림 로그 저장 (성공)
            notificationLogRepository.save(NotificationLog.builder()
                                                          .user(alert.getUser())
                                                          .movieId(alert.getMovieId())
                                                          .notificationType("EMAIL")
                                                          .status(1)  // 성공
                                                          .sentAt(LocalDateTime.now())
                                                          .build());

        } catch (Exception e) {
            // [실패 처리]
            int updatedRetryCount = queue.getRetryCount() + 1;
            int updatedStatus = (updatedRetryCount >= 3) ? 2 : 0;  // 3회 이상 실패 시 상태 2(완전 실패), 그 외는 다시 대기

            // 상태, 에러 메시지, 재시도 횟수 업데이트
            queue.updateStatus(updatedStatus, e.getMessage(), updatedRetryCount);

            // 알림 로그 저장 (실패)
            notificationLogRepository.save(NotificationLog.builder()
                                                          .user(queue.getUserMovieAlert()
                                                                     .getUser())
                                                          .movieId(queue.getUserMovieAlert()
                                                                        .getMovieId())
                                                          .notificationType("EMAIL")
                                                          .status(2)  // 실패
                                                          .errorMessage(e.getMessage())
                                                          .sentAt(LocalDateTime.now())
                                                          .build());
        }
    }
}
