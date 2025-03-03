package com.rerelease.movie.rereleasemovie.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_movie_alert")
public class UserMovieAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Users user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @ToString.Exclude
    private Movie movie;

    private boolean notificationStatus = true;

    @Builder
    public UserMovieAlert(Users user, Movie movie, boolean notificationStatus) {
        this.user = user;
        this.movie = movie;
        this.notificationStatus = notificationStatus;
    }
}