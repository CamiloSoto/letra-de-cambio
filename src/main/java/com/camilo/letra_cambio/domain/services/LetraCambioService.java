package com.camilo.letra_cambio.domain.services;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.domain.dtos.LetraCambioRequest;
import com.camilo.letra_cambio.persistence.entities.EstadoLetra;
import com.camilo.letra_cambio.persistence.entities.LetraCambioEntity;
import com.camilo.letra_cambio.persistence.repositories.LetraCambioJpaRepository;

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

        private final LetraCambioJpaRepository repository;

        public LetraCambioEntity crearLetraCambio(LetraCambioRequest request) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

                LetraCambioEntity letra = LetraCambioEntity.builder()
                                .ciudad(request.getCiudad())
                                .monto(request.getMonto())
                                .montoLetras(request.getMontoLetras())
                                .fechaEmision(LocalDate.parse(request.getFechaEmision(), formatter))
                                .fechaVencimiento(LocalDate.parse(request.getFechaVencimiento(), formatter))
                                .giradorNombre(request.getGirador().getNombre())
                                .giradorDocumento(request.getGirador().getDocumento())
                                .giradorDocumentoCiudad(request.getGirador().getDocumentoCiudad())
                                .giradoNombre(request.getGirado().getNombre())
                                .giradoDocumento(request.getGirado().getDocumento())
                                .giradoDocumentoCiudad(request.getGirado().getDocumentoCiudad())
                                .beneficiarioNombre(request.getBeneficiario().getNombre())
                                .beneficiarioDocumento(request.getBeneficiario().getDocumento())
                                .beneficiarioDocumentoCiudad(request.getBeneficiario().getDocumentoCiudad())
                                .createdAt(LocalDateTime.now())
                                .estado(EstadoLetra.BORRADOR)
                                .build();

                return repository.save(letra);
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
                        params.put("giradorNombre", request.getGirador().getNombre());
                        params.put("giradorDocumento", request.getGirador().getDocumento());
                        params.put("giradoNombre", request.getGirado().getNombre());
                        params.put("giradoDocumento", request.getGirado().getDocumento());

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