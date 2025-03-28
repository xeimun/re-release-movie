package com.rerelease.movie.rereleasemovie.scheduler;

import com.rerelease.movie.rereleasemovie.dto.api.tmdb.TmdbMovieListResponseDto;
import com.rerelease.movie.rereleasemovie.model.UserMovieAlert;
import com.rerelease.movie.rereleasemovie.repository.UserMovieAlertRepository;
import com.rerelease.movie.rereleasemovie.service.NotificationQueueService;
import com.rerelease.movie.rereleasemovie.service.TmdbApiService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final TmdbApiService tmdbApiService;
    private final UserMovieAlertRepository userMovieAlertRepository;
    private final NotificationQueueService notificationQueueService;

    @Scheduled(cron = "0 0 4 * * ?")
    public void checkAndSendNotifications() {
        Set<Long> availableMovieIds = new HashSet<>();

        // 모든 페이지의 개봉 예정 영화 목록 조회
        for (int page = 1; page <= tmdbApiService.getTotalPagesForUpcoming(); page++) {
            TmdbMovieListResponseDto upcomingMovies = tmdbApiService.getUpcomingMovies(page);
            if (upcomingMovies != null) {
                upcomingMovies.getResults()
                              .forEach(movie -> availableMovieIds.add(movie.getId()));
            }
        }

        // 모든 페이지의 현재 개봉 중인 영화 목록 조회
        for (int page = 1; page <= tmdbApiService.getTotalPagesForNowPlaying(); page++) {
            TmdbMovieListResponseDto nowPlayingMovies = tmdbApiService.getNowPlayingMovies(page);
            if (nowPlayingMovies != null) {
                nowPlayingMovies.getResults()
                                .forEach(movie -> availableMovieIds.add(movie.getId()));
            }
        }

        // 알림 등록된 영화들 중 아직 전송되지 않은 영화 조회
        List<UserMovieAlert> alertsToSend = userMovieAlertRepository.findByStatus(0);

        // 알림 발송 작업 수행
        for (UserMovieAlert alert : alertsToSend) {
            if (availableMovieIds.contains(alert.getMovieId())) {
                notificationQueueService.sendNotification(alert);
            }
        }
    }
}

