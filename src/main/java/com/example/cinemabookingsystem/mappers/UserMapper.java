package com.example.cinemabookingsystem.mappers;

import com.example.cinemabookingsystem.dtos.requests.UserRequestDto;
import com.example.cinemabookingsystem.dtos.responses.UserResponseDto;
import com.example.cinemabookingsystem.dtos.updates.UserUpdateDto;
import com.example.cinemabookingsystem.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User toEntity(UserRequestDto dto);

    UserResponseDto toResponseDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "comments", ignore = true)
    void updateEntity(UserUpdateDto dto, @MappingTarget User user);
}
