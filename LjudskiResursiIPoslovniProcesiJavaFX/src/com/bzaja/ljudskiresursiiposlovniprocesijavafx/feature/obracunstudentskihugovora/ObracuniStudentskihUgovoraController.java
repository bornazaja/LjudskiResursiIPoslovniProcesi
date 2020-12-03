/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunstudentskihugovora;

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
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.ObracunStudentskihUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.ObracunStudentskihUgovoraService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraPdfReportFactory;
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
        TableUtils.addColumns(obracuniStudentskihUgovoraPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        obracuniStudentskihUgovoraPageableTableView.addMenuItem("Dodaj", (e) -> onDodajClick());
        obracuniStudentskihUgovoraPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        obracuniStudentskihUgovoraPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        obracuniStudentskihUgovoraPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        obracuniStudentskihUgovoraPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        obracuniStudentskihUgovoraPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(obracunStudentskihUgovoraPage, pageIndex, () -> refreshPageableTableView(true)));
        obracuniStudentskihUgovoraPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(obracunStudentskihUgovoraPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(obracunStudentskihUgovoraDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(obracunStudentskihUgovoraDto))
                .addMenuItem("Eksportiraj cijeli obračun u PDF", () -> onEksportirajCijeliObracunUPdf(obracunStudentskihUgovoraDto))
                .addMenuItem("Isplatne liste", () -> paneManager.switchPanes((Pane) NodeUtils.getTabPaneFromInsideNode(root).getParent(), FxmlView.ISPLATNE_LISTE, obracunStudentskihUgovoraDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), obracuniStudentskihUgovoraPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            obracunStudentskihUgovoraPage = !enableSearchAndSort ? obracunStudentskihUgovoraService.findAll(queryCriteriaDto.getPageable()) : obracunStudentskihUgovoraService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(obracuniStudentskihUgovoraPageableTableView, obracunStudentskihUgovoraPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja obračuna studentskih ugovora.");
    }

    private void onDodajClick() {
        DodajObracunStudentskihUgovoraController dodajObracunStudentskihUgovoraController = stageManager.showSecondaryStage(FxmlView.DODAJ_OBRACUN_STUDENTSKIH_UGOVORA, AppUtils.getCssPathables());
        if (dodajObracunStudentskihUgovoraController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji obračuna studentskih ugovora", obracunStudentskihUgovoraDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj obračun studentskih ugovora?", obracunStudentskihUgovoraDto, propertiesInfo, () -> {
            obracunStudentskihUgovoraService.delete(obracunStudentskihUgovoraDto.getIdObracunUgovora());
            refreshPageableTableView(true);
        }, "Obračun studentskih ugovora je uspješno izbrisan.", "Desila se greška prilikom brisanja obračuna studentskih ugovora.", LanguageFormat.HR);
    }

    private void onEksportirajCijeliObracunUPdf(ObracunStudentskihUgovoraDto obracunStudentskihUgovoraDto) {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Obracun_Studentskih_Ugovora", LocalDateTimePattern.HR, obracunStudentskihUgovoraDto.getOpis()),
                f -> () -> obracunUgovoraPdfReportFactory.get(VrsteObracuna.OBRACUN_STUDENTSKIH_UGOVORA).createByIdObracunUgovora(obracunStudentskihUgovoraDto.getIdObracunUgovora(), f.getAbsolutePath()),
                "*.pdf",
                f -> String.format("Cijeli obračun je uspješno ekaportiranj u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja cijelog obračuna u PDF.");
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.OBRACUN_STUDENTSKIH_UGOVORA, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Obracuni_Studentskih_Ugovora", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("OBRAČUNI STUEDNTSKIH UGOVORA", obracunStudentskihUgovoraService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis obračuna studentskih ugovora je uspješno eksportian u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa obračuna studentskih ugovora u PDF.");
    }
}
