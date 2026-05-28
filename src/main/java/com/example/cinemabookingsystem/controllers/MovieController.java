package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.AddGenreToMovieRequestDto;
import com.example.cinemabookingsystem.dtos.requests.MovieRequestDto;
import com.example.cinemabookingsystem.dtos.responses.GenreResponseDto;
import com.example.cinemabookingsystem.dtos.responses.MovieResponseDto;
import com.example.cinemabookingsystem.dtos.updates.MovieUpdateDto;
import com.example.cinemabookingsystem.services.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/movies")
class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<MovieResponseDto> getMovies() {
        return movieService.getMovies();
    }

    @GetMapping("/{id}")
    public MovieResponseDto getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public ResponseEntity<MovieResponseDto> createMovie(@Valid @RequestBody MovieRequestDto movieRequest,
                                                        UriComponentsBuilder uriBuilder) {
        var movie = movieService.createMovie(movieRequest);
        var uri = uriBuilder.path("/movies/{id}").buildAndExpand(movie.getId()).toUri();
        return ResponseEntity.created(uri).body(movie);
    }

    @PutMapping("/{id}")
    public MovieResponseDto updateMovie(@PathVariable Long id,
                                        @Valid @RequestBody MovieUpdateDto movieUpdate) {
        return movieService.updateMovie(id, movieUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{movieId}/genres")
    public MovieResponseDto addGenreToMovie(
            @PathVariable Long movieId,
            @Valid @RequestBody AddGenreToMovieRequestDto request
    ) {
        return movieService.addGenreToMovie(movieId, request.getGenreId());
    }

    @GetMapping("/{movieId}/genres")
    public List<GenreResponseDto> getMovieGenres(@PathVariable Long movieId) {
        return movieService.getMovieGenres(movieId);
    }

    @PutMapping("/{movieId}/genres/{genreId}")
    public MovieResponseDto updateMovieGenre(
            @PathVariable Long movieId,
            @PathVariable Long genreId,
            @Valid @RequestBody AddGenreToMovieRequestDto request
    ) {
        return movieService.updateMovieGenre(movieId, genreId, request.getGenreId());
    }

    @DeleteMapping("/{movieId}/genres/{genreId}")
    public ResponseEntity<Void> deleteMovieGenre(
            @PathVariable Long movieId,
            @PathVariable Long genreId
    ) {
        movieService.deleteMovieGenre(movieId, genreId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{movieId}/genres/{genreId}")
    public GenreResponseDto getMovieGenre(
            @PathVariable Long movieId,
            @PathVariable Long genreId
    ) {
        return movieService.getMovieGenre(movieId, genreId);
    }
}
