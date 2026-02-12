package com.camilo.letra_cambio.domain.dtos;

import com.camilo.letra_cambio.persistence.entities.TipoFirma;

import lombok.Data;

@Data
public class OtpValidationRequest {

    private String letraCambioId;
    private String email;
    private String otp;
    private TipoFirma tipo;
}
