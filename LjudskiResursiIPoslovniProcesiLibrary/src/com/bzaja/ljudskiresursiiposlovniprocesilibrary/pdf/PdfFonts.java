package com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import java.io.IOException;

public final class PdfFonts {

    private static final String REGULAR_FONT_PATH = "fonts/OpenSans-Regular.ttf";
    private static final String BOLD_FONT_PATH = "fonts/OpenSans-Bold.ttf";

    public static final Font LARGE_BOLD = new Font(getBaseFont(BOLD_FONT_PATH), 16);
    public static final Font MEDIUM_BOLD = new Font(getBaseFont(BOLD_FONT_PATH), 12);
    public static final Font SMALL_NORMAL = new Font(getBaseFont(REGULAR_FONT_PATH), 10);
    public static final Font SMALL_BOLD = new Font(getBaseFont(BOLD_FONT_PATH), 10);
    
    private PdfFonts() {

    }

    private static BaseFont getBaseFont(String fontPath) {
        try {
            return BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, true);
        } catch (DocumentException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
