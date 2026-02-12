package com.camilo.letra_cambio.domain.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.persistence.entities.OtpEntity;
import com.camilo.letra_cambio.persistence.entities.TipoFirma;
import com.camilo.letra_cambio.persistence.repositories.OtpJpaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OtpService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int OTP_LENGTH = 6;
    private final OtpJpaRepository otpRepository;
    private final MailService emailService;

    public static String generateNumericOtp() {
        int bound = (int) Math.pow(10, OTP_LENGTH);
        int number = SECURE_RANDOM.nextInt(bound);
        return String.format("%0" + OTP_LENGTH + "d", number);
    }

    public static String generateSalt() {
        return UUID.randomUUID().toString();
    }

    public static String hashOtp(String otp, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((otp + salt).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    public void generateAndSendOtp(String email,
                                   String letraCambioId,
                                   TipoFirma tipo) {

        // 1️⃣ Generar OTP
        String otp = generateNumericOtp();

        // 2️⃣ Generar salt y hash
        String salt = generateSalt();
        String otpHash = hashOtp(otp, salt);

        // 3️⃣ Construir entidad OTP
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtpHash(otpHash);
        otpEntity.setSalt(salt);
        otpEntity.setEmail(email);
        otpEntity.setLetraCambioId(letraCambioId);
        otpEntity.setTipo(tipo.name());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpEntity.setUsed(false);
        otpEntity.setCreatedAt(LocalDateTime.now());

        otpRepository.save(otpEntity);

        // 4️⃣ Enviar correo
        emailService.sendOtpEmail(
                email,
                otp,
                tipo,
                letraCambioId
        );
    }

}
