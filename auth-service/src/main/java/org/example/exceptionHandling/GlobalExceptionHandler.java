package org.example.exceptionHandling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> buildResponse(HttpStatus status, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        return body;
    }

    // 🔹 Handle user not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex, HttpServletRequest req) {
        return new ResponseEntity<>(buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    // 🔹 Handle username already exists
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest req) {
        return new ResponseEntity<>(buildResponse(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI()), HttpStatus.CONFLICT);
    }

    // 🔹 Handle email already exists
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest req) {
        return new ResponseEntity<>(buildResponse(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI()), HttpStatus.CONFLICT);
    }

    // 🔹 Handle all other exceptions (safety net)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex, HttpServletRequest req) {
        return new ResponseEntity<>(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
