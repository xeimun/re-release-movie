package com.rerelease.movie.rereleasemovie.controller;

import com.rerelease.movie.rereleasemovie.dto.MovieRequestDto;
import com.rerelease.movie.rereleasemovie.dto.MovieResponseDto;
import com.rerelease.movie.rereleasemovie.service.MovieService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    
    private final MovieService movieService;

    @GetMapping
    public List<MovieResponseDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public ResponseEntity<MovieResponseDto> createMovie(@RequestBody MovieRequestDto requestDto) {
        MovieResponseDto createdMovie = movieService.createMovie(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(createdMovie);
    }
}
