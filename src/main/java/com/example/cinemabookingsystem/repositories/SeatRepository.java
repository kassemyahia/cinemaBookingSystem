package com.example.cinemabookingsystem.repositories;

import com.example.cinemabookingsystem.entity.Seat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Override
    @EntityGraph(attributePaths = { "hall" })
    List<Seat> findAll();

    @Override
    @EntityGraph(attributePaths = { "hall" })
    Optional<Seat> findById(Long id);

    @EntityGraph(attributePaths = { "hall" })
    List<Seat> findByHallId(Long hallId);

    @EntityGraph(attributePaths = { "hall" })
    List<Seat> findByIdIn(Collection<Long> ids);

    @Query("SELECT MAX(s.seatNumber) FROM Seat s WHERE s.hall.id = :hallId")
    Integer findMaxSeatNumberByHallId(@Param("hallId") Long hallId);

    @Query("SELECT COUNT(DISTINCT s.rowLabel) FROM Seat s WHERE s.hall.id = :hallId")
    Integer countRowsByHallId(@Param("hallId") Long hallId);
}
