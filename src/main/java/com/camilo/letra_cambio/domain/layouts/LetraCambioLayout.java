package com.camilo.letra_cambio.domain.layouts;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class LetraCambioLayout {
    public static final float MARGIN = 30;
    public static final float WIDTH = PDRectangle.LETTER.getWidth() - 2 * MARGIN;
    public static final float HEIGHT = PDRectangle.LETTER.getHeight() - 2 * MARGIN;

    public static final float TITLE_Y = HEIGHT - 20;
}
