package com.camilo.letra_cambio.domain.services;

import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.domain.dtos.RegisterRequest;
import com.camilo.letra_cambio.persistence.entities.UserEntity;
import com.camilo.letra_cambio.web.config.ClientProperties;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    private final ClientProperties clientProperties;
    private final UserService userService;
    private final MailService mailService;

    public void register(RegisterRequest request) {
        
        UserEntity user = userService.signIn(request.getName(), request.getEmail(), request.getPassword());
        
        String url = clientProperties.getUrl() + "/auth/verify?tk=" + user.getEmailCode() + "&email=" + user.getEmail();
        mailService.sendVerificationEmail(user.getEmail(), user.getFirstName(), url);
        //
    }

}
