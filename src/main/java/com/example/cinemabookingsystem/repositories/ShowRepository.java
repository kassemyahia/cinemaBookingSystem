package com.example.cinemabookingsystem.repositories;

import com.example.cinemabookingsystem.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {

    @Override
    @EntityGraph(attributePaths = {"movie", "hall"})
    List<Show> findAll();

    @Override
    @EntityGraph(attributePaths = {"movie", "hall"})
    Page<Show> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"movie", "hall"})
    List<Show> findByStartTimeLessThanEqualAndEndTimeGreaterThanOrderByStartTimeAsc(
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    @EntityGraph(attributePaths = {"movie", "hall"})
    List<Show> findByStartTimeGreaterThanOrderByStartTimeAsc(LocalDateTime now);

    @Override
    @EntityGraph(attributePaths = {"movie", "hall"})
    Optional<Show> findById(Long id);

    @EntityGraph(attributePaths = {"hall", "hall.seats"})
    @Query("""
           SELECT s
           FROM Show s
           WHERE s.id = :id
           """)
    Optional<Show> findWithHallAndSeatsById(@Param("id") Long id);

    boolean existsByMovieId(Long movieId);

    boolean existsByHallIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long hallId,
            LocalDateTime newEndTime,
            LocalDateTime newStartTime
    );

    boolean existsByHallIdAndIdNotAndStartTimeLessThanAndEndTimeGreaterThan(
            Long hallId,
            Long showId,
            LocalDateTime newEndTime,
            LocalDateTime newStartTime
    );
}
