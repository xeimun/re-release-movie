package com.rerelease.movie.rereleasemovie.service;

import com.rerelease.movie.rereleasemovie.dto.OpenAPI.tmdb.TmdbMovieListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class TmdbApiService {

    private final WebClient tmdbWebClient;

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    public TmdbMovieListResponseDto getUpcomingMovies(int page) {
        String url = "/movie/upcoming?api_key=" + tmdbApiKey + "&language=ko-KR&region=KR&page=" + page;

        return tmdbWebClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToMono(TmdbMovieListResponseDto.class)
                            .block();
    }

    public int getTotalPages() {
        TmdbMovieListResponseDto response = getUpcomingMovies(1);
        return response != null ? response.getTotalPages() : 1;
    }
}