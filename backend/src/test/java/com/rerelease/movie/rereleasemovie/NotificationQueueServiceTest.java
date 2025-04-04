package com.rerelease.movie.rereleasemovie;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.rerelease.movie.rereleasemovie.model.NotificationQueue;
import com.rerelease.movie.rereleasemovie.model.UserMovieAlert;
import com.rerelease.movie.rereleasemovie.model.Users;
import com.rerelease.movie.rereleasemovie.repository.NotificationQueueRepository;
import com.rerelease.movie.rereleasemovie.repository.UserMovieAlertRepository;
import com.rerelease.movie.rereleasemovie.repository.UserRepository;
import com.rerelease.movie.rereleasemovie.service.NotificationQueueService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Transactional
@Rollback(false)  // 실제 DB에 반영하여 상태 확인
public class NotificationQueueServiceTest {

    @Autowired
    private NotificationQueueService notificationQueueService;

    @Autowired
    private UserMovieAlertRepository userMovieAlertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationQueueRepository notificationQueueRepository;

    @Test
    public void testSendRealEmailNotification() {
        // 1. 테스트용 사용자 생성 또는 조회 (중복 생성 방지)
        Users user = userRepository.findByEmail("hinote444@naver.com")
                                   .orElseGet(() -> userRepository.save(
                                           Users.builder()
                                                .email("hinote444@naver.com")
                                                .nickname("지브리좋아")
                                                .password("12345678")
                                                .role(Users.Role.ROLE_USER)
                                                .build()
                                   ));

        // 2. 알림 등록 (영화 정보 포함)
        UserMovieAlert alert = userMovieAlertRepository.save(
                UserMovieAlert.builder()
                              .user(user)
                              .movieId(8392L)
                              .movieTitle("이웃집 토토로")
                              .posterPath("/c9zCkL0rTkNQ1HB9cmeFIqbkS50.jpg")
                              .build()
        );

        // 3. 알림 대기열(큐)에 등록
        NotificationQueue queue = notificationQueueRepository.save(
                NotificationQueue.builder()
                                 .userMovieAlert(alert)
                                 .retryCount(0)
                                 .status(0)
                                 .build()
        );

        // 4. 알림 이메일 전송 실행 (실제 메일 전송)
        notificationQueueService.sendNotification(queue);

        // 5. 상태 확인
        NotificationQueue updatedQueue = notificationQueueRepository.findById(queue.getId())
                                                                    .orElseThrow();

        assertEquals(1, updatedQueue.getStatus());  // 성공 상태 확인
    }
}
