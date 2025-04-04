package com.rerelease.movie.rereleasemovie.controller;

import com.rerelease.movie.rereleasemovie.dto.api.kofic.MovieInfoDto;
import com.rerelease.movie.rereleasemovie.service.KoficApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kofic")
@RequiredArgsConstructor
public class KoficApiController {

    private final KoficApiService koficApiService;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieInfoDto>> searchMovies(@RequestParam String movieName) {
        List<MovieInfoDto> movies = koficApiService.searchMoviesByName(movieName);
        return ResponseEntity.ok(movies);
    }
}
