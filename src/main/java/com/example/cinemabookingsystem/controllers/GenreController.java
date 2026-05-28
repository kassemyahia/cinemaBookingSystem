package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.GenreRequestDto;
import com.example.cinemabookingsystem.dtos.responses.GenreResponseDto;
import com.example.cinemabookingsystem.dtos.updates.GenreUpdateDto;
import com.example.cinemabookingsystem.services.GenreService;
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
@RequestMapping("/genres")
class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<GenreResponseDto> getGenres() {
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public GenreResponseDto getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @PostMapping
    public ResponseEntity<GenreResponseDto> createGenre(@Valid @RequestBody GenreRequestDto genreRequest,
                                                        UriComponentsBuilder uriBuilder) {
        var genre = genreService.createGenre(genreRequest);
        var uri = uriBuilder.path("/genres/{id}").buildAndExpand(genre.getId()).toUri();
        return ResponseEntity.created(uri).body(genre);
    }

    @PutMapping("/{id}")
    public GenreResponseDto updateGenre(@PathVariable Long id,
                                        @Valid @RequestBody GenreUpdateDto genreUpdate) {
        return genreService.updateGenre(id, genreUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
