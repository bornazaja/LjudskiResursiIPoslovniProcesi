/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstaobustave;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave.VrstaObustaveDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave.VrstaObustaveService;
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
public class VrsteObustavaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<VrstaObustaveDto> vrsteObustavaPageableTableView;

    @Autowired
    private VrstaObustaveService vrstaObustaveService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<VrstaObustaveDto> vrstaObustavePage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Vrsta obustave", "Naziv");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idVrstaObustave", "naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), vrsteObustavaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idVrstaObustave"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(vrsteObustavaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        vrsteObustavaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new VrstaObustaveDto()));
        vrsteObustavaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        vrsteObustavaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        vrsteObustavaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        vrsteObustavaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        vrsteObustavaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(vrstaObustavePage, pageIndex, () -> refreshPageableTableView(true)));
        vrsteObustavaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(vrstaObustavePage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(VrstaObustaveDto vrstaObustaveDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(vrstaObustaveDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(vrstaObustaveDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(vrstaObustaveDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), vrsteObustavaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            vrstaObustavePage = !enableSearchAndSort ? vrstaObustaveService.findAll(queryCriteriaDto.getPageable()) : vrstaObustaveService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(vrsteObustavaPageableTableView, vrstaObustavePage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja vrsta obustava.");
    }

    private void onDodajUrediClick(VrstaObustaveDto vrstaObustaveDto) {
        DodajUrediVrstuObustaveController dodajUrediVrstuObustaveController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_VRSTU_OBUSTAVE, vrstaObustaveDto, AppUtils.getCssPathables());
        if (dodajUrediVrstuObustaveController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(VrstaObustaveDto vrstaObustaveDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji vrste obustave", vrstaObustaveDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(VrstaObustaveDto vrstaObustaveDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu vrstu obustave?", vrstaObustaveDto, propertiesInfo, () -> {
            vrstaObustaveService.delete(vrstaObustaveDto.getIdVrstaObustave());
            refreshPageableTableView(true);
        }, "Vrsta obustave je uspješno izbrisana.", "Desila se greška prilkom brisanja vrste obustave.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.VRSTA_OBUSTAVE, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Vrste_Obustava", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("VRSTE OBUSTAVA", vrstaObustaveService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis vrsta obustava je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa vrste obustava u PDF.");
    }
}
