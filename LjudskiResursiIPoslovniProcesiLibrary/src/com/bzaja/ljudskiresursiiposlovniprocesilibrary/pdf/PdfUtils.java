package com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

public final class PdfUtils {

    private PdfUtils() {

    }

    public static Paragraph createParagraph(String text, Font font, int alignment) {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(alignment);
        return paragraph;
    }

    public static Paragraph createNewLineByParagraph() {
        return new Paragraph(" ");
    }
}
