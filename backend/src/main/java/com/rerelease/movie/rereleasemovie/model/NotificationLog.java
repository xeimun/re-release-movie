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
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Users user;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(nullable = false)
    private String notificationType;

    @Column(nullable = false)
    private int status;

    @Column
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Builder
    public NotificationLog(Users user, Long movieId, String notificationType, int status,
                           String errorMessage, LocalDateTime sentAt) {
        this.user = user;
        this.movieId = movieId;
        this.notificationType = notificationType;
        this.status = status;
        this.errorMessage = errorMessage;
        this.sentAt = sentAt;
    }
}
