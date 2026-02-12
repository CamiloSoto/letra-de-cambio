package com.camilo.letra_cambio.domain.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.camilo.letra_cambio.persistence.entities.TipoFirma;
import com.camilo.letra_cambio.web.config.MailProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final SpringTemplateEngine templateEngine;

    public boolean sendEmail(String to, String subject, String body, byte[] attachment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(mailProperties.getUsername());
            // Enviar el correo electrónico

            // Verificamos si hay un archivo adjunto
            if (attachment != null && attachment.length > 0) {
                ByteArrayResource byteArrayResource = new ByteArrayResource(attachment);
                helper.addAttachment("letra_cambio.pdf", byteArrayResource);
            }
            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void sendVerificationEmail(String to, String name, String verificationUrl) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("verificationUrl", verificationUrl);

            String html = templateEngine.process("mail/verify-email", context);

            helper.setTo(to);
            helper.setSubject("Verifica tu correo");
            helper.setFrom(mailProperties.getUsername());
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando correo", e);
        }
    }

    public void sendDocumentEmail(String to, String deudor, String beneficiario, BigDecimal valor,
            LocalDate fechaVencimiento, byte[] attachment) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("deudor", deudor);
            context.setVariable("beneficiario", beneficiario);
            context.setVariable("valor", valor);
            context.setVariable("fechaVencimiento", fechaVencimiento);

            String html = templateEngine.process("mail/document-email", context);

            helper.setTo(to);
            helper.setSubject("Documento de Letra de Cambio");
            helper.setFrom(mailProperties.getUsername());
            helper.setText(html, true);

            if (attachment != null && attachment.length > 0) {
                ByteArrayResource byteArrayResource = new ByteArrayResource(attachment);
                helper.addAttachment("letra_cambio.pdf", byteArrayResource);
            }

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando correo", e);
        }
    }

    public void sendOtpEmail(String to, String otp, TipoFirma tipo, String letraCambioId) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context(Locale.forLanguageTag("es"));
            context.setVariable("otpCode", otp);
            context.setVariable("expirationMinutes", 5);
            context.setVariable("letraCambioId", letraCambioId);
            context.setVariable("tipoFirma", tipo.name());

            String html = templateEngine.process("mail/otp", context);

            helper.setTo(to);
            helper.setSubject("Documento de Letra de Cambio");
            helper.setFrom(mailProperties.getUsername());
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando correo", e);
        }
    }

}
