package com.camilo.letra_cambio.domain.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.persistence.entities.RefreshTokenEntity;
import com.camilo.letra_cambio.persistence.entities.UserEntity;
import com.camilo.letra_cambio.persistence.repositories.RefreshTokenJpaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenJpaRepository repository;

    public RefreshTokenEntity create(UserEntity user, String token) {

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        return repository.save(refreshToken);
    }

    public RefreshTokenEntity validate(String token) {

        RefreshTokenEntity stored = repository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));

        if (stored.isRevoked()) {
            throw new IllegalStateException("Refresh token revocado");
        }

        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Refresh token expirado");
        }

        return stored;
    }

    public void revokeAll(UserEntity user) {
        repository.deleteByUser(user);
    }
}
