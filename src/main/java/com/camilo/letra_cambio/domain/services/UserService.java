package com.camilo.letra_cambio.domain.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.persistence.entities.UserEntity;
import com.camilo.letra_cambio.persistence.repositories.UserJpaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository repository;

    public UserEntity signIn(String name, String email, String password) {
        UserEntity user = UserEntity.builder()
                .firstName(name)
                .lastName("")
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .state(true)
                .build();
        return repository.save(user);
    }

}
