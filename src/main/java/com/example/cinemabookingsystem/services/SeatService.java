package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.dtos.requests.SeatRequestDto;
import com.example.cinemabookingsystem.dtos.responses.SeatMapResponseDto;
import com.example.cinemabookingsystem.dtos.responses.SeatMapSeatDto;
import com.example.cinemabookingsystem.dtos.responses.SeatResponseDto;
import com.example.cinemabookingsystem.dtos.updates.SeatUpdateDto;
import com.example.cinemabookingsystem.entity.SeatStatus;
import com.example.cinemabookingsystem.entity.SeatType;
import com.example.cinemabookingsystem.exceptions.ResourceNotFoundException;
import com.example.cinemabookingsystem.mappers.SeatMapper;
import com.example.cinemabookingsystem.repositories.BookingSeatRepository;
import com.example.cinemabookingsystem.repositories.HallRepository;
import com.example.cinemabookingsystem.repositories.SeatRepository;
import com.example.cinemabookingsystem.repositories.ShowRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
@Transactional
public class SeatService {
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final ShowRepository showRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatMapper seatMapper;
    private final EntityManager entityManager;

    public List<SeatResponseDto> getSeats() {
        return seatRepository.findAll().stream()
                .map(seatMapper::toResponseDto)
                .toList();
    }

    public SeatResponseDto getSeatById(Long id) {
        return seatRepository.findById(id)
                .map(seatMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
    }

    public SeatResponseDto createSeat(SeatRequestDto seatRequest) {
        var hall = hallRepository.findById(seatRequest.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        var seat = seatMapper.toEntity(seatRequest);
        seat.setHall(hall);
        seat = seatRepository.saveAndFlush(seat);
        entityManager.refresh(seat);
        return seatMapper.toResponseDto(seat);
    }

    public SeatResponseDto updateSeat(Long id, SeatUpdateDto seatUpdate) {
        var seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
        seatMapper.updateEntity(seatUpdate, seat);

        if (seatUpdate.getHallId() != null) {
            var hall = hallRepository.findById(seatUpdate.getHallId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));
            seat.setHall(hall);
        }

        var savedSeat = seatRepository.saveAndFlush(seat);
        entityManager.refresh(savedSeat);
        return seatMapper.toResponseDto(savedSeat);
    }

    public void deleteSeat(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seat not found");
        }

        seatRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SeatMapResponseDto getSeatMap(Long showId) {
        var show = showRepository.findWithHallAndSeatsById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        var hall = show.getHall();
        var seats = hall.getSeats();
        Set<Long> bookedSeatIds = Set.copyOf(bookingSeatRepository.findUnavailableSeatIdsByShowId(showId));

        Integer rowCount = seatRepository.countRowsByHallId(hall.getId());
        Integer seatsPerRow = seatRepository.findMaxSeatNumberByHallId(hall.getId());

        return new SeatMapResponseDto(
                show.getId(),
                hall.getId(),
                hall.getName(),
                hall.getTotalSeats(),
                rowCount != null ? rowCount : 0,
                seatsPerRow != null ? seatsPerRow : 0,
                seats.stream()
                        .map(seat -> toSeatMapSeat(seat, bookedSeatIds.contains(seat.getId()), show.getBasePrice(),
                                show.getVipPrice()))
                        .toList());
    }

    private SeatMapSeatDto toSeatMapSeat(com.example.cinemabookingsystem.entity.Seat seat,
            boolean booked,
            BigDecimal standardPrice,
            BigDecimal vipPrice) {
        return new SeatMapSeatDto(
                seat.getId(),
                seat.getRowLabel() != null ? seat.getRowLabel().name() : null,
                seat.getSeatNumber() != null ? seat.getSeatNumber().intValue() : null,
                (Boolean.TRUE.equals(seat.getIsVip()) ? SeatType.VIP : SeatType.STANDARD).name(),
                (booked ? SeatStatus.BOOKED : SeatStatus.AVAILABLE).name(),
                Boolean.TRUE.equals(seat.getIsVip()) ? vipPrice : standardPrice);
    }
}
