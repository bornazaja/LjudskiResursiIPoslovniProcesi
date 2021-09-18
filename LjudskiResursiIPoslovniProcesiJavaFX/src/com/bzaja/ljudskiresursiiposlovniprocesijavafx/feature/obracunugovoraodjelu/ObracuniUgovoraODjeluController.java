/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunugovoraodjelu;

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
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.ObracunUgovoraODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.ObracunUgovoraODjeluService;
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
public class ObracuniUgovoraODjeluController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<ObracunUgovoraODjeluDto> obracuniUgovoraODjeluPageableTableView;

    @Autowired
    private ObracunUgovoraODjeluService obracunUgovoraODjeluService;

    @Autowired
    private ObracunUgovoraPdfReportFactory obracunUgovoraPdfReportFactory;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<ObracunUgovoraODjeluDto> obracunUgovoraODjeluPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Obračun ugovora", "Vrsta obračuna", "Opis", "Datum obračuna", "Valuta");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idObracunUgovora", "vrstaObracuna.naziv", "opis", "datumObracuna", "valuta.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), obracuniUgovoraODjeluPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idObracunUgovora"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(obracuniUgovoraODjeluPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        obracuniUgovoraODjeluPageableTableView.addMenuItem("Dodaj", (e) -> onDodajClick());
        obracuniUgovoraODjeluPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        obracuniUgovoraODjeluPageableTableView.addMenuItem("Pretraživanje i sortrianje", (e) -> onPretrazivanjeISortiranjeClick());
        obracuniUgovoraODjeluPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        obracuniUgovoraODjeluPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        obracuniUgovoraODjeluPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(obracunUgovoraODjeluPage, pageIndex, () -> refreshPageableTableView(true)));
        obracuniUgovoraODjeluPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(obracunUgovoraODjeluPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(obracunUgovoraODjeluDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(obracunUgovoraODjeluDto))
                .addMenuItem("Eksportiraj cijeli obračun u PDF", () -> onEksportirajCijeliObracunUPdfClick(obracunUgovoraODjeluDto))
                .addMenuItem("Isplatne liste", () -> paneManager.switchPanes((Pane) NodeUtils.getTabPaneFromInsideNode(root).getParent(), FxmlView.ISPLATNE_LISTE, obracunUgovoraODjeluDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja obračuna ugovora o djelu.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), obracuniUgovoraODjeluPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            obracunUgovoraODjeluPage = !enableSearchAndSort ? obracunUgovoraODjeluService.findAll(queryCriteriaDto.getPageable()) : obracunUgovoraODjeluService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(obracuniUgovoraODjeluPageableTableView, obracunUgovoraODjeluPage, pageIndex.get()));
        });
    }

    private void onDodajClick() {
        DodajObracunUgovoraODjeluController dodajObracunUgovoraODjeluController = stageManager.showSecondaryStage(FxmlView.DODAJ_OBRACUN_UGOVORA_O_DJELU);
        if (dodajObracunUgovoraODjeluController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji obračuna ugovora o djelu", obracunUgovoraODjeluDto, propertiesInfo);
    }

    private void onIzbrisiClick(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj obračun ugovora o djelu.", obracunUgovoraODjeluDto, propertiesInfo, "Obračun ugovora o djelu je uspješno izbrisan.", "Desila se greška prilikom brisanja obračuna ugovora o djelu.", () -> {
            obracunUgovoraODjeluService.delete(obracunUgovoraODjeluDto.getIdObracunUgovora());
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajCijeliObracunUPdfClick(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto) {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Obracun_Ugovora_O_Djelu", obracunUgovoraODjeluDto.getOpis()), f -> () -> obracunUgovoraPdfReportFactory.get(VrsteObracuna.OBRACUN_UGOVORA_O_DJELU).createByIdObracunUgovora(obracunUgovoraODjeluDto.getIdObracunUgovora(), f.getAbsolutePath()), "*.pdf");
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("ObracunUgovoraODjelu", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Obracuni_Ugovora_O_Djelu"), f -> () -> GenericBeanReportPdf.create("OBRAČUNI UGOVORA O DJELU", obracunUgovoraODjeluService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
