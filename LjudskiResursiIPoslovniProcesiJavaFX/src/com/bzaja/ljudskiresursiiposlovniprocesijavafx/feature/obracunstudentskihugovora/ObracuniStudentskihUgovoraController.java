/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.PaneManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.ObracunStudentskihUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.ObracunStudentskihUgovoraService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraPdfReportFactory;
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
public class ObracuniStudentskihUgovoraController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<ObracunStudentskihUgovoraDto> obracuniStudentskihUgovoraPageableTableView;

    @Autowired
    private ObracunStudentskihUgovoraService obracunStudentskihUgovoraService;

    @Autowired
    private ObracunUgovoraPdfReportFactory obracunUgovoraPdfReportFactory;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<ObracunStudentskihUgovoraDto> obracunStudentskihUgovoraPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Obračun ugovora", "Vrsta obračuna", "Opis", "Datum obračuna", "Valuta", "Limit godišnjeg iznosa za studenta");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idObracunUgovora", "vrstaObracuna.naziv", "opis", "datumObracuna", "valuta.naziv", "limitGodisnjegIznosaZaStudenta");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), obracuniStudentskihUgovoraPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idObracunUgovora"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(obracuniStudentskihUgovoraPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        obracuniStudentskihUgovoraPageableTableView.addMenuItem("Dodaj", (e) -> onDodajClick());
        obracuniStudentskihUgovoraPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        obracuniStudentskihUgovoraPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        obracuniStudentskihUgovoraPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        obracuniStudentskihUgovoraPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        obracuniStudentskihUgovoraPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(obracunStudentskihUgovoraPage, pageIndex, () -> refreshPageableTableView(true)));
        obracuniStudentskihUgovoraPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(obracunStudentskihUgovoraPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(obracunStudentskihUgovoraDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(obracunStudentskihUgovoraDto))
                .addMenuItem("Eksportiraj cijeli obračun u PDF", () -> onEksportirajCijeliObracunUPdf(obracunStudentskihUgovoraDto))
                .addMenuItem("Isplatne liste", () -> paneManager.switchPanes((Pane) NodeUtils.getTabPaneFromInsideNode(root).getParent(), FxmlView.ISPLATNE_LISTE, obracunStudentskihUgovoraDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja obračuna studentskih ugovora.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), obracuniStudentskihUgovoraPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            obracunStudentskihUgovoraPage = !enableSearchAndSort ? obracunStudentskihUgovoraService.findAll(queryCriteriaDto.getPageable()) : obracunStudentskihUgovoraService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(obracuniStudentskihUgovoraPageableTableView, obracunStudentskihUgovoraPage, pageIndex.get()));
        });
    }

    private void onDodajClick() {
        DodajObracunStudentskihUgovoraController dodajObracunStudentskihUgovoraController = stageManager.showSecondaryStage(FxmlView.DODAJ_OBRACUN_STUDENTSKIH_UGOVORA);
        if (dodajObracunStudentskihUgovoraController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji obračuna studentskih ugovora", obracunStudentskihUgovoraDto, propertiesInfo);
    }

    private void onIzbrisiClick(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj obračun studentskih ugovora?", obracunStudentskihUgovoraDto, propertiesInfo, "Obračun studentskih ugovora je uspješno izbrisan.", "Desila se greška prilikom brisanja obračuna studentskih ugovora.", () -> {
            obracunStudentskihUgovoraService.delete(obracunStudentskihUgovoraDto.getIdObracunUgovora());
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajCijeliObracunUPdf(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto) {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Obracun_Studentskih_Ugovora", obracunStudentskihUgovoraDto.getOpis()), f -> () -> obracunUgovoraPdfReportFactory.get(VrsteObracuna.OBRACUN_STUDENTSKIH_UGOVORA).createByIdObracunUgovora(obracunStudentskihUgovoraDto.getIdObracunUgovora(), f.getAbsolutePath()), "*.pdf");
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("ObracunStudentskihUgovora", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Obracuni_Studentskih_Ugovora"), f -> () -> GenericBeanReportPdf.create("OBRAČUNI STUEDNTSKIH UGOVORA", obracunStudentskihUgovoraService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
