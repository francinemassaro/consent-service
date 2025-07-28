package com.openfinance.service.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", OffsetDateTime.now());
        error.put("status", ex.getStatusCode().value());
        error.put("error", ex.getReason());
        error.put("path", request.getRequestURI());

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }
}
