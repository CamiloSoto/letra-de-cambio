package com.camilo.letra_cambio.persistence.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Index;

@Entity
@Table(name = "firma_electronica", indexes = {
        @Index(name = "idx_firma_letra", columnList = "letra_cambio_id"),
        @Index(name = "idx_firma_email", columnList = "email"),
        @Index(name = "idx_firma_tipo", columnList = "tipo")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirmaElectronicaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificación del documento
    @Column(name = "letra_cambio_id", nullable = false, length = 50)
    private String letraCambioId;

    // Identidad del firmante
    @Column(nullable = false, length = 150)
    private String email;

    // Rol del firmante
    @Column(nullable = false, length = 20)
    private String tipo; // GIRADOR / GIRADO

    // Método de autenticación
    @Column(nullable = false, length = 30)
    private String metodo; // OTP_EMAIL

    // Evidencia criptográfica
    @Column(name = "hash_documento", nullable = true, length = 256)
    private String hashDocumento;

    // Momento exacto de la firma
    @Column(name = "fecha_firma", nullable = false)
    private LocalDateTime fechaFirma;

    // Auditoría técnica
    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "user_agent", length = 300)
    private String userAgent;

    @Column(nullable = false)
    private boolean vigente;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // =====================
    // Callbacks JPA
    // =====================

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
