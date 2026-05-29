package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.requests.ShowRequestDto;
import com.example.cinemabookingsystem.dtos.responses.ShowResponseDto;
import com.example.cinemabookingsystem.dtos.updates.ShowUpdateDto;
import com.example.cinemabookingsystem.entity.Show;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {MovieMapper.class, HallMapper.class})
public interface ShowMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "hall", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    Show toEntity(ShowRequestDto dto);

    ShowResponseDto toResponseDto(Show show);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "hall", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateEntity(ShowUpdateDto dto, @MappingTarget Show show);
}
