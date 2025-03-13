package com.rerelease.movie.rereleasemovie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                        .baseUrl("http://kobis.or.kr/kobisopenapi/webservice/rest")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build();
    }

    @Bean
    public WebClient tmdbWebClient() {
        return WebClient.builder()
                        .baseUrl("https://api.themoviedb.org/3")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build();
    }
}
