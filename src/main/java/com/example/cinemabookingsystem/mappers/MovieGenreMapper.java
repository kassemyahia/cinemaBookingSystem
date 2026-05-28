package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.responses.MovieGenreResponseDto;
import com.example.cinemabookingsystem.dtos.updates.MovieGenreUpdateDto;
import com.example.cinemabookingsystem.entity.MovieGenre;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = MovieMapper.class)
public interface MovieGenreMapper {
    MovieGenreResponseDto toResponseDto(MovieGenre movieGenre);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(MovieGenreUpdateDto dto, @MappingTarget MovieGenre movieGenre);
}
