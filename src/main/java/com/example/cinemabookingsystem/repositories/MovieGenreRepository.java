package com.example.cinemabookingsystem.repositories;

import com.example.cinemabookingsystem.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long> {
    boolean existsByMovieIdAndGenreId(Long movieId, Long genreId);

    Optional<MovieGenre> findByMovieIdAndGenreId(Long movieId, Long genreId);
}
