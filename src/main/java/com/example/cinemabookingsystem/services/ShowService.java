package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.ShowRequestDto;
import com.example.cinemabookingsystem.dtos.responses.ShowResponseDto;
import com.example.cinemabookingsystem.dtos.responses.ShowsHomeResponseDto;
import com.example.cinemabookingsystem.dtos.updates.ShowUpdateDto;
import com.example.cinemabookingsystem.entity.Show;
import com.example.cinemabookingsystem.exceptions.BadRequestException;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.exceptions.ShowHasBookingsException;
import com.example.cinemabookingsystem.mappers.ShowMapper;
import com.example.cinemabookingsystem.repositories.BookingRepository;
import com.example.cinemabookingsystem.repositories.HallRepository;
import com.example.cinemabookingsystem.repositories.MovieRepository;
import com.example.cinemabookingsystem.repositories.ShowRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final ShowMapper showMapper;
    private final EntityManager entityManager;
    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public Page<ShowResponseDto> getShows(Pageable pageable) {
        return showRepository.findAll(pageable)
                .map(showMapper::toResponseDto);
    }

    public ShowResponseDto getShowById(Long id) {
        return showRepository.findById(id)
                .map(showMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
    }

    public ShowResponseDto createShow(ShowRequestDto showRequest) {
        var movie = movieRepository.findById(showRequest.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        var hall = hallRepository.findById(showRequest.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        LocalDateTime startTime = showRequest.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(movie.getDuration());

        boolean hallHasConflict = showRepository
                .existsByHallIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        hall.getId(),
                        endTime,
                        startTime
                );

        if (hallHasConflict) {
            throw new BadRequestException("Hall is already booked during this time");
        }

        var show = showMapper.toEntity(showRequest);
        show.setMovie(movie);
        show.setHall(hall);
        show.setEndTime(endTime);

        show = showRepository.saveAndFlush(show);
        entityManager.refresh(show);

        return showMapper.toResponseDto(show);
    }

    public ShowResponseDto updateShow(Long id, ShowUpdateDto showUpdate) {
        var show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        var movie = show.getMovie();
        if (showUpdate.getMovieId() != null) {
            movie = movieRepository.findById(showUpdate.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        }

        var hall = show.getHall();
        if (showUpdate.getHallId() != null) {
            hall = hallRepository.findById(showUpdate.getHallId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));
        }

        LocalDateTime startTime = showUpdate.getStartTime() != null
                ? showUpdate.getStartTime()
                : show.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(movie.getDuration());

        boolean hallHasConflict = showRepository
                .existsByHallIdAndIdNotAndStartTimeLessThanAndEndTimeGreaterThan(
                        hall.getId(),
                        show.getId(),
                        endTime,
                        startTime
                );

        if (hallHasConflict) {
            throw new BadRequestException("Hall is already booked during this time");
        }

        showMapper.updateEntity(showUpdate, show);
        show.setMovie(movie);
        show.setHall(hall);
        show.setStartTime(startTime);
        show.setEndTime(endTime);

        var savedShow = showRepository.saveAndFlush(show);
        entityManager.refresh(savedShow);
        return showMapper.toResponseDto(savedShow);
    }

    public void deleteShow(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        boolean hasBookings = bookingRepository.existsByShowId(showId);

        if (hasBookings) {
            throw new ShowHasBookingsException("This show has bookings, so it cannot be deleted");
        }

        showRepository.delete(show);
    }

    public ShowsHomeResponseDto getShowsHome() {
        LocalDateTime now = LocalDateTime.now();

        List<ShowResponseDto> currentShows = showRepository
                .findByStartTimeLessThanEqualAndEndTimeGreaterThanOrderByStartTimeAsc(now, now)
                .stream()
                .map(showMapper::toResponseDto)
                .toList();

        List<ShowResponseDto> upcomingShows = showRepository
                .findByStartTimeGreaterThanOrderByStartTimeAsc(now)
                .stream()
                .map(showMapper::toResponseDto)
                .toList();

        return new ShowsHomeResponseDto(currentShows, upcomingShows);
    }
}
