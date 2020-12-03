/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.radnomjesto;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoService;
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
public class RadnaMjestaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<RadnoMjestoDto> radnaMjestaPageableTableView;

    @Autowired
    private RadnoMjestoService radnoMjestoService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<RadnoMjestoDto> radnoMjestoPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Radno mjesto", "Naziv", "Odjel");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idRadnoMjesto", "naziv", "odjel.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), radnaMjestaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idRadnoMjesto"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(radnaMjestaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        radnaMjestaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new RadnoMjestoDto()));
        radnaMjestaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        radnaMjestaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        radnaMjestaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        radnaMjestaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        radnaMjestaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(radnoMjestoPage, pageIndex, () -> refreshPageableTableView(true)));
        radnaMjestaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(radnoMjestoPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(RadnoMjestoDto radnoMjestoDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(radnoMjestoDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(radnoMjestoDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(radnoMjestoDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), radnaMjestaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            radnoMjestoPage = !enableSearchAndSort ? radnoMjestoService.findAll(queryCriteriaDto.getPageable()) : radnoMjestoService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(radnaMjestaPageableTableView, radnoMjestoPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja radnih mjesta.");
    }

    private void onDodajUrediClick(RadnoMjestoDto radnoMjestoDto) {
        DodajUrediRadnoMjestoController dodajUrediRadnoMjestoController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_RADNO_MJESTO, radnoMjestoDto, AppUtils.getCssPathables());
        if (dodajUrediRadnoMjestoController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(RadnoMjestoDto radnoMjestoDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji radnog mjesta", radnoMjestoDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(RadnoMjestoDto radnoMjestoDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da žeilte izbrisati ovo radno mjesto?", radnoMjestoDto, propertiesInfo, () -> {
            radnoMjestoService.delete(radnoMjestoDto.getIdRadnoMjesto());
            refreshPageableTableView(true);
        }, "Radno mjesto je uspješno izbrisano.", "Desila se greška prilikom brisanja radnog mjesta.", LanguageFormat.EN);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.RADNO_MJESTO, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Radna_Mjesta", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("RADNA MJESTA", radnoMjestoService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis radnih mjesta je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa radnih mjesta u PDF.");
    }
}
