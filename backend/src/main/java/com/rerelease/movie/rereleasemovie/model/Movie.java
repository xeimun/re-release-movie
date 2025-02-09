package com.rerelease.movie.rereleasemovie.model;

public class Movie {
    private int id;
    private String title;
    private int year;

    /**
     * Movie 클래스 (데이터 모델)
     * - 영화 정보를 담는 모델 클래스
     * - DB 연동 시 Entity로 변경 가능
     */
    public Movie(int id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }
}
