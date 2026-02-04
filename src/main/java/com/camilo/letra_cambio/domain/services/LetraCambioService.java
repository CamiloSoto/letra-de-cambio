package com.camilo.letra_cambio.domain.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.springframework.stereotype.Service;

import com.camilo.letra_cambio.domain.dtos.LetraCambioRequest;
import com.camilo.letra_cambio.persistence.entities.EstadoLetra;
import com.camilo.letra_cambio.persistence.entities.LetraCambioEntity;
import com.camilo.letra_cambio.persistence.entities.UserEntity;
import com.camilo.letra_cambio.persistence.repositories.LetraCambioJpaRepository;
import com.camilo.letra_cambio.persistence.repositories.UserJpaRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

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

    public byte[] generarLetraCambio() throws IOException {

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {

                float pageHeight = PDRectangle.LETTER.getHeight();

                dibujarSeccion1Lateral(cs, pageHeight);
                dibujarSeccion2Encabezado(cs, pageHeight);
                dibujarSeccion3Cuerpo(cs, pageHeight);
                dibujarSeccion4Aceptantes(cs, pageHeight);
                dibujarSeccion5Firma(cs);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);

            return baos.toByteArray();
        }
    }

    private void dibujarSeccion1Lateral(PDPageContentStream cs, float pageHeight)
            throws IOException {

        float x = 30;
        float y = 30;
        float width = 80;
        float height = pageHeight - 60;

        // ===== Marco de la sección =====
        cs.setLineWidth(1f);
        cs.addRect(x, y, width, height);
        cs.stroke();

        // ===== Banda ACEPTADA =====
        float headerHeight = 30;
        cs.addRect(x, y + height - headerHeight, width, headerHeight);
        cs.stroke();

        escribirCentrado(cs, "ACEPTADA",
                x + width / 2,
                y + height - 20,
                PDType1Font.HELVETICA_BOLD,
                9);

        escribirCentrado(cs, "(Girados)",
                x + width / 2,
                y + height - 32,
                PDType1Font.HELVETICA,
                7);

        // ===== Columnas 1, 2, 3 =====
        float columnasTop = y + height - headerHeight;
        float columnasBottom = y + 80;
        float colWidth = width / 3;

        for (int i = 1; i <= 2; i++) {
            cs.moveTo(x + colWidth * i, columnasBottom);
            cs.lineTo(x + colWidth * i, columnasTop);
            cs.stroke();
        }

        // Líneas horizontales (3 filas)
        float filaHeight = (columnasTop - columnasBottom) / 3;

        for (int i = 1; i <= 2; i++) {
            cs.moveTo(x, columnasBottom + filaHeight * i);
            cs.lineTo(x + width, columnasBottom + filaHeight * i);
            cs.stroke();
        }

        // Números 1, 2, 3
        for (int i = 0; i < 3; i++) {
            escribirCentrado(cs,
                    String.valueOf(i + 1),
                    x + colWidth * i + colWidth / 2,
                    columnasTop - filaHeight / 2,
                    PDType1Font.HELVETICA,
                    8);
        }

        // ===== Texto vertical "Cdo. M.L." =====
        escribirVertical(cs,
                "Cdo. M.L.",
                x + width / 2,
                y + 50,
                PDType1Font.HELVETICA,
                7);

        // ===== Código vertical LC-21 =====
        escribirVertical(cs,
                "LC-21 0629004",
                x + width - 10,
                y + height / 2,
                PDType1Font.HELVETICA_BOLD,
                9);
    }

    private void dibujarSeccion2Encabezado(PDPageContentStream cs, float pageHeight)
            throws IOException {

        float x = 30 + 80 + 10; // después de sección 1
        float y = pageHeight - 30 - 60; // arriba
        float width = 552 - 80 - 10;
        float height = 60;

        // ===== Marco del encabezado =====
        cs.setLineWidth(1f);
        cs.addRect(x, y, width, height);
        cs.stroke();

        // ===== Título =====
        escribirCentrado(
                cs,
                "LETRA DE CAMBIO",
                x + width / 2,
                y + height - 20,
                PDType1Font.HELVETICA_BOLD,
                14);

        // ===== Línea inferior del título =====
        cs.moveTo(x, y + height - 30);
        cs.lineTo(x + width, y + height - 30);
        cs.stroke();

        float camposY = y + 15;

        // ===== Fecha =====
        escribir(cs, "Fecha:", x + 10, camposY, true);
        linea(cs, x + 55, camposY - 2, 120);

        // ===== No. =====
        escribir(cs, "No.:", x + 200, camposY, true);
        linea(cs, x + 235, camposY - 2, 80);

        // ===== Por $ =====
        escribir(cs, "Por $:", x + 340, camposY, true);
        linea(cs, x + 390, camposY - 2, 120);
    }

    private void dibujarSeccion3Cuerpo(
            PDPageContentStream cs,
            float pageHeight) throws IOException {

        float x = 30 + 80 + 10; // después sección 1
        float yTop = pageHeight - 30 - 60 - 20; // debajo sección 2
        float width = 552 - 80 - 10;

        float cursorY = yTop;

        // ===== Señores =====
        escribir(cs, "Señor(es):", x, cursorY, true);
        linea(cs, x + 70, cursorY - 2, width - 80);

        cursorY -= 25;

        // ===== Fecha detallada =====
        escribir(cs, "El", x, cursorY, false);
        linea(cs, x + 20, cursorY - 2, 40);

        escribir(cs, "de", x + 70, cursorY, false);
        linea(cs, x + 90, cursorY - 2, 120);

        escribir(cs, "del año", x + 220, cursorY, false);
        linea(cs, x + 280, cursorY - 2, 60);

        cursorY -= 35;

        // ===== Texto legal =====
        String textoLegal = "Se servirá(n) ud(s) pagar solidariamente en la fecha indicada, "
                + "por esta única letra de cambio sin protesto, excusado el aviso de rechazo "
                + "a la orden de:";

        cursorY = escribirMultilinea(
                cs,
                textoLegal,
                x,
                cursorY,
                width,
                9);

        cursorY -= 10;

        // ===== A la orden de =====
        linea(cs, x, cursorY, width);
        cursorY -= 25;

        // ===== La cantidad de =====
        escribir(cs, "La cantidad de:", x, cursorY, true);
        linea(cs, x + 95, cursorY - 2, width - 100);

        cursorY -= 25;

        escribir(cs, "Pesos M/L", x, cursorY, false);

        escribir(cs, "($", x + width - 120, cursorY, false);
        linea(cs, x + width - 90, cursorY - 2, 80);
        escribir(cs, ")", x + width - 5, cursorY, false);

        cursorY -= 30;

        // ===== Cuotas =====
        escribir(cs, "en", x, cursorY, false);
        linea(cs, x + 20, cursorY - 2, 30);

        escribir(cs, "cuota(s) de $", x + 60, cursorY, false);
        linea(cs, x + 140, cursorY - 2, 80);

        escribir(cs, ", más intereses durante el plazo del", x + 230, cursorY, false);
        linea(cs, x + 420, cursorY - 2, 40);

        escribir(cs, "% mensual y de mora a la tasa máxima legal autorizada.",
                x,
                cursorY - 20,
                false);
    }

    private void dibujarSeccion4Aceptantes(
            PDPageContentStream cs,
            float pageHeight) throws IOException {

        float x = 30 + 80 + 10; // después sección 1
        float y = 170; // arriba de la firma
        float width = 552 - 80 - 10;
        float height = 120;

        // ===== Marco exterior =====
        cs.setLineWidth(1f);
        cs.addRect(x, y, width, height);
        cs.stroke();

        // ===== Encabezado =====
        cs.moveTo(x, y + height - 25);
        cs.lineTo(x + width, y + height - 25);
        cs.stroke();

        escribir(
                cs,
                "DIRECCIÓN ACEPTANTE(S)",
                x + 5,
                y + height - 18,
                true);

        // ===== Líneas internas =====
        float fila1 = y + height - 50;
        float fila2 = y + height - 75;

        cs.moveTo(x, fila1);
        cs.lineTo(x + width, fila1);
        cs.stroke();

        cs.moveTo(x, fila2);
        cs.lineTo(x + width, fila2);
        cs.stroke();

        // ===== Etiquetas =====
        escribir(cs, "Dirección:", x + 5, fila1 + 7, false);
        escribir(cs, "Ciudad:", x + 5, fila2 + 7, false);
        escribir(cs, "Teléfono:", x + 5, y + 7, false);
    }

    private void dibujarSeccion5Firma(
            PDPageContentStream cs) throws IOException {

        float x = 300;
        float y = 90;
        float width = 200;

        // ===== Línea de firma =====
        cs.moveTo(x, y);
        cs.lineTo(x + width, y);
        cs.stroke();

        // ===== Texto =====
        escribir(cs, "Firma del Girador", x, y - 12, false);

        cs.moveTo(x, y - 35);
        cs.lineTo(x + width, y - 35);
        cs.stroke();
        escribir(cs, "C.C. / NIT", x, y - 48, false);

        cs.moveTo(x, y - 70);
        cs.lineTo(x + width, y - 70);
        cs.stroke();
        escribir(cs, "Ciudad y Fecha", x, y - 83, false);
    }

    private void escribir(
            PDPageContentStream cs,
            String texto,
            float x,
            float y,
            boolean bold) throws IOException {

        cs.beginText();
        cs.setFont(
                bold ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA,
                9);
        cs.newLineAtOffset(x, y);
        cs.showText(texto);
        cs.endText();
    }

    private float escribirMultilinea(
            PDPageContentStream cs,
            String texto,
            float x,
            float y,
            float maxWidth,
            int size) throws IOException {

        PDFont font = PDType1Font.HELVETICA;
        float leading = size * 1.4f;

        StringBuilder linea = new StringBuilder();
        float cursorY = y;

        for (String palabra : texto.split(" ")) {
            float ancho = font.getStringWidth(linea + palabra) / 1000 * size;

            if (ancho > maxWidth) {
                cs.beginText();
                cs.setFont(font, size);
                cs.newLineAtOffset(x, cursorY);
                cs.showText(linea.toString());
                cs.endText();

                linea = new StringBuilder(palabra + " ");
                cursorY -= leading;
            } else {
                linea.append(palabra).append(" ");
            }
        }

        // última línea
        cs.beginText();
        cs.setFont(font, size);
        cs.newLineAtOffset(x, cursorY);
        cs.showText(linea.toString());
        cs.endText();

        return cursorY - leading;
    }

    private void linea(PDPageContentStream cs, float x, float y, float w)
            throws IOException {

        cs.moveTo(x, y);
        cs.lineTo(x + w, y);
        cs.stroke();
    }

    private void escribirCentrado(
            PDPageContentStream cs,
            String texto,
            float centerX,
            float y,
            PDFont font,
            int size) throws IOException {

        float textWidth = font.getStringWidth(texto) / 1000 * size;

        cs.beginText();
        cs.setFont(font, size);
        cs.newLineAtOffset(centerX - textWidth / 2, y);
        cs.showText(texto);
        cs.endText();
    }

    private void escribirVertical(
            PDPageContentStream cs,
            String texto,
            float x,
            float y,
            PDFont font,
            int size) throws IOException {

        cs.saveGraphicsState();
        cs.beginText();
        cs.setFont(font, size);

        cs.setTextMatrix(
                Matrix.getRotateInstance(Math.PI / 2, x, y));

        cs.showText(texto);
        cs.endText();
        cs.restoreGraphicsState();
    }

}