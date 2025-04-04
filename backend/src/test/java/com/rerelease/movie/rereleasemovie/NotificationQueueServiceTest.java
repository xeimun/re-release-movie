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
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Transactional
@Rollback(false)  // 실제 DB에 반영하여 상태 확인
public class NotificationQueueServiceTest {

    /*
    테스트 클래스는 스프링 빈으로 등록되지 않기 때문에 @RequiredArgsConstructor로 생성자 주입만 선언해도 스프링이 주입하지 않음.
    생성자 주입(@RequiredArgsConstructor)은 @Component, @Service, @Configuration 등으로 등록된 클래스에서만 작동하며,
    테스트 클래스에서는 @Autowired를 통한 필드 주입 방식이 일반적으로 사용됨.
    */
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
                                                .email("hinote444@naver.com")  // 본인 메일 주소로 변경 가능
                                                .nickname("지브리좋아")
                                                .password("12345678")
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
                                 .scheduledTime(LocalDateTime.now())    // 즉시 발송을 위해 현재 시간 설정
                                 .retryCount(0)                         // 처음 시도이므로 재시도 0
                                 .status(0)                             // 초기 상태: 대기 중
                                 .createdAt(LocalDateTime.now())
                                 .build()
        );

        // 4. 알림 이메일 전송 실행 (실제 메일 전송)
        notificationQueueService.sendNotification(queue);

        // 5. 상태 확인
        NotificationQueue updatedQueue = notificationQueueRepository.findById(queue.getId())
                                                                    .orElseThrow();
        assertEquals(1, updatedQueue.getStatus());
    }
}
