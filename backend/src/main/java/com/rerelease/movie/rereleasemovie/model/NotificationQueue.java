package com.rerelease.movie.rereleasemovie.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification_queue")
public class NotificationQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_movie_alert_id", nullable = false)
    @ToString.Exclude
    private UserMovieAlert userMovieAlert;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public NotificationQueue(UserMovieAlert userMovieAlert, int status, int retryCount) {
        this.userMovieAlert = userMovieAlert;
        this.status = status;
        this.retryCount = retryCount;
    }

    public void updateStatus(int newStatus, int retryCount) {
        this.status = newStatus;
        this.retryCount = retryCount;
    }
}
