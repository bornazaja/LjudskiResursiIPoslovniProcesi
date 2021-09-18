package com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PdfTableSingleBeanConfiguration {

    private Integer horizontalAlignment;
    private Float widthPercentage;
    private PdfCellConfiguration headerPdfCellConfiguration;
    private PdfCellConfiguration dataPdfCellConfiguration;
    private PdfTableHeaderOrientation pdfTableHeaderOrientation;
}
