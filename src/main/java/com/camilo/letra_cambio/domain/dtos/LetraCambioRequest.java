package com.camilo.letra_cambio.domain.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetraCambioRequest {
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    @NotBlank(message = "El monto en letras es obligatorio")
    private String montoLetras;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @FutureOrPresent(message = "La fecha de vencimiento no puede ser en el pasado")
    private LocalDate fechaVencimiento;

    @NotBlank(message = "Debes especificar los intereses")
    private BigDecimal intereses;

    @NotNull(message = "El girador es obligatorio")
    @Valid
    private UserRequest girador;

    @NotNull(message = "El girado es obligatorio")
    @Valid
    private UserRequest girado;

    @NotNull(message = "El beneficiario es obligatorio")
    @Valid
    private UserRequest beneficiario;
}
