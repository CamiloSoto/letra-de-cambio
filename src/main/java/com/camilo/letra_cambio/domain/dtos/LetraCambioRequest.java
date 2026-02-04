package com.camilo.letra_cambio.domain.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetraCambioRequest {
    private BigDecimal monto;
    private LocalDate fechaVencimiento;
    private Long giradorId;
    private Long giradoId;
    private Long beneficiarioId;
    private String lugarPago;
}
