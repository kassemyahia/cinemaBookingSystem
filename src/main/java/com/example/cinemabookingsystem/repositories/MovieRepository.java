package com.example.cinemabookingsystem.repositories;

import com.example.cinemabookingsystem.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Override
    @EntityGraph(attributePaths = {"movieGenres", "movieGenres.genre"})
    List<Movie> findAll();

    @Override
    @EntityGraph(attributePaths = {"movieGenres", "movieGenres.genre"})
    Optional<Movie> findById(Long id);

    @EntityGraph(attributePaths = {"movieGenres", "movieGenres.genre"})
    @Query("SELECT m FROM Movie m WHERE m.id = :movieId")
    Optional<Movie> findByIdWithGenres(@Param("movieId") Long movieId);
}
