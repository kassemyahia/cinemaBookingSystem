package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.requests.PaymentRequestDto;
import com.example.cinemabookingsystem.dtos.responses.PaymentResponseDto;
import com.example.cinemabookingsystem.dtos.updates.PaymentUpdateDto;
import com.example.cinemabookingsystem.entity.Payment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Payment toEntity(PaymentRequestDto dto);

    @Mapping(target = "paymentId", source = "id")
    @Mapping(target = "paymentStatus", expression = "java(payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : null)")
    PaymentResponseDto toResponseDto(Payment payment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(PaymentUpdateDto dto, @MappingTarget Payment payment);
}
