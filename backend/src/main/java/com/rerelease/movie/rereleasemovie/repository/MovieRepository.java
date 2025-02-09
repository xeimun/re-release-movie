package com.rerelease.movie.rereleasemovie.repository;

import com.rerelease.movie.rereleasemovie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MovieRepository 인터페이스 (데이터베이스 연동)
 * - JPA를 사용하여 DB와 연결
 * - 현재는 사용하지 않지만, 이후 MySQL 연동 시 적용 예정
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
