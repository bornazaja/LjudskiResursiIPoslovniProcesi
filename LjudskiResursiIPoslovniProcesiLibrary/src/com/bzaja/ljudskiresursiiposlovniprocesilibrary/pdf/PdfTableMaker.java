package com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoDto;
import java.util.List;

public final class PdfTableMaker {

    private PdfTableMaker() {

    }

    public static <T> PdfPTable make(PdfTableListBeanConfiguration pdfTableListBeanConfiguration, List<T> list, List<PropertyInfoDto> propertiesInfo, List<String> footerCells) {
        PdfPTable pdfPTable = createBaseTable(propertiesInfo.size(), pdfTableListBeanConfiguration.getHorizontalAlignmnet(), pdfTableListBeanConfiguration.getWidthPercentage());

        propertiesInfo.forEach((propertyInfo) -> {
            pdfPTable.addCell(createCell(propertyInfo.getDisplayName(), pdfTableListBeanConfiguration.getHeaderPdfCellConfiguration()));
        });

        if (list.isEmpty()) {
            PdfPCell pdfPCell = createCell("Nema sadržaja", pdfTableListBeanConfiguration.getDataPdfCellConfiguration());
            pdfPCell.setColspan(pdfPTable.getNumberOfColumns());
            pdfPTable.addCell(pdfPCell);
            return pdfPTable;
        }

        list.forEach((t) -> {
            propertiesInfo.forEach((propertyInfo) -> {
                pdfPTable.addCell(createCell(BeanUtils.getFormattedPropertyValue(t, propertyInfo.getPropertyName()), pdfTableListBeanConfiguration.getDataPdfCellConfiguration()));
            });
        });

        if (!footerCells.isEmpty() && pdfTableListBeanConfiguration.getFooterPdfCellConfiguration() != null) {
            footerCells.forEach((footerCell) -> {
                pdfPTable.addCell(createCell(footerCell, pdfTableListBeanConfiguration.getFooterPdfCellConfiguration()));
            });
        }

        return pdfPTable;
    }

    public static <T> PdfPTable make(PdfTableSingleBeanConfiguration pdfTableSingleBeanConfiguration, T t, List<PropertyInfoDto> propertiesInfo) {

        switch (pdfTableSingleBeanConfiguration.getPdfTableHeaderOrientation()) {
            case HORIZONTAL:
                PdfPTable pdfPTableHorizontal = createBaseTable(propertiesInfo.size(), pdfTableSingleBeanConfiguration.getHorizontalAlignment(), pdfTableSingleBeanConfiguration.getWidthPercentage());

                propertiesInfo.forEach((propertyInfo) -> {
                    pdfPTableHorizontal.addCell(createCell(propertyInfo.getDisplayName(), pdfTableSingleBeanConfiguration.getHeaderPdfCellConfiguration()));
                });

                propertiesInfo.forEach((propertyInfo) -> {
                    pdfPTableHorizontal.addCell(createCell(BeanUtils.getFormattedPropertyValue(t, propertyInfo.getPropertyName()), pdfTableSingleBeanConfiguration.getDataPdfCellConfiguration()));
                });

                return pdfPTableHorizontal;
            case VERTICAL:
                PdfPTable pdfPTableVertical = createBaseTable(2, pdfTableSingleBeanConfiguration.getHorizontalAlignment(), pdfTableSingleBeanConfiguration.getWidthPercentage());

                propertiesInfo.forEach((propertyInfo) -> {
                    pdfPTableVertical.addCell(createCell(String.format("%s:", propertyInfo.getDisplayName()), pdfTableSingleBeanConfiguration.getHeaderPdfCellConfiguration()));
                    pdfPTableVertical.addCell(createCell(BeanUtils.getFormattedPropertyValue(t, propertyInfo.getPropertyName()), pdfTableSingleBeanConfiguration.getDataPdfCellConfiguration()));
                });

                return pdfPTableVertical;
            default:
                throw new IllegalArgumentException("This pdf table header orientation does not exists");
        }
    }

    private static PdfPTable createBaseTable(Integer numberOfColumns, Integer horizontalAlignment, Float widthPercentage) {
        PdfPTable pdfPTable = new PdfPTable(numberOfColumns);
        pdfPTable.setHorizontalAlignment(horizontalAlignment);
        pdfPTable.setWidthPercentage(widthPercentage);
        return pdfPTable;
    }

    private static PdfPCell createCell(String text, PdfCellConfiguration pdfCellConfiguration) {
        PdfPCell pdfPCell = new PdfPCell(new Paragraph(text, pdfCellConfiguration.getFont()));
        pdfPCell.setBackgroundColor(pdfCellConfiguration.getBackgroundColor());
        pdfPCell.setHorizontalAlignment(pdfCellConfiguration.getHoriznotalAlignment());
        pdfPCell.setBorderColor(pdfCellConfiguration.getBorderColor());

        switch (pdfCellConfiguration.getPdfCellBorderStyle()) {
            case NONE:
                pdfPCell.setBorder(Rectangle.NO_BORDER);
                break;
            case BOTTOM:
                pdfPCell.setBorder(Rectangle.BOTTOM);
                break;
            case TOP:
                pdfPCell.setBorder(Rectangle.TOP);
                break;
            case BOTTOM_TOP:
                pdfPCell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                break;
            case BOX:
                pdfPCell.setBorder(Rectangle.BOX);
                break;
            default:
                throw new IllegalArgumentException("This pdf cell border style does not exits");
        }
        return pdfPCell;
    }
}
