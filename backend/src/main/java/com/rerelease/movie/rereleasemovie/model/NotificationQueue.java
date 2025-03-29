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

    @Column(nullable = false)
    private LocalDateTime scheduledTime;

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private int retryCount;

    @Column
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public NotificationQueue(UserMovieAlert userMovieAlert, LocalDateTime scheduledTime, int status,
                             int retryCount, String errorMessage, LocalDateTime createdAt) {
        this.userMovieAlert = userMovieAlert;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.retryCount = retryCount;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt;
    }

    public NotificationQueue changeStatus(int newStatus, String errorMessage, int retryCount) {
        return NotificationQueue.builder()
                                .userMovieAlert(this.userMovieAlert)
                                .scheduledTime(this.scheduledTime)
                                .status(newStatus)
                                .retryCount(retryCount)
                                .errorMessage(errorMessage)
                                .createdAt(this.createdAt)
                                .build();
    }
}
