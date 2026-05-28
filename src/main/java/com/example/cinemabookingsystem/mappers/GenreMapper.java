package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.requests.GenreRequestDto;
import com.example.cinemabookingsystem.dtos.responses.GenreResponseDto;
import com.example.cinemabookingsystem.dtos.updates.GenreUpdateDto;
import com.example.cinemabookingsystem.entity.Genre;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "movieGenres", ignore = true)
    Genre toEntity(GenreRequestDto dto);

    GenreResponseDto toResponseDto(Genre genre);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "movieGenres", ignore = true)
    void updateEntity(GenreUpdateDto dto, @MappingTarget Genre genre);
}
