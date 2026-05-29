package com.example.cinemabookingsystem.repositories;

import com.example.cinemabookingsystem.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByShowId(Long showId);

    @Query("""
            SELECT b.id
            FROM Booking b
            """)
    Page<Long> findAllIds(Pageable pageable);

    @EntityGraph(attributePaths = {
            "seats",
            "seats.seat",
            "payment",
            "show",
            "user"
    })
    @Query("""
            SELECT DISTINCT b
            FROM Booking b
            WHERE b.id IN :ids
            """)
    List<Booking> findWithDetailsByIdIn(@Param("ids") Collection<Long> ids);

    @EntityGraph(attributePaths = {
            "seats",
            "seats.seat",
            "payment",
            "show",
            "user"
    })
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.id = :id
            """)
    Optional<Booking> findWithDetailsById(@Param("id") Long id);

    @Query("""
            SELECT b.id
            FROM Booking b
            WHERE b.user.email = :email
            """)
    Page<Long> findIdsByUserEmail(@Param("email") String email, Pageable pageable);

    @EntityGraph(attributePaths = {
            "seats",
            "payment"
    })
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.status = com.example.cinemabookingsystem.entity.BookingStatus.PENDING
              AND b.paymentDueAt < :now
            """)
    List<Booking> findExpiredPendingBookings(@Param("now") LocalDateTime now);
}
