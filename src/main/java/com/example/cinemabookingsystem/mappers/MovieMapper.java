package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.requests.MovieRequestDto;
import com.example.cinemabookingsystem.dtos.responses.GenreResponseDto;
import com.example.cinemabookingsystem.dtos.responses.MovieResponseDto;
import com.example.cinemabookingsystem.dtos.updates.MovieUpdateDto;
import com.example.cinemabookingsystem.entity.Genre;
import com.example.cinemabookingsystem.entity.Movie;
import com.example.cinemabookingsystem.entity.MovieGenre;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "movieGenres", ignore = true)
    @Mapping(target = "shows", ignore = true)
    Movie toEntity(MovieRequestDto dto);

    @Mapping(target = "genres", expression = "java(toGenreResponseDtos(movie.getMovieGenres()))")
    MovieResponseDto toResponseDto(Movie movie);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "movieGenres", ignore = true)
    @Mapping(target = "shows", ignore = true)
    void updateEntity(MovieUpdateDto dto, @MappingTarget Movie movie);

    GenreResponseDto toGenreResponseDto(Genre genre);

    default List<GenreResponseDto> toGenreResponseDtos(Set<MovieGenre> movieGenres) {
        if (movieGenres == null) {
            return List.of();
        }

        return movieGenres.stream()
                .map(MovieGenre::getGenre)
                .map(this::toGenreResponseDto)
                .toList();
    }
}
