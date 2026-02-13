package com.camilo.letra_cambio.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camilo.letra_cambio.domain.dtos.OtpValidationRequest;
import com.camilo.letra_cambio.domain.services.OtpService;
import com.camilo.letra_cambio.persistence.entities.TipoFirma;

import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/validate")
    public ResponseEntity<Void> validateOtp(@RequestBody OtpValidationRequest request, HttpServletRequest httpRequest) {

        String ipOrigen = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        service.validateOtp(request, ipOrigen, userAgent);

        return ResponseEntity.ok().build();
    }

    private String getClientIp(HttpServletRequest request) {

        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

}
