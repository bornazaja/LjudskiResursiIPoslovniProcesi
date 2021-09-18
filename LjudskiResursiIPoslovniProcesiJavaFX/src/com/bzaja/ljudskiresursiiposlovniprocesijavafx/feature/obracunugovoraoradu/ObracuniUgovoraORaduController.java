/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.PaneManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraPdfReportFactory;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.rekapitulacija.RekapitulacijaObracunaUgovoraORaduPdfReport;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.GenericBeanReportPdf;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.StringUtils;
import com.bzaja.myjavafxlibrary.control.PageableTableView;
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
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
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

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Obračun ugovora", "Vrsta obračuna", "Opis", "Datum obračuna", "Valuta", "Datum od", "Datum do", "Osnovni osobni odbitak", "Osnovica osobnog odbitka");
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
        TableUtils.addColumns(obracuniUgovoraORaduPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        obracuniUgovoraORaduPageableTableView.addMenuItem("Dodaj", (e) -> onDodajClick());
        obracuniUgovoraORaduPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        obracuniUgovoraORaduPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        obracuniUgovoraORaduPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        obracuniUgovoraORaduPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        obracuniUgovoraORaduPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(obracunUgovoraORaduPage, pageIndex, () -> refreshPageableTableView(true)));
        obracuniUgovoraORaduPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(obracunUgovoraORaduPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(obracunUgovoraORaduDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(obracunUgovoraORaduDto))
                .addMenuItem("Eksportiraj cijeli obračun u PDF", () -> onEksportirajCijeliObracunUPdfClick(obracunUgovoraORaduDto))
                .addMenuItem("Eksportiraj rekapitulaciju u PDF", () -> onEksportirajRekapitulacijuUPdfClick(obracunUgovoraORaduDto))
                .addMenuItem("Isplatne liste", () -> paneManager.switchPanes((Pane) NodeUtils.getTabPaneFromInsideNode(root).getParent(), FxmlView.ISPLATNE_LISTE, obracunUgovoraORaduDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja obračuna ugovora o radu.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), obracuniUgovoraORaduPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            obracunUgovoraORaduPage = !enableSearchAndSort ? obracunUgovoraORaduService.findAll(queryCriteriaDto.getPageable()) : obracunUgovoraORaduService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(obracuniUgovoraORaduPageableTableView, obracunUgovoraORaduPage, pageIndex.get()));
        });
    }

    private void onDodajClick() {
        DodajObracunUgovoraORaduController dodajObracunUgovoraORaduController = stageManager.showSecondaryStage(FxmlView.DODAJ_OBRACUN_UGOVORA_O_RADU);
        if (dodajObracunUgovoraORaduController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji obračuna ugovora o radu", obracunUgovoraORaduDto, propertiesInfo);
    }

    private void onIzbrisiClick(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj obračun ugovora o radu?", obracunUgovoraORaduDto, propertiesInfo, "Obračun ugovora o radu je uspješno izbrisan.", "Desila se greška prilikom brisanja obračuna ugovora o radu.", () -> {
            obracunUgovoraORaduService.delete(obracunUgovoraORaduDto.getIdObracunUgovora());
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajCijeliObracunUPdfClick(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        String nazivDatoteke = String.format("Obracun_Ugovora_O_Radu_Za_Period_Od_%s_Do_%s", obracunUgovoraORaduDto.getDatumOd().toString(), obracunUgovoraORaduDto.getDatumDo().toString());
        FileChooserUtils.showSaveDialog(root, nazivDatoteke, f -> () -> obracunUgovoraPdfReportFactory.get(VrsteObracuna.OBRACUN_UGOVORA_O_RADU).createByIdObracunUgovora(obracunUgovoraORaduDto.getIdObracunUgovora(), f.getAbsolutePath()), "*.pdf");
    }

    private void onEksportirajRekapitulacijuUPdfClick(ObracunUgovoraORaduDto obracunUgovoraORaduDto) {
        String nazivDatoteke = String.format("Rekapitulacija_Za_Period_Od_%s_Do_%s", obracunUgovoraORaduDto.getDatumOd().toString(), obracunUgovoraORaduDto.getDatumDo().toString());
        FileChooserUtils.showSaveDialog(root, nazivDatoteke, f -> () -> rekapitulacijaObracunaUgovoraORaduPdfReport.create(obracunUgovoraORaduDto.getIdObracunUgovora(), f.getAbsolutePath()), "*.pdf");
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("ObracunUgovoraORadu", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Obracuni_Ugovora_O_Radu"), f -> () -> GenericBeanReportPdf.create("OBRAČUNI UGOVORA_O RADU", obracunUgovoraORaduService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
