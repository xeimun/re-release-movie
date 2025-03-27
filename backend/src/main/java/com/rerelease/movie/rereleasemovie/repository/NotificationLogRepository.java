package com.rerelease.movie.rereleasemovie.repository;

import com.rerelease.movie.rereleasemovie.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}
