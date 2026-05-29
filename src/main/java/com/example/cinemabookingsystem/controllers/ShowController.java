package com.example.cinemabookingsystem.controllers;

import com.example.cinemabookingsystem.dtos.requests.ShowRequestDto;
import com.example.cinemabookingsystem.dtos.responses.SeatMapResponseDto;
import com.example.cinemabookingsystem.dtos.responses.ShowResponseDto;
import com.example.cinemabookingsystem.dtos.responses.ShowsHomeResponseDto;
import com.example.cinemabookingsystem.dtos.updates.ShowUpdateDto;
import com.example.cinemabookingsystem.services.SeatService;
import com.example.cinemabookingsystem.services.ShowService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

@RestController
@AllArgsConstructor
@RequestMapping("/shows")
class ShowController {
    private final ShowService showService;
    private final SeatService seatService;

    @GetMapping
    public Page<ShowResponseDto> getShows(
            @PageableDefault(size = 20, sort = "startTime", direction = Sort.Direction.ASC) Pageable pageable) {
        return showService.getShows(pageable);
    }

    @GetMapping("/{id}")
    public ShowResponseDto getShowById(@PathVariable Long id) {
        return showService.getShowById(id);
    }

    @PostMapping
    public ResponseEntity<ShowResponseDto> createShow(@Valid @RequestBody ShowRequestDto showRequest,
                                                      UriComponentsBuilder uriBuilder) {
        var show = showService.createShow(showRequest);
        var uri = uriBuilder.path("/shows/{id}").buildAndExpand(show.getId()).toUri();
        return ResponseEntity.created(uri).body(show);
    }

    @PutMapping("/{id}")
    public ShowResponseDto updateShow(@PathVariable Long id,
                                      @Valid @RequestBody ShowUpdateDto showUpdate) {
        return showService.updateShow(id, showUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/home")
    public ResponseEntity<ShowsHomeResponseDto> getShowsHome() {
        return ResponseEntity.ok(showService.getShowsHome());
    }

    @GetMapping("/{showId}/seats")
    public SeatMapResponseDto getShowSeatMap(@PathVariable Long showId) {
        return seatService.getSeatMap(showId);
    }
}
