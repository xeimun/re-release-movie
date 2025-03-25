package com.rerelease.movie.rereleasemovie.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovieAlertResponse {
    private String message;
    private String movieTitle;
    private long movieId;
}
