package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.BookingDTO;
import org.example.dto.CreateBookingRequest;
import org.example.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody CreateBookingRequest request,
                                                    Authentication authentication) {
        Long userId = extractUserId(authentication);
        BookingDTO dto = bookingService.createBooking(request, userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAll() {
        return ResponseEntity.ok(bookingService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> update(@PathVariable("id") Long id, @Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
    private Long extractUserId(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Unauthorized");
        }
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = authentication.getName();
        }

        try {
            return Long.valueOf(username);
        } catch (NumberFormatException ex) {
            return -1L;
        }
    }
}