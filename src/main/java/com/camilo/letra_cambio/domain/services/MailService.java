package com.camilo.letra_cambio.domain.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

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

}
