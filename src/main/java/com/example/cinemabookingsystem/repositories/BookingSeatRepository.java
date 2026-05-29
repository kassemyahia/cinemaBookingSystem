package com.example.cinemabookingsystem.repositories;

import com.example.cinemabookingsystem.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    @Query("""
           SELECT bs.seat.id
           FROM BookingSeat bs
           WHERE bs.booking.show.id = :showId
             AND bs.booking.status IN (
                 com.example.cinemabookingsystem.entity.BookingStatus.PENDING,
                 com.example.cinemabookingsystem.entity.BookingStatus.CONFIRMED
             )
           """)
    Set<Long> findUnavailableSeatIdsByShowId(@Param("showId") Long showId);

    @Query("""
           SELECT CASE WHEN COUNT(bs) > 0 THEN true ELSE false END
           FROM BookingSeat bs
           WHERE bs.booking.show.id = :showId
             AND bs.seat.id = :seatId
             AND bs.booking.status IN (
                 com.example.cinemabookingsystem.entity.BookingStatus.PENDING,
                 com.example.cinemabookingsystem.entity.BookingStatus.CONFIRMED
             )
           """)
    boolean existsUnavailableSeatByShowIdAndSeatId(@Param("showId") Long showId,
                                                   @Param("seatId") Long seatId);
}
