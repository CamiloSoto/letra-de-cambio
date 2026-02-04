package com.camilo.letra_cambio.web.controllers;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.camilo.letra_cambio.domain.services.LetraCambioService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/letra-cambio")
@AllArgsConstructor
public class LetraCambioController {
    private final LetraCambioService service;

    @GetMapping
    public ResponseEntity<byte[]> print() {
        try {
            byte[] pdf = service.generarLetraCambio();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=letra_cambio.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdf.length)
                    .body(pdf);

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error generando la letra de cambio", e);
        }
    }

}
