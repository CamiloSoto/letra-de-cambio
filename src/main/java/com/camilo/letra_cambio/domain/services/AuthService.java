package com.camilo.letra_cambio.domain.services;

import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.domain.dtos.RegisterRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserService userService;

    public void register(RegisterRequest request) {
        userService.signIn(request.getName(), request.getEmail(), request.getPassword());
    }

}
