package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.PayBookingRequestDto;
import com.example.cinemabookingsystem.dtos.responses.BookingResponseDto;
import com.example.cinemabookingsystem.dtos.responses.PaymentResponseDto;
import com.example.cinemabookingsystem.entity.Booking;
import com.example.cinemabookingsystem.entity.BookingStatus;
import com.example.cinemabookingsystem.entity.Payment;
import com.example.cinemabookingsystem.entity.PaymentStatus;
import com.example.cinemabookingsystem.entity.User;
import com.example.cinemabookingsystem.exceptions.BadRequestException;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.mappers.BookingMapper;
import com.example.cinemabookingsystem.mappers.PaymentMapper;
import com.example.cinemabookingsystem.repositories.BookingRepository;
import com.example.cinemabookingsystem.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;
    private final BookingMapper bookingMapper;
    private final CurrentUserService currentUserService;

    public List<PaymentResponseDto> getAdminPayments() {
        return paymentRepository.findAllWithDetails().stream()
                .map(paymentMapper::toResponseDto)
                .toList();
    }

    public PaymentResponseDto getAdminPaymentById(Long id) {
        var booking = findBookingWithDetailsByPaymentId(id);
        return paymentMapper.toResponseDto(requirePayment(booking, id));
    }

    public BookingResponseDto payBooking(Long bookingId, PayBookingRequestDto request) {
        var booking = bookingRepository.findWithDetailsById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        validatePaymentOwnership(booking);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot pay a cancelled booking");
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new BadRequestException("Booking is already confirmed");
        }

        if (booking.getPaymentDueAt() != null && booking.getPaymentDueAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Payment time expired");
        }

        if (booking.getSeats() == null || booking.getSeats().isEmpty()) {
            throw new BadRequestException("Booking has no seats");
        }

        Payment payment = booking.getPayment();
        if (payment != null && payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Payment already completed");
        }

        if (payment == null) {
            payment = new Payment();
            payment.setBooking(booking);
            booking.setPayment(payment);
        }

        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepository.saveAndFlush(payment);

        // TODO: This is a development-only payment simulation.
        // In a real production environment, payment should be confirmed via a payment gateway webhook or redirect.
        // Frontend must not be trusted to confirm payment directly.
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionReference(generateTransactionReference());
        booking.setStatus(BookingStatus.CONFIRMED);

        paymentRepository.saveAndFlush(payment);
        bookingRepository.saveAndFlush(booking);
        return reloadBookingResponse(booking.getId());
    }

    public BookingResponseDto updateAdminPaymentStatus(Long id, PaymentStatus status) {
        if (status == null) {
            throw new BadRequestException("Payment status is required");
        }

        var booking = findBookingWithDetailsByPaymentId(id);
        var payment = requirePayment(booking, id);

        applyPaymentStatus(payment, status, false);
        paymentRepository.saveAndFlush(payment);
        bookingRepository.saveAndFlush(booking);
        return reloadBookingResponse(booking.getId());
    }

    public BookingResponseDto failAdminPayment(Long id) {
        var booking = findBookingWithDetailsByPaymentId(id);
        var payment = requirePayment(booking, id);

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Completed payments cannot be failed");
        }

        payment.setPaymentStatus(PaymentStatus.FAILED);
        cancelBookingAndReleaseSeats(booking);
        paymentRepository.saveAndFlush(payment);
        bookingRepository.saveAndFlush(booking);
        return reloadBookingResponse(booking.getId());
    }

    private void applyPaymentStatus(Payment payment, PaymentStatus status, boolean allowExpiredConfirmation) {
        Booking booking = payment.getBooking();

        if (booking.getStatus() == BookingStatus.CANCELLED && status == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Cannot confirm payment for a cancelled booking");
        }

        if (status == PaymentStatus.SUCCESS
                && !allowExpiredConfirmation
                && booking.getPaymentDueAt() != null
                && booking.getPaymentDueAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot confirm payment after payment time expired");
        }

        payment.setPaymentStatus(status);

        if (status == PaymentStatus.SUCCESS) {
            booking.setStatus(BookingStatus.CONFIRMED);
        } else if (status == PaymentStatus.FAILED || status == PaymentStatus.EXPIRED) {
            cancelBookingAndReleaseSeats(booking);
        }
    }

    private void validatePaymentOwnership(Booking booking) {
        User currentUser = currentUserService.getCurrentUser();
        boolean isOwner = booking.getUser() != null && booking.getUser().getId().equals(currentUser.getId());

        if (!currentUserService.isAdmin(currentUser) && !isOwner) {
            throw new AccessDeniedException("You are not allowed to access this payment");
        }
    }

    private void cancelBookingAndReleaseSeats(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
        booking.getSeats().clear();
    }

    private BookingResponseDto reloadBookingResponse(Long bookingId) {
        var booking = bookingRepository.findWithDetailsById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return bookingMapper.toResponseDto(booking);
    }

    private Booking findBookingWithDetailsByPaymentId(Long paymentId) {
        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getBooking() == null || payment.getBooking().getId() == null) {
            throw new BadRequestException("Payment is not linked to a booking");
        }

        return bookingRepository.findWithDetailsById(payment.getBooking().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    private Payment requirePayment(Booking booking, Long paymentId) {
        Payment payment = booking.getPayment();
        if (payment == null || !paymentId.equals(payment.getId())) {
            throw new ResourceNotFoundException("Payment not found");
        }
        return payment;
    }

    private String generateTransactionReference() {
        return "TX-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
