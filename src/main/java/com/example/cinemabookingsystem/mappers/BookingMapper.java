package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.responses.BookingSeatResponseDto;
import com.example.cinemabookingsystem.dtos.responses.BookingResponseDto;
import com.example.cinemabookingsystem.dtos.responses.PaymentResponseDto;
import com.example.cinemabookingsystem.entity.Booking;
import com.example.cinemabookingsystem.entity.BookingSeat;
import com.example.cinemabookingsystem.entity.Payment;
import com.example.cinemabookingsystem.entity.SeatType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "showId", source = "show.id")
    @Mapping(target = "status", expression = "java(booking.getStatus() != null ? booking.getStatus().name() : null)")
    @Mapping(target = "seats", expression = "java(toBookingSeatResponseDtos(booking.getSeats()))")
    @Mapping(target = "payment", expression = "java(toPaymentResponseDto(booking.getPayment()))")
    BookingResponseDto toResponseDto(Booking booking);

    default List<BookingSeatResponseDto> toBookingSeatResponseDtos(List<BookingSeat> seats) {
        if (seats == null) {
            return List.of();
        }

        return seats.stream()
                .map(this::toBookingSeatResponseDto)
                .toList();
    }

    default BookingSeatResponseDto toBookingSeatResponseDto(BookingSeat bookingSeat) {
        var dto = new BookingSeatResponseDto();
        dto.setBookingSeatId(bookingSeat.getId());
        dto.setSeatId(bookingSeat.getSeat() != null ? bookingSeat.getSeat().getId() : null);
        if (bookingSeat.getSeat() != null) {
            dto.setRowLabel(bookingSeat.getSeat().getRowLabel() != null ? bookingSeat.getSeat().getRowLabel().name() : null);
            dto.setSeatNumber(bookingSeat.getSeat().getSeatNumber() != null ? bookingSeat.getSeat().getSeatNumber().intValue() : null);
            dto.setSeatType(Boolean.TRUE.equals(bookingSeat.getSeat().getIsVip()) ? SeatType.VIP.name() : SeatType.STANDARD.name());
        }
        dto.setPrice(bookingSeat.getPrice());
        return dto;
    }

    default PaymentResponseDto toPaymentResponseDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        var dto = new PaymentResponseDto();
        dto.setPaymentId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : null);
        dto.setTransactionReference(payment.getTransactionReference());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}
