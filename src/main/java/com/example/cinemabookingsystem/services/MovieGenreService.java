package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.responses.MovieGenreResponseDto;
import com.example.cinemabookingsystem.dtos.updates.MovieGenreUpdateDto;
import com.example.cinemabookingsystem.entity.MovieGenre;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.mappers.MovieGenreMapper;
import com.example.cinemabookingsystem.repositories.GenreRepository;
import com.example.cinemabookingsystem.repositories.MovieGenreRepository;
import com.example.cinemabookingsystem.repositories.MovieRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class MovieGenreService {
    private final MovieGenreRepository movieGenreRepository;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreMapper movieGenreMapper;
    private final EntityManager entityManager;

    public List<MovieGenreResponseDto> getMovieGenres() {
        return movieGenreRepository.findAll().stream()
                .map(movieGenreMapper::toResponseDto)
                .toList();
    }

    public MovieGenreResponseDto getMovieGenreById(Long id) {
        return movieGenreRepository.findById(id)
                .map(movieGenreMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Movie genre not found"));
    }

    public MovieGenreResponseDto createMovieGenre(Long movieId, Long genreId) {
        var movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

        var movieGenre = new MovieGenre();
        movieGenre.setMovie(movie);
        movieGenre.setGenre(genre);
        movieGenre = movieGenreRepository.saveAndFlush(movieGenre);
        entityManager.refresh(movieGenre);
        return movieGenreMapper.toResponseDto(movieGenre);
    }

    public MovieGenreResponseDto updateMovieGenre(Long id, MovieGenreUpdateDto movieGenreUpdate) {
        var movieGenre = movieGenreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie genre not found"));
        movieGenreMapper.updateEntity(movieGenreUpdate, movieGenre);

        if (movieGenreUpdate.getMovieId() != null) {
            var movie = movieRepository.findById(movieGenreUpdate.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
            movieGenre.setMovie(movie);
        }

        if (movieGenreUpdate.getGenreId() != null) {
            var genre = genreRepository.findById(movieGenreUpdate.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
            movieGenre.setGenre(genre);
        }

        var savedMovieGenre = movieGenreRepository.saveAndFlush(movieGenre);
        entityManager.refresh(savedMovieGenre);
        return movieGenreMapper.toResponseDto(savedMovieGenre);
    }

    public void deleteMovieGenre(Long id) {
        if (!movieGenreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie genre not found");
        }

        movieGenreRepository.deleteById(id);
    }
}
