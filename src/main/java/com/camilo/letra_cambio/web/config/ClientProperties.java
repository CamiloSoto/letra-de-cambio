package com.camilo.letra_cambio.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "client")
@Data
public class ClientProperties {

    private String url;

}
