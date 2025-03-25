package com.rerelease.movie.rereleasemovie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MovieAlertRequest {
    private String title;
    private String releaseYear;
    private long tmdbId;
}
