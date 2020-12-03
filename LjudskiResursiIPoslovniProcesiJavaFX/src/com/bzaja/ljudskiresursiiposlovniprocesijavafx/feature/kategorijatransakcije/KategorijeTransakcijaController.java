/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.kategorijatransakcije;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcijeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcijeService;
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
public class KategorijeTransakcijaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<KategorijaTransakcijeDto> kategorijeTransakcijaPageableTableView;

    @Autowired
    private KategorijaTransakcijeService kategorijaTransakcijeService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<KategorijaTransakcijeDto> kategorijaTransakcijaPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Kategorija transakcije", "Naziv");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idKategorijaTransakcije", "naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), kategorijeTransakcijaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idKategorijaTransakcije"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(kategorijeTransakcijaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        kategorijeTransakcijaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new KategorijaTransakcijeDto()));
        kategorijeTransakcijaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        kategorijeTransakcijaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        kategorijeTransakcijaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        kategorijeTransakcijaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        kategorijeTransakcijaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(kategorijaTransakcijaPage, pageIndex, () -> refreshPageableTableView(true)));
        kategorijeTransakcijaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(kategorijaTransakcijaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(KategorijaTransakcijeDto kategorijaTransakcijeDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(kategorijaTransakcijeDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(kategorijaTransakcijeDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(kategorijaTransakcijeDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), kategorijeTransakcijaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            kategorijaTransakcijaPage = !enableSearchAndSort ? kategorijaTransakcijeService.findAll(queryCriteriaDto.getPageable()) : kategorijaTransakcijeService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(kategorijeTransakcijaPageableTableView, kategorijaTransakcijaPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja kategorija transakcija.");
    }

    private void onDodajUrediClick(KategorijaTransakcijeDto kategorijaTransakcijeDto) {
        DodajUrediKategorijuTransakcijeController dodajUrediKategorijuTransakcijeController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_KATEGORIJU_TRANSAKCIJE, kategorijaTransakcijeDto, AppUtils.getCssPathables());
        if (dodajUrediKategorijuTransakcijeController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(KategorijaTransakcijeDto kategorijaTransakcijeDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji kategorije transkacije", kategorijaTransakcijeDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(KategorijaTransakcijeDto kategorijaTransakcijeDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurani da želite izbrisati ovu kategoriju transakcije?", kategorijaTransakcijeDto, propertiesInfo, () -> {
            kategorijaTransakcijeService.delete(kategorijaTransakcijeDto.getIdKategorijaTransakcije());
            refreshPageableTableView(true);
        }, "Kategorija transkcije je uspješno izbrisana.", "Desila se greška prilikom brisanja kategorije transakcije.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.KATEGORIJA_TRANSAKCIJE, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Kategorije_Transakcija", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("KATEGORIJE TRANSAKCIJA", kategorijaTransakcijeService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis kategorija transakcija je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se grška prilikom eksportianja popisa kateogrija transakcija.");
    }
}
