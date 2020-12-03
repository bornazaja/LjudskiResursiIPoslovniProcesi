/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.drzava;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanClass;
import com.bzaja.myjavalibrary.pdf.GenericBeanReportPdf;
import com.bzaja.myjavalibrary.springframework.data.query.Operator;
import com.bzaja.myjavalibrary.springframework.data.query.QueryCriteriaDto;
import com.bzaja.myjavalibrary.util.PropertyInfoDto;
import com.bzaja.myjavalibrary.util.PropertyInfoUtils;
import com.bzaja.myjavalibrary.util.StringUtils;
import com.bzaja.myjavafxlibrary.control.PageableTableView;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
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
public class DrzaveController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<DrzavaDto> drzavePageableTableView;

    @Autowired
    private DrzavaService drzavaService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<DrzavaDto> drzavaPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Država", "Naziv", "Valuta", "Je domovina");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idDrzava", "naziv", "valuta.naziv", "jeDomovina");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), drzavePageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idDrzava"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(drzavePageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        drzavePageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new DrzavaDto()));
        drzavePageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        drzavePageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        drzavePageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        drzavePageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        drzavePageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(drzavaPage, pageIndex, () -> refreshPageableTableView(true)));
        drzavePageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(drzavaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(DrzavaDto drzavaDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(drzavaDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(drzavaDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(drzavaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), drzavePageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            drzavaPage = !enableSearchAndSort ? drzavaService.findAll(queryCriteriaDto.getPageable()) : drzavaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(drzavePageableTableView, drzavaPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja država.");
    }

    private void onDodajUrediClick(DrzavaDto drzavaDto) {
        DodajUrediDrzavuController dodajUrediDrzavuController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_DRZAVU, drzavaDto, AppUtils.getCssPathables());
        if (dodajUrediDrzavuController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(DrzavaDto drzavaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji države", drzavaDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(DrzavaDto drzavaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu državu?", drzavaDto, propertiesInfo, () -> {
            drzavaService.delete(drzavaDto.getIdDrzava());
            refreshPageableTableView(true);
        }, "Država je uspješno izbrisana.", "Desila se greška prilikom brisanja države.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.DRZAVA, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Drzave", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("DRŽAVE", drzavaService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis država je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa država u PDF.");
    }
}
