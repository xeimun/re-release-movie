package com.rerelease.movie.rereleasemovie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (테스트용)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()  // API 엔드포인트 인증 없이 허용
                .anyRequest().authenticated()  // 그 외 요청은 인증 필요
            )
            .formLogin(login -> login.disable()) // 로그인 폼 비활성화
            .httpBasic(basic -> basic.disable()); // HTTP 기본 인증 비활성화

        return http.build();
    }
}
