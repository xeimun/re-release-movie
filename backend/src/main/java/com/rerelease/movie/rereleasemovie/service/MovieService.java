package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.dto.MovieRequestDto;
import com.rerelease.movie.rereleasemovie.dto.MovieResponseDto;
import com.rerelease.movie.rereleasemovie.model.Movie;
import com.rerelease.movie.rereleasemovie.repository.MovieRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieResponseDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                     .map(MovieResponseDto::fromEntity)
                     .toList();
    }

    public MovieResponseDto createMovie(MovieRequestDto requestDto) {
        Movie movie = requestDto.toEntity();
        Movie savedMovie = movieRepository.save(movie);
        return MovieResponseDto.fromEntity(savedMovie);
    }
}
