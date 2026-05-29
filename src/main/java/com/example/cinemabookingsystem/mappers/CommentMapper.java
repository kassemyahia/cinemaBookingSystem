package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.requests.CommentRequestDto;
import com.example.cinemabookingsystem.dtos.responses.CommentResponseDto;
import com.example.cinemabookingsystem.dtos.updates.CommentUpdateDto;
import com.example.cinemabookingsystem.entity.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {MovieMapper.class, UserMapper.class})
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CommentRequestDto dto);

    CommentResponseDto toResponseDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(CommentUpdateDto dto, @MappingTarget Comment comment);
}
