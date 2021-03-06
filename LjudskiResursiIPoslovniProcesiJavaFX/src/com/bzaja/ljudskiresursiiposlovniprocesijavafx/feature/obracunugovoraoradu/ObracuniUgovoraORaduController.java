/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.PaneManager;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraPdfReportFactory;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.rekapitulacija.RekapitulacijaObracunaUgovoraORaduPdfReport;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
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
import javafx.scene.layout.Pane;
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
public class ObracuniUgovoraORaduController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<ObracunUgovoraORaduDto> obracuniUgovoraORaduPageableTableView;

    @Autowired
    private ObracunUgovoraORaduService obracunUgovoraORaduService;

    @Autowired
    private ObracunUgovoraPdfReportFactory obracunUgovoraPdfReportFactory;

    @Autowired
    private RekapitulacijaObracunaUgovoraORaduPdfReport rekapitulacijaObracunaUgovoraORaduPdfReport;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<ObracunUgovoraORaduDto> obracunUgovoraORaduPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Obra??un ugovora", "Vrsta obra??una", "Opis", "Datum obra??una", "Valuta", "Datum od", "Datum do", "Osnovni osobni odbitak", "Osnovica osobnog odbitka");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idObracunUgovora", "vrstaObracuna.naziv", "opis", "datumObracuna", "valuta.naziv", "datumOd", "datumDo", "osnovniOsobniOdbitak", "osnovicaOsobnogOdbitka");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), obracuniUgovoraORaduPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idObracunUgovora"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(obracuniUgovoraORaduPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        obracuniUgovoraORaduPageableTableView.addMenuItem("Dodaj", (e) -> onDodajClick());
        obracuniUgovoraORaduPageableTableView.addMenuItem("Osvje??i", (e) -> refreshPageableTableView(false));
        obracuniUgovoraORaduPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        obracuniUgovoraORaduPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        obracuniUgovoraORaduPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        obracuniUgovoraORaduPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(obracunUgovoraORaduPage, pageIndex, () -> refreshPageableTableView(true)));
        obracuniUgovoraORaduPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(obracunUgovoraORaduPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(obracunUgovoraORaduDto))
                .addMenuItem("Izbri??i", () -> onIzbrisiClick(obracunUgovoraORaduDto))
                .addMenuItem("Eksportiraj cijeli obra??un u PDF", () -> onEksportirajCijeliObracunUPdfClick(obracunUgovoraORaduDto))
                .addMenuItem("Eksportiraj rekapitulaciju u PDF", () -> onEksportirajRekapitulacijuUPdfClick(obracunUgovoraORaduDto))
                .addMenuItem("Isplatne liste", () -> paneManager.switchPanes((Pane) NodeUtils.getTabPaneFromInsideNode(root).getParent(), FxmlView.ISPLATNE_LISTE, obracunUgovoraORaduDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), obracuniUgovoraORaduPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            obracunUgovoraORaduPage = !enableSearchAndSort ? obracunUgovoraORaduService.findAll(queryCriteriaDto.getPageable()) : obracunUgovoraORaduService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(obracuniUgovoraORaduPageableTableView, obracunUgovoraORaduPage, pageIndex.get()));
        }, "Desila se gre??ka prilikom dohva??anja obra??una ugovora o radu.");
    }

    private void onDodajClick() {
        DodajObracunUgovoraORaduController dodajObracunUgovoraORaduController = stageManager.showSecondaryStage(FxmlView.DODAJ_OBRACUN_UGOVORA_O_RADU, AppUtils.getCssPathables());
        if (dodajObracunUgovoraORaduController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji obra??una ugovora o radu", obracunUgovoraORaduDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da ??elite izbrisati ovaj obra??un ugovora o radu?", obracunUgovoraORaduDto, propertiesInfo, () -> {
            obracunUgovoraORaduService.delete(obracunUgovoraORaduDto.getIdObracunUgovora());
            refreshPageableTableView(true);
        }, "Obra??un ugovora o radu je uspje??no izbrisan.", "Desila se gre??ka prilikom brisanja obra??una ugovora o radu.", LanguageFormat.HR);
    }

    private void onEksportirajCijeliObracunUPdfClick(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        String nazivDatoteke = String.format("Obracun_Ugovora_O_Radu_Za_Period_Od_%s_Do_%s", obracunUgovoraORaduDto.getDatumOd().toString(), obracunUgovoraORaduDto.getDatumDo().toString());
        FileChooserUtils.showSaveDialog(root, nazivDatoteke,
                f -> () -> obracunUgovoraPdfReportFactory.get(VrsteObracuna.OBRACUN_UGOVORA_O_RADU).createByIdObracunUgovora(obracunUgovoraORaduDto.getIdObracunUgovora(), f.getAbsolutePath()),
                "*.pdf",
                f -> String.format("Cijeli obra??un ugovora o radu je uspje??no eksportian u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se gre??ka prilikom eksportiranja obra??una ugovora o radu u PDF.");
    }

    private void onEksportirajRekapitulacijuUPdfClick(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        String nazivDatoteke = String.format("Rekapitulacija_Za_Period_Od_%s_Do_%s", obracunUgovoraORaduDto.getDatumOd().toString(), obracunUgovoraORaduDto.getDatumDo().toString());
        FileChooserUtils.showSaveDialog(root, nazivDatoteke,
                f -> () -> rekapitulacijaObracunaUgovoraORaduPdfReport.create(obracunUgovoraORaduDto.getIdObracunUgovora(), f.getAbsolutePath()),
                "*.pdf",
                f -> String.format("Rekapitulacija je uspje??no eksportirana u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se gre??ka prilikom eksportiranja rekapitulacije u PDF.");
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.OBRACUN_UGOVORA_O_RADU, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Obracuni_Ugovora_O_Radu", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("OBRA??UNI UGOVORA_O RADU", obracunUgovoraORaduService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis obra??una ugovora o radu je uspje??no eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se gre??ka prilikom eksportiranja popisa u obra??una uogovra o radu u PDF.");
    }
}
