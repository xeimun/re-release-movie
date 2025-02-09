package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.model.Movie;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * MovieService 클래스 (비즈니스 로직)
 * - 영화 목록 데이터를 제공하는 서비스
 * - 현재는 더미 데이터 사용, 이후 DB 연동 예정
 */
@Service
public class MovieService {
    public List<Movie> getMovies() {
        return Arrays.asList(
                new Movie(1, "Inception", 2010),
                new Movie(2, "Interstellar", 2014),
                new Movie(3, "The Dark Knight", 2008)
        );
    }
}
