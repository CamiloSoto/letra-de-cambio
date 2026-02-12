package com.camilo.letra_cambio.domain.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.persistence.entities.FirmaElectronicaEntity;
import com.camilo.letra_cambio.persistence.repositories.FirmaElectronicaJpaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FirmaElectronicaService {

    private final FirmaElectronicaJpaRepository firmaRepository;

    public void registrarFirma(FirmaElectronicaEntity firma) {
        firma.setFechaFirma(LocalDateTime.now());
        firmaRepository.save(firma);
    }

}
