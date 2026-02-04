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
import jakarta.persistence.ManyToOne;
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

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLetra estado;

    @ManyToOne(optional = false)
    private UserEntity girador;

    @ManyToOne(optional = false)
    private UserEntity girado;

    @ManyToOne(optional = false)
    private UserEntity beneficiario;

    @Column(nullable = false)
    private String lugarPago;

    @Column(nullable = false, unique = true, updatable = false)
    private String numeroLetra;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
