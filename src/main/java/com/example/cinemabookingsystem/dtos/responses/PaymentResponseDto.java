package com.example.cinemabookingsystem.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {
    private Long paymentId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionReference;
    private LocalDateTime createdAt;
}
