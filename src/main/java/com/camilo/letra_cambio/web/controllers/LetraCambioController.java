package com.camilo.letra_cambio.web.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<LetraCambioEntity> print(@RequestParam String id) {
        LetraCambioEntity letra = service.generarPdf(id);
        return ResponseEntity.ok(letra);
    }

    @PostMapping
    public ResponseEntity<LetraCambioEntity> crearLetra(@RequestBody LetraCambioRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        System.out.println("Usuario autenticado: " + email);
        LetraCambioEntity letra = service.crearLetraCambio(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(letra);
    }

    @GetMapping
    public ResponseEntity<List<LetraCambioEntity>> list(@RequestParam String documento) {
        List<LetraCambioEntity> letras = service.listar(documento);
        return ResponseEntity.ok(letras);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LetraCambioEntity>> findAll(Authentication authentication) {
        List<LetraCambioEntity> letras = service.findAll(authentication);
        return ResponseEntity.ok(letras);
    }
}
