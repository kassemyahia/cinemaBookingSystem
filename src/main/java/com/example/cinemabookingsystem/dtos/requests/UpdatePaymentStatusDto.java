package com.example.cinemabookingsystem.dtos.requests;

import com.example.cinemabookingsystem.entity.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdatePaymentStatusDto(
        @NotNull(message = "Payment status is required")
        PaymentStatus paymentStatus
) {
}
