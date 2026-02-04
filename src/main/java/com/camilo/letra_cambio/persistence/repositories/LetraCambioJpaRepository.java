package com.camilo.letra_cambio.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camilo.letra_cambio.persistence.entities.LetraCambioEntity;

public interface LetraCambioJpaRepository extends JpaRepository<LetraCambioEntity, Long> {

}
