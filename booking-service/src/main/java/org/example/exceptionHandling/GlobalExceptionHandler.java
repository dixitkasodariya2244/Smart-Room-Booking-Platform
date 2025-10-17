package org.example.exceptionHandling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> build(HttpStatus status, String message, String path) {
        Map<String, Object> m = new HashMap<>();
        m.put("timestamp", LocalDateTime.now());
        m.put("status", status.value());
        m.put("error", status.getReasonPhrase());
        m.put("message", message);
        m.put("path", path);
        return m;
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(BookingNotFoundException ex, HttpServletRequest req) {
        return new ResponseEntity<>(build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex, HttpServletRequest req) {
        return new ResponseEntity<>(build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}