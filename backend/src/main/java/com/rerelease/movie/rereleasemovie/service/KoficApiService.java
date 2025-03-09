package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.dto.api.MovieInfoDto;
import com.rerelease.movie.rereleasemovie.dto.api.MovieListResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KoficApiService {

    private final WebClient webClient;

    @Value("${kofic.api.key}")
    private String koficApiKey;

    public List<MovieInfoDto> searchMoviesByName(String movieName) {
        String url = "/movie/searchMovieList.json?key=" + koficApiKey + "&movieNm=" + movieName;

        // KOFIC API 요청 및 응답 처리
        MovieListResponseDto response = webClient.get()
                                                 .uri(url)
                                                 .retrieve()
                                                 .bodyToMono(MovieListResponseDto.class)
                                                 .block();

        // API 응답이 null이거나 데이터가 없는 경우
        if (response == null || response.getMovieList()
                                        .isEmpty()) {
            return List.of();
        }

        return response.getMovieList();
    }
}