package com.rerelease.movie.rereleasemovie.service.impl;

import com.rerelease.movie.rereleasemovie.dto.MovieAlertRequest;
import com.rerelease.movie.rereleasemovie.dto.MovieAlertResponse;
import com.rerelease.movie.rereleasemovie.exceptions.MovieAlreadyRegisteredException;
import com.rerelease.movie.rereleasemovie.exceptions.UserNotFoundException;
import com.rerelease.movie.rereleasemovie.model.UserMovieAlert;
import com.rerelease.movie.rereleasemovie.model.Users;
import com.rerelease.movie.rereleasemovie.repository.UserMovieAlertRepository;
import com.rerelease.movie.rereleasemovie.repository.UserRepository;
import com.rerelease.movie.rereleasemovie.service.MovieAlertService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieAlertServiceImpl implements MovieAlertService {

    private final UserMovieAlertRepository userMovieAlertRepository;
    private final UserRepository userRepository;

    @Override
    public MovieAlertResponse registerMovieAlert(String userEmail, MovieAlertRequest request) {

        Users currentUser = userRepository.findByEmail(userEmail)
                                          .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        Optional<UserMovieAlert> existingAlert = userMovieAlertRepository.findByUserAndMovieId(currentUser,
                request.getTmdbId());

        if (existingAlert.isPresent()) {
            throw new MovieAlreadyRegisteredException("이미 등록된 영화입니다.");
        }

        UserMovieAlert newAlert = UserMovieAlert.builder()
                                                .user(currentUser)
                                                .movieId(request.getTmdbId())
                                                .notificationSent(false)
                                                .build();

        userMovieAlertRepository.save(newAlert);

        return new MovieAlertResponse("영화 등록이 완료되었습니다.", request.getTitle(), request.getTmdbId());
    }
}
