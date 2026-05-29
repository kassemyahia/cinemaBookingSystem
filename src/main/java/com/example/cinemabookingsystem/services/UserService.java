package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.UserRequestDto;
import com.example.cinemabookingsystem.dtos.responses.UserResponseDto;
import com.example.cinemabookingsystem.dtos.updates.UserUpdateDto;
import com.example.cinemabookingsystem.exceptions.BadRequestException;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.mappers.UserMapper;
import com.example.cinemabookingsystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordPolicyService passwordPolicyService;

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponseDto createUser(UserRequestDto request) {
        String email = request.getEmail().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already registered");
        }

        var user = userMapper.toEntity(request);
        user.setEmail(email);
        passwordPolicyService.validateStrongPassword(request.getPassword());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toResponseDto(userRepository.saveAndFlush(user));
    }

    public UserResponseDto updateUser(Long id, UserUpdateDto request) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getEmail() != null) {
            String email = request.getEmail().toLowerCase();
            userRepository.findByEmail(email)
                    .filter(existingUser -> !existingUser.getId().equals(id))
                    .ifPresent(existingUser -> {
                        throw new BadRequestException("Email is already registered");
                    });
            request.setEmail(email);
        }

        userMapper.updateEntity(request, user);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            passwordPolicyService.validateStrongPassword(request.getPassword());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userMapper.toResponseDto(userRepository.saveAndFlush(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }
}
