package com.example.cinemabookingsystem.dtos.updates;

import com.example.cinemabookingsystem.entity.PaymentStatus;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentUpdateDto {
    private Long bookingId;

    @PositiveOrZero(message = "Amount must be zero or greater")
    private BigDecimal amount;

    @Size(max = 50, message = "Payment method must be less than 50 characters")
    private String paymentMethod;

    private PaymentStatus paymentStatus;

    @Size(max = 150, message = "Transaction reference must be less than 150 characters")
    private String transactionReference;
}
