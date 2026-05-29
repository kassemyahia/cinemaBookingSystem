package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.requests.SeatRequestDto;
import com.example.cinemabookingsystem.dtos.responses.SeatResponseDto;
import com.example.cinemabookingsystem.dtos.updates.SeatUpdateDto;
import com.example.cinemabookingsystem.entity.Seat;
import com.example.cinemabookingsystem.entity.SeatType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hall", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bookingSeats", ignore = true)
    Seat toEntity(SeatRequestDto dto);

    @Mapping(target = "hallId", source = "hall.id")
    @Mapping(target = "seatType", expression = "java(toSeatType(seat.getIsVip()))")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "price", ignore = true)
    SeatResponseDto toResponseDto(Seat seat);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hall", ignore = true)
    @Mapping(target = "isVip", source = "seatType")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bookingSeats", ignore = true)
    void updateEntity(SeatUpdateDto dto, @MappingTarget Seat seat);

    default SeatType toSeatType(Boolean isVip) {
        return Boolean.TRUE.equals(isVip) ? SeatType.VIP : SeatType.STANDARD;
    }

    default Boolean toIsVip(SeatType seatType) {
        return seatType == null ? null : seatType == SeatType.VIP;
    }
}
