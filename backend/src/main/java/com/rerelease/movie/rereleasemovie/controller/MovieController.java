package com.rerelease.movie.rereleasemovie.controller;

import com.rerelease.movie.rereleasemovie.model.Movie;
import com.rerelease.movie.rereleasemovie.service.MovieService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MovieController 클래스 (API 요청 처리)
 * - 클라이언트(React)에서 요청을 받는 컨트롤러
 * - 현재는 `/api/movies` 엔드포인트 제공
 */
@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getMovies() {
        return movieService.getMovies();
    }
}
