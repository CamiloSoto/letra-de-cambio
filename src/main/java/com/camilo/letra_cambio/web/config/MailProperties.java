package com.camilo.letra_cambio.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mail")
@Data
public class MailProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
}
