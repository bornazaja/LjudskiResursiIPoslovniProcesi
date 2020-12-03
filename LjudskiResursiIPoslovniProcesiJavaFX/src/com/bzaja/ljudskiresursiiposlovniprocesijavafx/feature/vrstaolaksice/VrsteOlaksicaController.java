/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstaolaksice;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice.VrstaOlaksiceDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice.VrstaOlaksiceService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanClass;
import com.bzaja.myjavalibrary.pdf.GenericBeanReportPdf;
import com.bzaja.myjavalibrary.springframework.data.query.Operator;
import com.bzaja.myjavalibrary.springframework.data.query.QueryCriteriaDto;
import com.bzaja.myjavalibrary.util.PropertyInfoDto;
import com.bzaja.myjavalibrary.util.PropertyInfoUtils;
import com.bzaja.myjavalibrary.util.StringUtils;
import com.bzaja.myjavafxlibrary.control.PageableTableView;
import com.bzaja.myjavalibrary.util.LanguageFormat;
import com.bzaja.myjavalibrary.util.LocalDateTimePattern;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Controller
public class VrsteOlaksicaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<VrstaOlaksiceDto> vrsteOlaksicaPageableTableView;

    @Autowired
    private VrstaOlaksiceService vrstaOlaksiceService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<VrstaOlaksiceDto> vrstaOlaksicePage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Vrsta olakšice", "Naziv", "Koeficjent", "Vrijedi do");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idVrstaOlaksice", "naziv", "koeficjent", "vrijediDo");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), vrsteOlaksicaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idVrstaOlaksice"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(vrsteOlaksicaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        vrsteOlaksicaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new VrstaOlaksiceDto()));
        vrsteOlaksicaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        vrsteOlaksicaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        vrsteOlaksicaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        vrsteOlaksicaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        vrsteOlaksicaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(vrstaOlaksicePage, pageIndex, () -> refreshPageableTableView(true)));
        vrsteOlaksicaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(vrstaOlaksicePage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(VrstaOlaksiceDto vrstaOlaksiceDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(vrstaOlaksiceDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(vrstaOlaksiceDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(vrstaOlaksiceDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), vrsteOlaksicaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            vrstaOlaksicePage = !enableSearchAndSort ? vrstaOlaksiceService.findAll(queryCriteriaDto.getPageable()) : vrstaOlaksiceService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(vrsteOlaksicaPageableTableView, vrstaOlaksicePage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja vrste olakšica.");
    }

    private void onDodajUrediClick(VrstaOlaksiceDto vrstaOlaksiceDto) {
        DodajUrediVrstuOlaksiceController dodajUrediVrstuOlaksiceController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_VRSTU_OLAKSICE, vrstaOlaksiceDto, AppUtils.getCssPathables());
        if (dodajUrediVrstuOlaksiceController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(VrstaOlaksiceDto vrstaOlaksiceDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji vrste olakšice", vrstaOlaksiceDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(VrstaOlaksiceDto vrstaOlaksiceDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu vrstu olakšice?", vrstaOlaksiceDto, propertiesInfo, () -> {
            vrstaOlaksiceService.delete(vrstaOlaksiceDto.getIdVrstaOlaksice());
            refreshPageableTableView(true);
        }, "Vrste olakšice je uspješno izbrisana.", "Desila se greška prilikom brisanja vrste olakšice.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.VRSTA_OLAKSICE, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Vrste_Olaksica", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("VRSTE OLAKŠICA", vrstaOlaksiceService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis vrsta olakšica je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa vrsta olakšica u PDF.");
    }
}
