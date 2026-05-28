package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.requests.HallRequestDto;
import com.example.cinemabookingsystem.dtos.responses.HallResponseDto;
import com.example.cinemabookingsystem.dtos.updates.HallUpdateDto;
import com.example.cinemabookingsystem.entity.Hall;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface HallMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "seats", ignore = true)
    @Mapping(target = "shows", ignore = true)
    Hall toEntity(HallRequestDto dto);

    HallResponseDto toResponseDto(Hall hall);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "seats", ignore = true)
    @Mapping(target = "shows", ignore = true)
    void updateEntity(HallUpdateDto dto, @MappingTarget Hall hall);
}
