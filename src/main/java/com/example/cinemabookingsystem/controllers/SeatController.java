package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.SeatRequestDto;
import com.example.cinemabookingsystem.dtos.responses.SeatResponseDto;
import com.example.cinemabookingsystem.dtos.updates.SeatUpdateDto;
import com.example.cinemabookingsystem.services.SeatService;
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
@RequestMapping("/seats")
class SeatController {
    private final SeatService seatService;

    @GetMapping
    public List<SeatResponseDto> getSeats() {
        return seatService.getSeats();
    }

    @GetMapping("/{id}")
    public SeatResponseDto getSeatById(@PathVariable Long id) {
        return seatService.getSeatById(id);
    }

    @PostMapping
    public ResponseEntity<SeatResponseDto> createSeat(@Valid @RequestBody SeatRequestDto seatRequest,
                                                      UriComponentsBuilder uriBuilder) {
        var seat = seatService.createSeat(seatRequest);
        var uri = uriBuilder.path("/seats/{id}").buildAndExpand(seat.getId()).toUri();
        return ResponseEntity.created(uri).body(seat);
    }

    @PutMapping("/{id}")
    public SeatResponseDto updateSeat(@PathVariable Long id,
                                      @Valid @RequestBody SeatUpdateDto seatUpdate) {
        return seatService.updateSeat(id, seatUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
