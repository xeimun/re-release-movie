package com.rerelease.movie.rereleasemovie.controller;

import com.rerelease.movie.rereleasemovie.dto.MovieAlertRequest;
import com.rerelease.movie.rereleasemovie.dto.MovieAlertResponse;
import com.rerelease.movie.rereleasemovie.dto.UserAlertManageDto;
import com.rerelease.movie.rereleasemovie.exceptions.MovieAlreadyRegisteredException;
import com.rerelease.movie.rereleasemovie.exceptions.UserNotFoundException;
import com.rerelease.movie.rereleasemovie.service.MovieAlertService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class MovieAlertController {

    private final MovieAlertService movieAlertService;

    @PostMapping("/register")
    public ResponseEntity<?> registerMovieAlert(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestBody MovieAlertRequest request) {

        if (userDetails == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        try {
            // 영화 알림 등록 (알림 등록 및 NotificationQueue 추가가 내부에서 처리됨)
            MovieAlertResponse response = movieAlertService.registerMovieAlert(userDetails.getUsername(), request);

            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(e.getMessage());

        } catch (MovieAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body(e.getMessage());
        }
    }

    @GetMapping("/my-alerts")
    public ResponseEntity<List<UserAlertManageDto>> getUserMovieAlerts(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .build();
        }

        List<UserAlertManageDto> alerts = movieAlertService.getUserMovieAlerts(userDetails.getUsername());
        return ResponseEntity.ok(alerts);
    }

    @DeleteMapping("/{alertId}")
    public ResponseEntity<?> deleteUserMovieAlert(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @PathVariable Long alertId) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("로그인이 필요합니다.");
        }

        try {
            movieAlertService.deleteUserMovieAlert(userDetails.getUsername(), alertId);
            return ResponseEntity.noContent()
                                 .build(); // 204 No Content
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body("본인의 알림만 삭제할 수 있습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("해당 알림을 찾을 수 없습니다.");
        }
    }
}
