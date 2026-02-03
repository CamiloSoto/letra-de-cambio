package com.camilo.letra_cambio.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {
    private static final String SEED = "Colombia1.";

    public String extractSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SEED);
            JWTVerifier verifier = JWT.require(algorithm).build();
            String decodedToken = verifier.verify(token).getToken();
            return JWT.decode(decodedToken).getSubject();
        } catch (JWTDecodeException e) {
            throw new RuntimeException("Error al decodificar el token JWT", e);
        }
    }
}