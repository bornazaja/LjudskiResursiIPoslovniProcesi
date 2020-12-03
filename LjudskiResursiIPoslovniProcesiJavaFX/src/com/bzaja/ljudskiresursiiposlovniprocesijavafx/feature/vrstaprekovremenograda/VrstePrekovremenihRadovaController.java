/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstaprekovremenograda;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda.VrstaPrekovremenogRadaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda.VrstaPrekovremenogRadaService;
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
public class VrstePrekovremenihRadovaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<VrstaPrekovremenogRadaDto> vrstePrekovremenihRadovaPageableTableView;

    @Autowired
    private VrstaPrekovremenogRadaService vrstaPrekovremenogRadaService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<VrstaPrekovremenogRadaDto> vrstaPrekovremenogRadaPage;

    private static final List<String> DIPSPLY_NAMES = Arrays.asList("ID Vrsta prekovremenog rada", "Naziv", "Koeficjent");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idVrstaPrekovremenogRada", "naziv", "koeficjent");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DIPSPLY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), vrstePrekovremenihRadovaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idVrstaPrekovremenogRada"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(vrstePrekovremenihRadovaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        vrstePrekovremenihRadovaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new VrstaPrekovremenogRadaDto()));
        vrstePrekovremenihRadovaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        vrstePrekovremenihRadovaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        vrstePrekovremenihRadovaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        vrstePrekovremenihRadovaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        vrstePrekovremenihRadovaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(vrstaPrekovremenogRadaPage, pageIndex, () -> refreshPageableTableView(true)));
        vrstePrekovremenihRadovaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(vrstaPrekovremenogRadaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(vrstaPrekovremenogRadaDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(vrstaPrekovremenogRadaDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(vrstaPrekovremenogRadaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), vrstePrekovremenihRadovaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            vrstaPrekovremenogRadaPage = !enableSearchAndSort ? vrstaPrekovremenogRadaService.findAll(queryCriteriaDto.getPageable()) : vrstaPrekovremenogRadaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(vrstePrekovremenihRadovaPageableTableView, vrstaPrekovremenogRadaPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja vrste prekovremenih radova.");
    }

    private void onDodajUrediClick(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto) {
        DodajUrediVrstuPrekovremenogRadaController dodajUrediVrstuPrekovremenogRadaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_VRSTU_PREKOVREMENOG_RADA, vrstaPrekovremenogRadaDto, AppUtils.getCssPathables());
        if (dodajUrediVrstuPrekovremenogRadaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji vrste prekovremenog rada", vrstaPrekovremenogRadaDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu vrstu prekovremenog rada?", vrstaPrekovremenogRadaDto, propertiesInfo, () -> {
            vrstaPrekovremenogRadaService.delete(vrstaPrekovremenogRadaDto.getIdVrstaPrekovremenogRada());
            refreshPageableTableView(true);
        }, "Vrsta prekovremenog rada je uspješno izbrisana.", "Desila se greška prilikom brisanja vrste prekovremenog rada.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.VRSTA_PREKOVREMENOG_RADA, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Vrste_Prekovremenih_Radova", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("VRSTE PREKOVREMENIH RADOVA", vrstaPrekovremenogRadaService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis vrsta prekovremenih radova je uspješno eksportiran. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa vrsta prekovremenih radova u PDF.");
    }
}
