package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.MovieRequestDto;
import com.example.cinemabookingsystem.dtos.responses.GenreResponseDto;
import com.example.cinemabookingsystem.dtos.responses.MovieResponseDto;
import com.example.cinemabookingsystem.dtos.updates.MovieUpdateDto;
import com.example.cinemabookingsystem.entity.Genre;
import com.example.cinemabookingsystem.entity.Movie;
import com.example.cinemabookingsystem.entity.MovieGenre;
import com.example.cinemabookingsystem.exceptions.BadRequestException;
import com.example.cinemabookingsystem.exceptions.MovieNotFoundException;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.mappers.MovieMapper;
import com.example.cinemabookingsystem.repositories.GenreRepository;
import com.example.cinemabookingsystem.repositories.MovieGenreRepository;
import com.example.cinemabookingsystem.repositories.MovieRepository;
import com.example.cinemabookingsystem.repositories.ShowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class MovieService {
    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieMapper movieMapper;

    public List<MovieResponseDto> getMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toResponseDto)
                .toList();
    }

    public MovieResponseDto getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(movieMapper::toResponseDto)
                .orElseThrow(MovieNotFoundException::new);
    }

    public MovieResponseDto createMovie(MovieRequestDto movieRequest) {
        var movie = movieRepository.save(movieMapper.toEntity(movieRequest));
        return movieMapper.toResponseDto(movie);
    }

    public MovieResponseDto updateMovie(Long id, MovieUpdateDto movieUpdate) {
        var movie = movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
        movieMapper.updateEntity(movieUpdate, movie);
        var savedMovie = movieRepository.save(movie);
        return movieMapper.toResponseDto(savedMovie);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException();
        }

        if (showRepository.existsByMovieId(id)) {
            throw new BadRequestException("This movie has shows, so it cannot be deleted");
        }

        movieRepository.deleteById(id);
    }

    public MovieResponseDto addGenreToMovie(Long movieId, Long genreId) {
        validatePositiveId(movieId, "Movie id must be positive");
        validatePositiveId(genreId, "Genre id must be positive");

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

        boolean alreadyExists = movieGenreRepository.existsByMovieIdAndGenreId(movieId, genreId);

        if (alreadyExists) {
            throw new BadRequestException("Genre already added to this movie");
        }

        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setMovie(movie);
        movieGenre.setGenre(genre);

        MovieGenre savedMovieGenre = movieGenreRepository.save(movieGenre);
        movie.getMovieGenres().add(savedMovieGenre);

        Movie updatedMovie = movieRepository.findByIdWithGenres(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return movieMapper.toResponseDto(updatedMovie);
    }

    public List<GenreResponseDto> getMovieGenres(Long movieId) {
        validatePositiveId(movieId, "Movie id must be positive");

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return movieMapper.toGenreResponseDtos(movie.getMovieGenres());
    }

    @Transactional(readOnly = true)
    public GenreResponseDto getMovieGenre(Long movieId, Long genreId) {
        validatePositiveId(movieId, "Movie id must be positive");
        validatePositiveId(genreId, "Genre id must be positive");

        MovieGenre movieGenre = movieGenreRepository.findByMovieIdAndGenreId(movieId, genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie genre not found"));

        return movieMapper.toGenreResponseDto(movieGenre.getGenre());
    }

    @Transactional
    public MovieResponseDto updateMovieGenre(Long movieId, Long oldGenreId, Long newGenreId) {
        validatePositiveId(movieId, "Movie id must be positive");
        validatePositiveId(oldGenreId, "Genre id must be positive");
        validatePositiveId(newGenreId, "Genre id must be positive");

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        MovieGenre movieGenre = movieGenreRepository.findByMovieIdAndGenreId(movieId, oldGenreId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie genre not found"));

        if (oldGenreId.equals(newGenreId)) {
            return movieMapper.toResponseDto(movie);
        }

        if (movieGenreRepository.existsByMovieIdAndGenreId(movieId, newGenreId)) {
            throw new BadRequestException("Genre already added to this movie");
        }

        Genre newGenre = genreRepository.findById(newGenreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

        movieGenre.setGenre(newGenre);
        movieGenreRepository.save(movieGenre);

        Movie updatedMovie = movieRepository.findByIdWithGenres(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        return movieMapper.toResponseDto(updatedMovie);
    }

    public void deleteMovieGenre(Long movieId, Long genreId) {
        validatePositiveId(movieId, "Movie id must be positive");
        validatePositiveId(genreId, "Genre id must be positive");

        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found");
        }

        MovieGenre movieGenre = movieGenreRepository.findByMovieIdAndGenreId(movieId, genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie genre not found"));

        movieGenreRepository.delete(movieGenre);
        movieGenreRepository.flush();
    }

    private void validatePositiveId(Long id, String message) {
        if (id == null || id <= 0) {
            throw new BadRequestException(message);
        }
    }
}
