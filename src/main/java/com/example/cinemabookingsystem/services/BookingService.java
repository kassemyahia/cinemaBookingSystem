package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.CreateBookingRequestDto;
import com.example.cinemabookingsystem.dtos.responses.BookingResponseDto;
import com.example.cinemabookingsystem.entity.Booking;
import com.example.cinemabookingsystem.entity.BookingSeat;
import com.example.cinemabookingsystem.entity.BookingStatus;
import com.example.cinemabookingsystem.entity.Payment;
import com.example.cinemabookingsystem.entity.PaymentStatus;
import com.example.cinemabookingsystem.entity.Seat;
import com.example.cinemabookingsystem.entity.Show;
import com.example.cinemabookingsystem.entity.User;
import com.example.cinemabookingsystem.exceptions.BadRequestException;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.mappers.BookingMapper;
import com.example.cinemabookingsystem.repositories.BookingRepository;
import com.example.cinemabookingsystem.repositories.BookingSeatRepository;
import com.example.cinemabookingsystem.repositories.PaymentRepository;
import com.example.cinemabookingsystem.repositories.SeatRepository;
import com.example.cinemabookingsystem.repositories.ShowRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatRepository seatRepository;
    private final PaymentRepository paymentRepository;
    private final ShowRepository showRepository;
    private final BookingMapper bookingMapper;
    private final CurrentUserService currentUserService;

    @Transactional(readOnly = true)
    public Page<BookingResponseDto> getBookings(Pageable pageable) {
        Page<Long> bookingIds = bookingRepository.findAllIds(pageable);
        return toBookingResponsePage(bookingIds, pageable);
    }

    @Transactional(readOnly = true)
    public Page<BookingResponseDto> getMyBookings(Pageable pageable) {
        User currentUser = currentUserService.getCurrentUser();
        Page<Long> bookingIds = bookingRepository.findIdsByUserEmail(currentUser.getEmail(), pageable);
        return toBookingResponsePage(bookingIds, pageable);
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(Long id) {
        var booking = bookingRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        validateBookingAccess(booking);
        return bookingMapper.toResponseDto(booking);
    }

    public BookingResponseDto createBooking(CreateBookingRequestDto request) {
        Long showId = request.getShowId();
        if (showId == null) {
            throw new BadRequestException("Show id is required");
        }

        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new BadRequestException("At least one seat must be selected");
        }

        var user = currentUserService.getCurrentUser();
        var show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        LocalDateTime now = LocalDateTime.now();
        validateShowBookable(show, now);
        validateShowConfigured(show);

        var uniqueSeatIds = new HashSet<>(request.getSeatIds());
        if (uniqueSeatIds.size() != request.getSeatIds().size()) {
            throw new BadRequestException("Duplicate seat ids are not allowed");
        }

        var seatsById = seatRepository.findByIdIn(uniqueSeatIds).stream()
                .collect(Collectors.toMap(Seat::getId, Function.identity()));
        if (seatsById.size() != uniqueSeatIds.size()) {
            throw new ResourceNotFoundException("One or more seats were not found");
        }

        boolean hasVipSeat = seatsById.values().stream()
                .anyMatch(seat -> Boolean.TRUE.equals(seat.getIsVip()));
        if (hasVipSeat && show.getVipPrice() == null) {
            throw new BadRequestException("Show VIP price is not configured");
        }

        Long hallId = show.getHall().getId();
        boolean allSeatsBelongToShowHall = seatsById.values().stream()
                .allMatch(seat -> seat.getHall() != null && hallId.equals(seat.getHall().getId()));
        if (!allSeatsBelongToShowHall) {
            throw new BadRequestException("All selected seats must belong to the show hall");
        }

        var bookedSeatIds = new HashSet<>(bookingSeatRepository.findUnavailableSeatIdsByShowId(showId));
        boolean hasAlreadyBookedSeat = uniqueSeatIds.stream().anyMatch(bookedSeatIds::contains);
        if (hasAlreadyBookedSeat) {
            throw new BadRequestException("One or more selected seats are already booked");
        }

        var booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setStatus(BookingStatus.PENDING);
        booking.setBookingReference(generateBookingReference());
        booking.setExpiresAt(show.getEndTime());
        booking.setPaymentDueAt(now.plusHours(1));

        List<BookingSeat> bookingSeats = request.getSeatIds().stream()
                .map(seatId -> {
                    Seat seat = seatsById.get(seatId);
                    var bookingSeat = new BookingSeat();
                    bookingSeat.setBooking(booking);
                    bookingSeat.setShow(show);
                    bookingSeat.setSeat(seat);
                    bookingSeat.setPrice(calculateSeatPrice(show.getBasePrice(), show.getVipPrice(), seat.getIsVip()));
                    return bookingSeat;
                })
                .toList();
        booking.getSeats().addAll(bookingSeats);

        BigDecimal totalAmount = bookingSeats.stream()
                .map(BookingSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        booking.setTotalAmount(totalAmount);

        try {
            bookingRepository.saveAndFlush(booking);
            bookingSeatRepository.saveAllAndFlush(bookingSeats);

            var payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(totalAmount);
            payment.setPaymentMethod("pending");
            payment.setTransactionReference(null);
            payment.setPaymentStatus(PaymentStatus.PENDING);
            booking.setPayment(payment);
            paymentRepository.saveAndFlush(payment);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("One or more selected seats are already booked");
        }

        var savedBooking = bookingRepository.findWithDetailsById(booking.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return bookingMapper.toResponseDto(savedBooking);
    }

    public void deleteBooking(Long id) {
        var booking = bookingRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        validateBookingAccess(booking);
        validateBookingCanBeDeleted(booking);
        cancelBookingAndReleaseSeats(booking);
        bookingRepository.saveAndFlush(booking);
    }

    public BookingResponseDto cancelBooking(Long id) {
        var booking = bookingRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        validateBookingAccess(booking);

        validateBookingCanBeCancelled(booking);
        cancelBookingAndReleaseSeats(booking);
        bookingRepository.saveAndFlush(booking);
        return getBookingById(id);
    }

    public BookingResponseDto updateBookingStatus(Long id, BookingStatus status) {
        if (status == null) {
            throw new BadRequestException("Booking status is required");
        }

        var booking = bookingRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED && status != BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot change a cancelled booking back to active status");
        }

        if (status == BookingStatus.CONFIRMED && !hasSuccessfulPayment(booking)) {
            throw new BadRequestException("Cannot confirm booking without successful payment");
        }

        if (status == BookingStatus.CANCELLED) {
            cancelBookingAndReleaseSeats(booking);
        } else {
            booking.setStatus(status);
        }

        bookingRepository.saveAndFlush(booking);
        return getBookingById(id);
    }

    public void cancelExpiredPendingBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findExpiredPendingBookings(now);

        for (Booking booking : bookings) {
            boolean paymentSuccessful = booking.getPayment() != null
                    && booking.getPayment().getPaymentStatus() == PaymentStatus.SUCCESS;

            if (!paymentSuccessful) {
                booking.setStatus(BookingStatus.CANCELLED);

                if (booking.getPayment() != null) {
                    booking.getPayment().setPaymentStatus(PaymentStatus.EXPIRED);
                }

                booking.getSeats().clear();
            }
        }
    }

    private BigDecimal calculateSeatPrice(BigDecimal basePrice, BigDecimal vipPrice, Boolean isVip) {
        return Boolean.TRUE.equals(isVip) ? vipPrice : basePrice;
    }

    private void cancelBookingAndReleaseSeats(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);

        if (booking.getPayment() != null && booking.getPayment().getPaymentStatus() == PaymentStatus.PENDING) {
            booking.getPayment().setPaymentStatus(PaymentStatus.FAILED);
        }

        booking.getSeats().clear();
    }

    private boolean hasSuccessfulPayment(Booking booking) {
        return booking.getPayment() != null
                && booking.getPayment().getPaymentStatus() == PaymentStatus.SUCCESS;
    }

    private void validateBookingAccess(Booking booking) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUserService.isCustomer(currentUser)
                && (booking.getUser() == null || !booking.getUser().getId().equals(currentUser.getId()))) {
            throw new AccessDeniedException("You are not allowed to access this booking");
        }
    }

    private void validateShowBookable(Show show, LocalDateTime now) {
        if (show.getEndTime() != null && show.getEndTime().isBefore(now)) {
            throw new BadRequestException("Cannot book seats for an expired show");
        }

        if (show.getStartTime() != null && show.getStartTime().isBefore(now)) {
            throw new BadRequestException("Cannot book seats for a show that already started");
        }
    }

    private void validateShowConfigured(Show show) {
        if (show.getHall() == null || show.getHall().getId() == null) {
            throw new BadRequestException("Show hall is not configured");
        }

        if (show.getBasePrice() == null) {
            throw new BadRequestException("Show base price is not configured");
        }
    }

    private void validateBookingCanBeCancelled(Booking booking) {
        validateBookingCanBeCancelled(booking, "Paid bookings cannot be cancelled");
    }

    private void validateBookingCanBeDeleted(Booking booking) {
        validateBookingCanBeCancelled(booking, "Paid bookings cannot be deleted or cancelled");
    }

    private void validateBookingCanBeCancelled(Booking booking, String paidBookingMessage) {
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED || hasSuccessfulPayment(booking)) {
            throw new BadRequestException(paidBookingMessage);
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only pending bookings can be cancelled");
        }

        if (booking.getPayment() != null && booking.getPayment().getPaymentStatus() != PaymentStatus.PENDING) {
            throw new BadRequestException("Only unpaid pending bookings can be cancelled");
        }
    }

    private String generateBookingReference() {
        return "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Page<BookingResponseDto> toBookingResponsePage(Page<Long> bookingIds, Pageable pageable) {
        if (bookingIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, bookingIds.getTotalElements());
        }

        Map<Long, Booking> bookingsById = bookingRepository.findWithDetailsByIdIn(bookingIds.getContent()).stream()
                .collect(Collectors.toMap(Booking::getId, Function.identity()));

        List<BookingResponseDto> content = bookingIds.getContent().stream()
                .map(bookingsById::get)
                .filter(booking -> booking != null)
                .map(bookingMapper::toResponseDto)
                .toList();

        return new PageImpl<>(content, pageable, bookingIds.getTotalElements());
    }
}
