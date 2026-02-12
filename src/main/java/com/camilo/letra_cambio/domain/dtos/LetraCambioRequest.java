package com.camilo.letra_cambio.domain.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetraCambioRequest {
    private String ciudad;
    private BigDecimal monto;
    private String montoLetras;
    private String fechaEmision;
    private String fechaVencimiento;
    private String intereses;
    private UserRequest girador;
    private UserRequest girado;
    private UserRequest beneficiario;
}
