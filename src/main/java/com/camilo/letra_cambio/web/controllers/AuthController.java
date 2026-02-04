package com.camilo.letra_cambio.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camilo.letra_cambio.domain.dtos.AuthResponse;
import com.camilo.letra_cambio.domain.dtos.LoginRequest;
import com.camilo.letra_cambio.domain.dtos.RegisterRequest;
import com.camilo.letra_cambio.domain.dtos.ValidateUserRequest;
import com.camilo.letra_cambio.domain.services.AuthService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        service.register(request);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateAccount(
            @RequestBody ValidateUserRequest request,
            @RequestParam("ec") String emailCode,
            @RequestParam String email) {
        service.validateUser(request, emailCode, email);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(service.refresh(refreshToken));
    }

}
