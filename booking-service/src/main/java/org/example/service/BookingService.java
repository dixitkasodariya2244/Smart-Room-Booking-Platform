package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.BookingDTO;
import org.example.dto.CreateBookingRequest;
import org.example.exceptionHandling.BookingNotFoundException;
import org.example.model.Booking;
import org.example.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    @Transactional
    public BookingDTO createBooking(CreateBookingRequest req, Long userId) {
        Booking booking = Booking.builder()
                .roomId(req.getRoomId())
                .userId(userId)
                .startAt(req.getStartAt())
                .endAt(req.getEndAt())
                .note(req.getNote())
                .build();

        Booking saved = bookingRepository.save(booking);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public BookingDTO getById(Long id) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
        return toDto(b);
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getAll() {
        return bookingRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public BookingDTO updateBooking(Long id, CreateBookingRequest req) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));

        // NOTE: we do not check owner/permissions yet
        b.setRoomId(req.getRoomId());
        b.setStartAt(req.getStartAt());
        b.setEndAt(req.getEndAt());
        b.setNote(req.getNote());

        Booking saved = bookingRepository.save(b);
        return toDto(saved);
    }

    @Transactional
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    private BookingDTO toDto(Booking b) {
        return BookingDTO.builder()
                .id(b.getId())
                .roomId(b.getRoomId())
                .userId(b.getUserId())
                .startAt(b.getStartAt())
                .endAt(b.getEndAt())
                .note(b.getNote())
                .build();
    }
}
