package com.camilo.letra_cambio.web.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.camilo.letra_cambio.domain.dtos.LetraCambioRequest;
import com.camilo.letra_cambio.domain.services.LetraCambioService;
import com.camilo.letra_cambio.persistence.entities.LetraCambioEntity;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/letra-cambio")
@AllArgsConstructor
public class LetraCambioController {
    private final LetraCambioService service;

    @PostMapping("/print")
    public ResponseEntity<?> print(@RequestBody LetraCambioRequest request) {
        byte[] pdf = service.generarPdf(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=letra_cambio.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping
    public ResponseEntity<LetraCambioEntity> crearLetra(
            @RequestBody LetraCambioRequest request) {

        LetraCambioEntity letra = service.crearLetraCambio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(letra);
    }
}
