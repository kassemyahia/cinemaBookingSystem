package com.example.cinemabookingsystem.dtos.requests;

import com.example.cinemabookingsystem.entity.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    @NotNull(message = "Booking id is required")
    private Long bookingId;

    @NotNull(message = "Amount is required")
    @PositiveOrZero(message = "Amount must be zero or greater")
    private BigDecimal amount;

    @NotBlank(message = "Payment method is required")
    @Size(max = 50, message = "Payment method must be less than 50 characters")
    private String paymentMethod;

    private PaymentStatus paymentStatus;

    @Size(max = 150, message = "Transaction reference must be less than 150 characters")
    private String transactionReference;
}
