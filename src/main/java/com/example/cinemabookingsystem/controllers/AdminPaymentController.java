package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.UpdatePaymentStatusDto;
import com.example.cinemabookingsystem.dtos.responses.BookingResponseDto;
import com.example.cinemabookingsystem.dtos.responses.PaymentResponseDto;
import com.example.cinemabookingsystem.services.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/payments")
@PreAuthorize("hasRole('ADMIN')")
class AdminPaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public List<PaymentResponseDto> getPayments() {
        return paymentService.getAdminPayments();
    }

    @GetMapping("/{id}")
    public PaymentResponseDto getPaymentById(@PathVariable Long id) {
        return paymentService.getAdminPaymentById(id);
    }

    @PatchMapping("/{id}/status")
    public BookingResponseDto updatePaymentStatus(@PathVariable Long id,
                                                  @Valid @RequestBody UpdatePaymentStatusDto request) {
        return paymentService.updateAdminPaymentStatus(id, request.paymentStatus());
    }

    @PatchMapping("/{id}/fail")
    public BookingResponseDto failPayment(@PathVariable Long id) {
        return paymentService.failAdminPayment(id);
    }
}
