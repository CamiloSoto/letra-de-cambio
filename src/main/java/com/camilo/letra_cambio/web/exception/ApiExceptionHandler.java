package com.camilo.letra_cambio.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgs(IllegalArgumentException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .code("NEGOCIO_ERROR")
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now().toString())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .code("AUTH_ERROR")
                .message(ex.getMessage())
                .timestamp(OffsetDateTime.now().toString())
                .build();

        System.out.println("UsernameNotFoundException: " + ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }
}
