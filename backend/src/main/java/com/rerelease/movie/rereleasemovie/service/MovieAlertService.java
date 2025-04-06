package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.dto.MovieAlertRequest;
import com.rerelease.movie.rereleasemovie.dto.MovieAlertResponse;
import com.rerelease.movie.rereleasemovie.dto.UserAlertManageDto;
import java.util.List;

public interface MovieAlertService {

    // 영화 알림 등록
    MovieAlertResponse registerMovieAlert(String userEmail, MovieAlertRequest request);

    // 사용자 알림 목록 조회
    List<UserAlertManageDto> getUserMovieAlerts(String userEmail);

    // 사용자 알림 삭제
    void deleteUserMovieAlert(String userEmail, Long alertId);
}
