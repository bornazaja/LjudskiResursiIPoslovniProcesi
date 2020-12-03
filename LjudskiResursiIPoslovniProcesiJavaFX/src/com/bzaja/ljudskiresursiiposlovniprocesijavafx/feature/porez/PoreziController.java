/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.porez;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezService;
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
public class PoreziController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<PorezDto> poreziPageableTableView;

    @Autowired
    private PorezService porezService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<PorezDto> porezPage;

    private static final List<String> DISPLAY_MAMES = Arrays.asList("ID Porez", "Vrsta poreza", "Stopa", "Min. osnovica", "Max. osnovica", "Država");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idPorez", "vrstaPoreza.naziv", "stopa", "minOsnovica", "maxOsnovica", "drzava.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_MAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), poreziPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idPorez"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(poreziPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        poreziPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new PorezDto()));
        poreziPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        poreziPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        poreziPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        poreziPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        poreziPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(porezPage, pageIndex, () -> refreshPageableTableView(true)));
        poreziPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(porezPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(PorezDto porezDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(porezDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(porezDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(porezDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), poreziPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            porezPage = !enableSearchAndSort ? porezService.findAll(queryCriteriaDto.getPageable()) : porezService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(poreziPageableTableView, porezPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja poreza.");
    }

    private void onDodajUrediClick(PorezDto porezDto) {
        DodajUrediPorezController dodajUrediPorezController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_POREZ, porezDto, AppUtils.getCssPathables());
        if (dodajUrediPorezController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(PorezDto porezDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji poreza", porezDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(PorezDto porezDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da žeilte izbrisati ovaj porez?", porezDto, propertiesInfo, () -> {
            porezService.delete(porezDto.getIdPorez());
            refreshPageableTableView(true);
        }, "Porez je uspješno izbrisan.", "Desila se greška prilikom brisanja poreza.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.POREZ, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Porezi", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("POREZI", porezService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis poreza je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa poreza u PDF.");
    }
}
