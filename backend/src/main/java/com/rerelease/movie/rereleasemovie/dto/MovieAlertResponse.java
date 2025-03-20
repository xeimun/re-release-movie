package com.rerelease.movie.rereleasemovie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieAlertResponse {
    private String message;
    private String movieTitle;
    private long movieId;
}
