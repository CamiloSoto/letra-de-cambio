package com.camilo.letra_cambio.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.camilo.letra_cambio.persistence.entities.UserEntity;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private static final String SEED = "Colombia1.";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SEED);

    public String createAccessToken(UserEntity user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userId", user.getId().toString())
                .withClaim("verified", user.isEmailVerified())
                .withClaim("roles", List.of("USER"))
                .withIssuer("olimac")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))
                .sign(ALGORITHM);
    }

    public String createRefreshToken(UserEntity user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuer("olimac")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)))
                .sign(ALGORITHM);
    }

    public Boolean isValid(String jwt) {
        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }

    public String getId(String jwt) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getClaim("userId").asString();
    }
}