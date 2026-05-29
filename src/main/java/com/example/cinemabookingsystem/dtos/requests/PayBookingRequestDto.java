package com.example.cinemabookingsystem.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PayBookingRequestDto {

    @NotBlank(message = "Payment method is required")
    @Size(max = 50, message = "Payment method must be less than 50 characters")
    private String paymentMethod;

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod != null ? paymentMethod.trim() : null;
    }
}