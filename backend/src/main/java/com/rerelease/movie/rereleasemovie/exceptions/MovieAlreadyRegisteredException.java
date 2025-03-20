package com.rerelease.movie.rereleasemovie.exceptions;

public class MovieAlreadyRegisteredException extends RuntimeException {
    public MovieAlreadyRegisteredException(String message) {
        super(message);
    }
}
