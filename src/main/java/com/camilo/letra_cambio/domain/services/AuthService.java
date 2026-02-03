package com.camilo.letra_cambio.domain.services;

import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.domain.dtos.RegisterRequest;
import com.camilo.letra_cambio.domain.dtos.ValidateUserRequest;
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
    }

    public void validateUser(ValidateUserRequest request, String emailCode, String email) {

        UserEntity user = userService.findByEmailAndEmailCode(email, emailCode)
                .orElseThrow(() -> new IllegalArgumentException("Datos inválidos"));

        user.setDocumentNumber(request.getDocumentNumber());
        user.setDocumentType(request.getDocumentType());
        user.setEmailCode("");
        user.setEmailVerified(true);
        user.setLastName(request.getLastName());

        userService.update(user);

    }

}
