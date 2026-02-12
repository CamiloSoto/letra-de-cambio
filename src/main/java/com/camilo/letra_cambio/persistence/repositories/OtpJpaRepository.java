package com.camilo.letra_cambio.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camilo.letra_cambio.persistence.entities.OtpEntity;

public interface OtpJpaRepository extends JpaRepository<OtpEntity, Long> {

    Optional<OtpEntity> findTopByEmailAndLetraCambioIdAndTipoAndUsedFalseOrderByCreatedAtDesc(
            String email,
            String letraCambioId,
            String tipo);

}
