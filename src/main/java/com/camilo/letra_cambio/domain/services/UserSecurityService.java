package com.camilo.letra_cambio.domain.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.camilo.letra_cambio.persistence.entities.UserEntity;
import com.camilo.letra_cambio.persistence.repositories.UserJpaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserSecurityService implements UserDetailsService {
    private final UserJpaRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = this.repository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .build();
    }
}