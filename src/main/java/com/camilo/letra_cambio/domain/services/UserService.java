package com.camilo.letra_cambio.domain.services;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.persistence.entities.UserEntity;
import com.camilo.letra_cambio.persistence.repositories.UserJpaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository repository;

    public UserEntity signIn(String name, String email, String password) {
        // validar si existe el usuario
        
        UUID emailCode = UUID.randomUUID();

        UserEntity user = UserEntity.builder()
                .firstName(name)
                .lastName("")
                .email(email)
                .emailCode(emailCode.toString())
                .passwordHash(passwordEncoder.encode(password))
                .emailVerified(false)
                .state(true)
                .build();
        // TODO: Armar la url para enviar 
        mailService.sendVerificationEmail(email, name, emailCode.toString());
        return repository.save(user);
    }

}
