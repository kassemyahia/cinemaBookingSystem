package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.requests.BookingSeatRequestDto;
import com.example.cinemabookingsystem.dtos.responses.BookingSeatResponseDto;
import com.example.cinemabookingsystem.dtos.updates.BookingSeatUpdateDto;
import com.example.cinemabookingsystem.entity.BookingSeat;
import com.example.cinemabookingsystem.entity.SeatType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BookingSeatMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "show", ignore = true)
    @Mapping(target = "seat", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BookingSeat toEntity(BookingSeatRequestDto dto);

    @Mapping(target = "seatId", source = "seat.id")
    @Mapping(target = "bookingSeatId", source = "id")
    @Mapping(target = "rowLabel", expression = "java(bookingSeat.getSeat().getRowLabel() != null ? bookingSeat.getSeat().getRowLabel().name() : null)")
    @Mapping(target = "seatNumber", expression = "java(bookingSeat.getSeat().getSeatNumber() != null ? bookingSeat.getSeat().getSeatNumber().intValue() : null)")
    @Mapping(target = "seatType", expression = "java(toSeatType(bookingSeat.getSeat().getIsVip()).name())")
    BookingSeatResponseDto toResponseDto(BookingSeat bookingSeat);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "show", ignore = true)
    @Mapping(target = "seat", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(BookingSeatUpdateDto dto, @MappingTarget BookingSeat bookingSeat);

    default SeatType toSeatType(Boolean isVip) {
        return Boolean.TRUE.equals(isVip) ? SeatType.VIP : SeatType.STANDARD;
    }
}
