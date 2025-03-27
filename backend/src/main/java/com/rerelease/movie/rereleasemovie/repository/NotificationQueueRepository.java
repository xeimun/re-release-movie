package com.rerelease.movie.rereleasemovie.repository;

import com.rerelease.movie.rereleasemovie.model.NotificationQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationQueueRepository extends JpaRepository<NotificationQueue, Long> {
}
