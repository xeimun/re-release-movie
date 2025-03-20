package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.dto.MovieAlertRequest;
import com.rerelease.movie.rereleasemovie.dto.MovieAlertResponse;

public interface MovieAlertService {
    MovieAlertResponse registerMovieAlert(String userEmail, MovieAlertRequest request);
}
