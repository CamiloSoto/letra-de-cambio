package com.camilo.letra_cambio.domain.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.domain.dtos.AuthResponse;
import com.camilo.letra_cambio.domain.dtos.LoginRequest;
import com.camilo.letra_cambio.domain.dtos.RegisterRequest;
import com.camilo.letra_cambio.domain.dtos.ValidateUserRequest;
import com.camilo.letra_cambio.persistence.entities.UserEntity;
import com.camilo.letra_cambio.web.config.ClientProperties;
import com.camilo.letra_cambio.web.config.JwtUtil;
import org.springframework.security.core.Authentication;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    private final ClientProperties clientProperties;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final MailService mailService;

    public void register(RegisterRequest request) {

        UserEntity user = userService.signIn(request.getName(), request.getEmail(), request.getPassword());

        String url = clientProperties.getUrl() + "/auth/verify?tk=" + user.getEmailCode() + "&email=" + user.getEmail();
        mailService.sendVerificationEmail(user.getEmail(), user.getFirstName(), url);
    }

    public AuthResponse login(LoginRequest request) {

        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);

        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication.getPrincipal());

        return AuthResponse.builder()
                .token(this.jwtUtil.create(request.getEmail()))
                .refreshToken(this.jwtUtil.refreshToken(request.getEmail()))
                .build();
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
