package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.GenreRequestDto;
import com.example.cinemabookingsystem.dtos.responses.GenreResponseDto;
import com.example.cinemabookingsystem.dtos.updates.GenreUpdateDto;
import com.example.cinemabookingsystem.exceptions.GenreNotFoundException;
import com.example.cinemabookingsystem.mappers.GenreMapper;
import com.example.cinemabookingsystem.repositories.GenreRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final EntityManager entityManager;

    public List<GenreResponseDto> getGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toResponseDto)
                .toList();
    }

    public GenreResponseDto getGenreById(Long id) {
        return genreRepository.findById(id)
                .map(genreMapper::toResponseDto)
                .orElseThrow(GenreNotFoundException::new);
    }

    public GenreResponseDto createGenre(GenreRequestDto genreRequest) {
        var genre = genreRepository.saveAndFlush(genreMapper.toEntity(genreRequest));
        entityManager.refresh(genre);
        return genreMapper.toResponseDto(genre);
    }

    public GenreResponseDto updateGenre(Long id, GenreUpdateDto genreUpdate) {
        var genre = genreRepository.findById(id).orElseThrow(GenreNotFoundException::new);
        genreMapper.updateEntity(genreUpdate, genre);
        var savedGenre = genreRepository.saveAndFlush(genre);
        entityManager.refresh(savedGenre);
        return genreMapper.toResponseDto(savedGenre);
    }

    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new GenreNotFoundException();
        }

        genreRepository.deleteById(id);
    }
}
