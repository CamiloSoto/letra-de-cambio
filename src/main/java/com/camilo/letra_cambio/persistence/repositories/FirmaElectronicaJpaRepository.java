package com.camilo.letra_cambio.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camilo.letra_cambio.persistence.entities.FirmaElectronicaEntity;

public interface FirmaElectronicaJpaRepository extends JpaRepository<FirmaElectronicaEntity, Long> {
    List<FirmaElectronicaEntity> findByLetraCambioId(String letraCambioId);

}
