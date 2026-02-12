package com.camilo.letra_cambio.persistence.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camilo.letra_cambio.persistence.entities.LetraCambioEntity;

public interface LetraCambioJpaRepository extends JpaRepository<LetraCambioEntity, UUID> {
    List<LetraCambioEntity> findByGiradorDocumento(String documento);

}
