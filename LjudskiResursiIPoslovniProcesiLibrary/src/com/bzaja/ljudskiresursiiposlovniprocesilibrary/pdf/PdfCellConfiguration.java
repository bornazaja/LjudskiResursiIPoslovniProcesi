package com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PdfCellConfiguration {

    private Font font;
    private BaseColor backgroundColor;
    private PdfCellBorderStyle pdfCellBorderStyle;
    private BaseColor borderColor;
    private Integer horiznotalAlignment;
}
