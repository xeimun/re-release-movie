package com.rerelease.movie.rereleasemovie.repository;

import com.rerelease.movie.rereleasemovie.model.UserMovieAlert;
import com.rerelease.movie.rereleasemovie.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieAlertRepository extends JpaRepository<UserMovieAlert, Long> {
    Optional<UserMovieAlert> findByUserAndMovieId(Users user, Long movieId);
}
