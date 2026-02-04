package com.camilo.letra_cambio.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camilo.letra_cambio.persistence.entities.RefreshTokenEntity;
import com.camilo.letra_cambio.persistence.entities.UserEntity;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteByUser(UserEntity user);
}
