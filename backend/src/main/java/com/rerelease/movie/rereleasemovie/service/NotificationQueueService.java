package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.model.NotificationQueue;
import com.rerelease.movie.rereleasemovie.model.UserMovieAlert;
import com.rerelease.movie.rereleasemovie.repository.NotificationQueueRepository;
import com.rerelease.movie.rereleasemovie.repository.UserMovieAlertRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationQueueService {

    private final NotificationQueueRepository notificationQueueRepository;
    private final UserMovieAlertRepository userMovieAlertRepository;

    @Transactional
    public void addAlertToQueue(Long userMovieAlertId) {
        // User_Movie_Alert 존재 여부 확인
        UserMovieAlert userMovieAlert = userMovieAlertRepository.findById(userMovieAlertId)
                                                                .orElseThrow(() -> new IllegalArgumentException(
                                                                        "해당 알림이 존재하지 않습니다."));

        // Notification_Queue에 추가
        NotificationQueue notificationQueue = NotificationQueue.builder()
                                                               .userMovieAlert(userMovieAlert)
                                                               .scheduledTime(LocalDateTime.now())
                                                               .retryCount(0)
                                                               .status(0)
                                                               .createdAt(LocalDateTime.now())
                                                               .build();

        notificationQueueRepository.save(notificationQueue);
    }
}
