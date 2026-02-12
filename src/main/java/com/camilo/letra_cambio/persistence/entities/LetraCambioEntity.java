package com.camilo.letra_cambio.persistence.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "letras_cambio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LetraCambioEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String ciudad;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    private String montoLetras;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    private String intereses;

    // GIRADOR
    private String giradorNombre;
    private String giradorDocumento;
    private String giradorDocumentoCiudad;

    // GIRADO
    private String giradoNombre;
    private String giradoDocumento;
    private String giradoDocumentoCiudad;

    // BENEFICIARIO
    private String beneficiarioNombre;
    private String beneficiarioDocumento;
    private String beneficiarioDocumentoCiudad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLetra estado;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
