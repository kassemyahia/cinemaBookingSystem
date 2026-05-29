package com.example.cinemabookingsystem.repositories;

import com.example.cinemabookingsystem.entity.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking_Id(Long bookingId);

    @EntityGraph(attributePaths = {
            "booking",
            "booking.user"
    })
    @Query("""
           SELECT p
           FROM Payment p
           """)
    List<Payment> findAllWithDetails();

    @EntityGraph(attributePaths = {
            "booking",
            "booking.user",
            "booking.seats",
            "booking.seats.seat",
            "booking.payment",
            "booking.show"
    })
    @Query("""
           SELECT p
           FROM Payment p
           WHERE p.id = :paymentId
           """)
    Optional<Payment> findByIdWithBookingDetails(@Param("paymentId") Long paymentId);
}
