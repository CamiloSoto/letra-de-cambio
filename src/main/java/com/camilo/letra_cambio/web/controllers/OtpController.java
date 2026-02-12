package com.camilo.letra_cambio.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camilo.letra_cambio.domain.services.OtpService;
import com.camilo.letra_cambio.persistence.entities.TipoFirma;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/otp")
@AllArgsConstructor
public class OtpController {
    private final OtpService service;

    @GetMapping("/generate")
    public ResponseEntity<?> generateOtp(@RequestParam String email,
            @RequestParam String letraCambioId,
            @RequestParam TipoFirma tipoFirma) {
        service.generateAndSendOtp(
                email,
                letraCambioId,
                tipoFirma);
        return ResponseEntity.ok().build();
    }

}
