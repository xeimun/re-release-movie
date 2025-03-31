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
@Rollback(false)  // 테스트 실행 후 DB에 실제 반영되도록 롤백 비활성화
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
    public void testSendNotification() {
        // 1. 테스트용 사용자 조회 또는 생성 (중복 생성 방지)
        Users user = userRepository.findByEmail("hinote444@naver.com")
                                   .orElseGet(() -> userRepository.save(
                                           Users.builder()
                                                .email("hinote444@naver.com")
                                                .nickname("지브리좋아")
                                                .password("12345678")
                                                .build()
                                   ));

        // 2. 알림 대상 영화 등록 (UserMovieAlert 테이블에 삽입)
        UserMovieAlert alert = userMovieAlertRepository.save(
                UserMovieAlert.builder()
                              .user(user)
                              .movieId(12345L)  // TMDB 영화 ID 임의 번호 지정
                              .build()
        );

        // 3. 알림 대기열에 등록 (NotificationQueue 테이블에 삽입)
        NotificationQueue queue = notificationQueueRepository.save(
                NotificationQueue.builder()
                                 .userMovieAlert(alert)
                                 .scheduledTime(LocalDateTime.now())   // 즉시 발송을 위해 현재 시간 설정
                                 .retryCount(0)                        // 처음 시도이므로 재시도 0
                                 .status(0)                            // 초기 상태: 대기 중
                                 .createdAt(LocalDateTime.now())
                                 .build()
        );

        // 4. queue를 다시 조회하여 영속 상태 보장 (JPA 변경 감지용)
        NotificationQueue persistedQueue = notificationQueueRepository.findById(queue.getId())
                                                                      .orElseThrow();

        // 5. 이메일 전송 실행 → 상태 업데이트 + 로그 기록
        notificationQueueService.sendNotification(persistedQueue);

        // 6. 다시 queue를 조회하여 상태가 '1 (전송 성공)' 으로 변경되었는지 확인
        NotificationQueue updatedQueue = notificationQueueRepository.findById(queue.getId())
                                                                    .orElseThrow();

        // 7. 테스트 검증 (status == 1)
        assertEquals(1, updatedQueue.getStatus());
    }
}
