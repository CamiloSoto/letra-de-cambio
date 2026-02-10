package com.camilo.letra_cambio.domain.services;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.domain.dtos.LetraCambioRequest;
import com.camilo.letra_cambio.persistence.entities.LetraCambioEntity;

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

        public LetraCambioEntity crearLetraCambio(LetraCambioRequest request) {

                // UserEntity girador = userRepository.findById(request.getGiradorId())
                // .orElseThrow(() -> new IllegalArgumentException("Girador no existe"));

                // UserEntity girado = userRepository.findById(request.getGiradoId())
                // .orElseThrow(() -> new IllegalArgumentException("Girado no existe"));

                // UserEntity beneficiario =
                // userRepository.findById(request.getBeneficiarioId())
                // .orElseThrow(() -> new IllegalArgumentException("Beneficiario no existe"));

                // if (request.getFechaVencimiento().isBefore(LocalDate.now())) {
                // throw new IllegalArgumentException("La fecha de vencimiento debe ser
                // futura");
                // }

                // LetraCambioEntity letra = LetraCambioEntity.builder()
                // .monto(request.getMonto())
                // .fechaEmision(LocalDate.now())
                // .fechaVencimiento(request.getFechaVencimiento())
                // .estado(EstadoLetra.BORRADOR)
                // .girador(girador)
                // .girado(girado)
                // .beneficiario(beneficiario)
                // .lugarPago(request.getLugarPago())
                // .createdAt(LocalDateTime.now())
                // .build();

                // return letraCambioRepository.save(letra);
                return null;
        }

        public byte[] generarPdf(LetraCambioRequest request) {
                try {
                        InputStream jrxml = getClass().getResourceAsStream("/reports/letra_cambio_simple.jrxml");

                        JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);

                        Map<String, Object> params = new HashMap<>();

                        params.put("ciudad", request.getCiudad());
                        params.put("monto", request.getMonto().toString());
                        params.put("montoLetras", request.getMontoLetras());
                        params.put("fechaEmision", request.getFechaEmision());
                        params.put("fechaVencimiento", request.getFechaVencimiento());
                        params.put("nombreGirador", request.getGirador().getNombre());

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