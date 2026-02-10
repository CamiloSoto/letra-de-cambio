package com.camilo.letra_cambio.domain.services;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.domain.dtos.LetraCambioRequest;
import com.camilo.letra_cambio.persistence.entities.EstadoLetra;
import com.camilo.letra_cambio.persistence.entities.LetraCambioEntity;
import com.camilo.letra_cambio.persistence.entities.UserEntity;
import com.camilo.letra_cambio.persistence.repositories.LetraCambioJpaRepository;
import com.camilo.letra_cambio.persistence.repositories.UserJpaRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
@AllArgsConstructor
@Transactional
public class LetraCambioService {

        private final LetraCambioJpaRepository letraCambioRepository;
        private final UserJpaRepository userRepository;

        public LetraCambioEntity crearLetraCambio(LetraCambioRequest request) {

                UserEntity girador = userRepository.findById(request.getGiradorId())
                                .orElseThrow(() -> new IllegalArgumentException("Girador no existe"));

                UserEntity girado = userRepository.findById(request.getGiradoId())
                                .orElseThrow(() -> new IllegalArgumentException("Girado no existe"));

                UserEntity beneficiario = userRepository.findById(request.getBeneficiarioId())
                                .orElseThrow(() -> new IllegalArgumentException("Beneficiario no existe"));

                if (request.getFechaVencimiento().isBefore(LocalDate.now())) {
                        throw new IllegalArgumentException("La fecha de vencimiento debe ser futura");
                }

                LetraCambioEntity letra = LetraCambioEntity.builder()
                                .monto(request.getMonto())
                                .fechaEmision(LocalDate.now())
                                .fechaVencimiento(request.getFechaVencimiento())
                                .estado(EstadoLetra.BORRADOR)
                                .girador(girador)
                                .girado(girado)
                                .beneficiario(beneficiario)
                                .lugarPago(request.getLugarPago())
                                .createdAt(LocalDateTime.now())
                                .build();

                return letraCambioRepository.save(letra);
        }

        public byte[] generarPdf() {
                try {
                        InputStream jrxml = getClass().getResourceAsStream("/reports/letra_cambio.jrxml");

                        JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);

                        Map<String, Object> params = new HashMap<>();
                        params.put("fecha", "28 de frebrero del 2025");
                        params.put("fechaPago", "28 de Noviembre del 2025");
                        params.put("numero", "1");
                        params.put("valor", "4.000.000");
                        params.put("valorLetras", "Cuatro millones de pesos");
                        params.put("senores", "Maria Daniela Martinez Rodriguez");
                        params.put("ciudad", "Bogotá");
                        params.put("nombreGirador", "Jessica Gonzales Santodomoingo");
                        params.put("direccion", "cll 70 a bis a # 117-96");
                        params.put("telefono", "3059294167");

                        JasperPrint jasperPrint = JasperFillManager.fillReport(
                                        jasperReport,
                                        params,
                                        new JREmptyDataSource());

                        return JasperExportManager.exportReportToPdf(jasperPrint);
                } catch (Exception e) {
                        throw new RuntimeException("Error generando PDF", e);
                }
        }

}