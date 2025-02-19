package com.rerelease.movie.rereleasemovie.dto;

import com.rerelease.movie.rereleasemovie.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequestDto {
    private String title;
    private int releaseYear;
    private String genre;
    private String director;
    private String posterUrl;

    public Movie toEntity() {
        return Movie.builder()
                    .title(title)
                    .releaseYear(releaseYear)
                    .genre(genre)
                    .director(director)
                    .posterUrl(posterUrl)
                    .build();
    }
}
