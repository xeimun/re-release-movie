package com.rerelease.movie.rereleasemovie.repository;

import com.rerelease.movie.rereleasemovie.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
}
