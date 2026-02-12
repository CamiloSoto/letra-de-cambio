package com.camilo.letra_cambio.domain.services;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        private final MailService mailService;

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
                                .beneficiarioNombre(request.getBeneficiario() != null
                                                ? request.getBeneficiario().getNombre()
                                                : null)
                                .beneficiarioDocumento(request.getBeneficiario() != null
                                                ? request.getBeneficiario().getDocumento()
                                                : null)
                                .beneficiarioDocumentoCiudad(request.getBeneficiario() != null
                                                ? request.getBeneficiario().getDocumentoCiudad()
                                                : null)
                                .createdAt(LocalDateTime.now())
                                .estado(EstadoLetra.BORRADOR)
                                .intereses(request.getIntereses())
                                .build();

                return repository.save(letra);
        }

        public List<LetraCambioEntity> listar(String documento) {
                return repository.findByGiradorDocumento(documento);
        }

        public LetraCambioEntity generarPdf(String id) {
                try {
                        LetraCambioEntity letra = repository.findById(UUID.fromString(id))
                                        .orElseThrow(() -> new RuntimeException("Letra de cambio no encontrada"));

                        InputStream jrxml = getClass()
                                        .getResourceAsStream("/reports/letra_cambio_simple.jrxml");

                        if (jrxml == null) {
                                throw new RuntimeException("No se encontró el archivo JRXML");
                        }

                        JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);

                        Map<String, Object> params = new HashMap<>();
                        params.put("ciudad", letra.getCiudad());
                        params.put("monto", letra.getMonto().toString());
                        params.put("montoLetras", letra.getMontoLetras());
                        params.put("fechaEmision", letra.getFechaEmision().toString());
                        params.put("fechaVencimiento", letra.getFechaVencimiento().toString());
                        params.put("giradorNombre", letra.getGiradorNombre());
                        params.put("giradorDocumento", letra.getGiradorDocumento());
                        params.put("giradoNombre", letra.getGiradoNombre());
                        params.put("giradoDocumento", letra.getGiradoDocumento());
                        params.put("intereses", letra.getIntereses());

                        JasperPrint jasperPrint = JasperFillManager.fillReport(
                                        jasperReport,
                                        params,
                                        new JREmptyDataSource());

                        // 1️⃣ Generar PDF una sola vez
                        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

                        // 2️⃣ Guardar PDF en disco
                        String rutaBase = "/tmp/letras-cambio"; // ideal: property
                        Files.createDirectories(Paths.get(rutaBase));

                        String nombreArchivo = "letra_cambio_" + letra.getId() + ".pdf";
                        Path rutaPdf = Paths.get(rutaBase, nombreArchivo);

                        Files.write(rutaPdf, pdfBytes);

                        // 3️⃣ Actualizar estado y ruta
                        letra.setEstado(EstadoLetra.GENERADA);
                        // letra.setRutaPdf(rutaPdf.toString());

                        LetraCambioEntity guardada = repository.save(letra);

                        // 4️⃣ Enviar email (adjunto)
                        mailService.sendDocumentEmail("alejandro.vega.lims@gmail.com", letra.getGiradorNombre(),
                                        letra.getBeneficiarioNombre(), letra.getMonto(), letra.getFechaVencimiento(),
                                        pdfBytes);
                        // 5️⃣ Retornar el registro
                        return guardada;
                } catch (Exception e) {
                        throw new RuntimeException("Error generando PDF", e);
                }
        }

}