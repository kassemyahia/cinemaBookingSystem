package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.HallRequestDto;
import com.example.cinemabookingsystem.dtos.responses.HallResponseDto;
import com.example.cinemabookingsystem.dtos.responses.SeatResponseDto;
import com.example.cinemabookingsystem.dtos.updates.HallUpdateDto;
import com.example.cinemabookingsystem.exceptions.HallNotFoundException;
import com.example.cinemabookingsystem.mappers.HallMapper;
import com.example.cinemabookingsystem.mappers.SeatMapper;
import com.example.cinemabookingsystem.repositories.HallRepository;
import com.example.cinemabookingsystem.repositories.SeatRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class HallService {
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final HallMapper hallMapper;
    private final SeatMapper seatMapper;
    private final EntityManager entityManager;

    public List<HallResponseDto> getHalls() {
        return hallRepository.findAll().stream()
                .map(hallMapper::toResponseDto)
                .toList();
    }

    public HallResponseDto getHallById(Long id) {
        return hallRepository.findById(id)
                .map(hallMapper::toResponseDto)
                .orElseThrow(HallNotFoundException::new);
    }

    public HallResponseDto createHall(HallRequestDto hallRequest) {
        var hall = hallRepository.saveAndFlush(hallMapper.toEntity(hallRequest));
        entityManager.refresh(hall);
        return hallMapper.toResponseDto(hall);
    }

    public HallResponseDto updateHall(Long id, HallUpdateDto hallUpdate) {
        var hall = hallRepository.findById(id).orElseThrow(HallNotFoundException::new);
        hallMapper.updateEntity(hallUpdate, hall);
        var savedHall = hallRepository.saveAndFlush(hall);
        entityManager.refresh(savedHall);
        return hallMapper.toResponseDto(savedHall);
    }

    public void deleteHall(Long id) {
        if (!hallRepository.existsById(id)) {
            throw new HallNotFoundException();
        }

        hallRepository.deleteById(id);
    }

    public List<SeatResponseDto> getHallSeats(Long id) {
        if (!hallRepository.existsById(id)) {
            throw new HallNotFoundException();
        }

        return seatRepository.findByHallId(id).stream()
                .map(seatMapper::toResponseDto)
                .toList();
    }
}
