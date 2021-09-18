package com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PdfTableListBeanConfiguration {

    private Integer horizontalAlignmnet;
    private Float widthPercentage;
    private PdfCellConfiguration headerPdfCellConfiguration;
    private PdfCellConfiguration dataPdfCellConfiguration;
    private PdfCellConfiguration footerPdfCellConfiguration;
}
