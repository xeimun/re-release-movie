package com.rerelease.movie.rereleasemovie.dto;

import com.rerelease.movie.rereleasemovie.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieResponseDto {
    private String title;
    private int releaseYear;
    private String genre;
    private String director;
    private String posterUrl;

    public static MovieResponseDto fromEntity(Movie movie) {
        return new MovieResponseDto(
                movie.getTitle(),
                movie.getReleaseYear(),
                movie.getGenre(),
                movie.getDirector(),
                movie.getPosterUrl()
        );
    }
}
