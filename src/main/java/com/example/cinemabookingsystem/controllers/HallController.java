package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.HallRequestDto;
import com.example.cinemabookingsystem.dtos.responses.HallResponseDto;
import com.example.cinemabookingsystem.dtos.responses.SeatResponseDto;
import com.example.cinemabookingsystem.dtos.updates.HallUpdateDto;
import com.example.cinemabookingsystem.services.HallService;
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
@RequestMapping("/halls")
class HallController {
    private final HallService hallService;

    @GetMapping
    public List<HallResponseDto> getHalls() {
        return hallService.getHalls();
    }

    @GetMapping("/{id}")
    public HallResponseDto getHallById(@PathVariable Long id) {
        return hallService.getHallById(id);
    }

    @PostMapping
    public ResponseEntity<HallResponseDto> createHall(@Valid @RequestBody HallRequestDto hallRequest,
                                                      UriComponentsBuilder uriBuilder) {
        var hall = hallService.createHall(hallRequest);
        var uri = uriBuilder.path("/halls/{id}").buildAndExpand(hall.getId()).toUri();
        return ResponseEntity.created(uri).body(hall);
    }

    @PutMapping("/{id}")
    public HallResponseDto updateHall(@PathVariable Long id,
                                      @Valid @RequestBody HallUpdateDto hallUpdate) {
        return hallService.updateHall(id, hallUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/seats")
    public List<SeatResponseDto> getHallSeats(@PathVariable Long id) {
        return hallService.getHallSeats(id);
    }
}
