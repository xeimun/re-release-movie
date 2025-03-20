package com.rerelease.movie.rereleasemovie.dto;

import lombok.Getter;

@Getter
public class MovieAlertRequest {
    private String title;
    private String releaseYear;
    private long tmdbId;
}
