/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunugovoraodjelu;

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
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.ObracunUgovoraODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.ObracunUgovoraODjeluService;
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

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Obra??un ugovora", "Vrsta obra??una", "Opis", "Datum obra??una", "Valuta");
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
        TableUtils.addColumns(obracuniUgovoraODjeluPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        obracuniUgovoraODjeluPageableTableView.addMenuItem("Dodaj", (e) -> onDodajClick());
        obracuniUgovoraODjeluPageableTableView.addMenuItem("Osvje??i", (e) -> refreshPageableTableView(false));
        obracuniUgovoraODjeluPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        obracuniUgovoraODjeluPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        obracuniUgovoraODjeluPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        obracuniUgovoraODjeluPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(obracunUgovoraODjeluPage, pageIndex, () -> refreshPageableTableView(true)));
        obracuniUgovoraODjeluPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(obracunUgovoraODjeluPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(obracunUgovoraODjeluDto))
                .addMenuItem("Izbri??i", () -> onIzbrisiClick(obracunUgovoraODjeluDto))
                .addMenuItem("Eksportiraj cijeli obra??un u PDF", () -> onEksportirajCijeliObracunUPdfClick(obracunUgovoraODjeluDto))
                .addMenuItem("Isplatne liste", () -> paneManager.switchPanes((Pane) NodeUtils.getTabPaneFromInsideNode(root).getParent(), FxmlView.ISPLATNE_LISTE, obracunUgovoraODjeluDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), obracuniUgovoraODjeluPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            obracunUgovoraODjeluPage = !enableSearchAndSort ? obracunUgovoraODjeluService.findAll(queryCriteriaDto.getPageable()) : obracunUgovoraODjeluService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(obracuniUgovoraODjeluPageableTableView, obracunUgovoraODjeluPage, pageIndex.get()));
        }, "Desila se gre??ka prilikom dohva??anja obra??una ugovora o djelu.");
    }

    private void onDodajClick() {
        DodajObracunUgovoraODjeluController dodajObracunUgovoraODjeluController = stageManager.showSecondaryStage(FxmlView.DODAJ_OBRACUN_UGOVORA_O_DJELU, AppUtils.getCssPathables());
        if (dodajObracunUgovoraODjeluController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji obra??una ugovora o djelu", obracunUgovoraODjeluDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da ??elite izbrisati ovaj obra??un ugovora o djelu.", obracunUgovoraODjeluDto, propertiesInfo, () -> {
            obracunUgovoraODjeluService.delete(obracunUgovoraODjeluDto.getIdObracunUgovora());
            refreshPageableTableView(true);
        }, "Obra??un ugovora o djelu je uspje??no izbrisan.", "Desila se gre??ka prilikom brisanja obra??una ugovora o djelu.", LanguageFormat.HR);
    }

    private void onEksportirajCijeliObracunUPdfClick(ObracunUgovoraODjeluDto obracunUgovoraODjeluDto) {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Obracun_Ugovora_O_Djelu", LocalDateTimePattern.HR, obracunUgovoraODjeluDto.getOpis()),
                f -> () -> obracunUgovoraPdfReportFactory.get(VrsteObracuna.OBRACUN_UGOVORA_O_DJELU).createByIdObracunUgovora(obracunUgovoraODjeluDto.getIdObracunUgovora(), f.getAbsolutePath()),
                "*.pdf",
                f -> String.format("Cijeli obra??un ugovora o djelu je uspje??no eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se gre??ka prilikom eksportiranja cijelog obra??una ugovora o djelu u PDF.");
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.OBRACUN_UGOVORA_O_DJELU, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Obracuni_Ugovora_O_Djelu", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("OBRA??UNI UGOVORA O DJELU", obracunUgovoraODjeluService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis obra??una ugovora o djelu je uspje??no eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se gre??ka prilikom eksportiranja popisa obra??una ugovora o djelu u PDF.");
    }
}
