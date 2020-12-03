/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstadodatka;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka.VrstaDodatkaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka.VrstaDodatkaService;
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
public class VrsteDodatakaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<VrstaDodatkaDto> vrsteDodatakaPageableTableView;

    @Autowired
    private VrstaDodatkaService vrstaDodatkaService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<VrstaDodatkaDto> vrstaDodatkaPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Vrsta dodatka", "Naziv");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idVrstaDodatka", "naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), vrsteDodatakaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idVrstaDodatka"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(vrsteDodatakaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        vrsteDodatakaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new VrstaDodatkaDto()));
        vrsteDodatakaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        vrsteDodatakaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        vrsteDodatakaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        vrsteDodatakaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        vrsteDodatakaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(vrstaDodatkaPage, pageIndex, () -> refreshPageableTableView(true)));
        vrsteDodatakaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(vrstaDodatkaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(VrstaDodatkaDto vrstaDodatkaDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(vrstaDodatkaDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(vrstaDodatkaDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(vrstaDodatkaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), vrsteDodatakaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            vrstaDodatkaPage = !enableSearchAndSort ? vrstaDodatkaService.findAll(queryCriteriaDto.getPageable()) : vrstaDodatkaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(vrsteDodatakaPageableTableView, vrstaDodatkaPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja vrsta dodataka.");
    }

    private void onDodajUrediClick(VrstaDodatkaDto vrstaDodatkaDto) {
        DodajUrediVrstuDodatkaController dodajUrediVrstuDodatkaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_VRSTU_DODATKA, vrstaDodatkaDto, AppUtils.getCssPathables());
        if (dodajUrediVrstuDodatkaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(VrstaDodatkaDto vrstaDodatkaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji vrste dodatka", vrstaDodatkaDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(VrstaDodatkaDto vrstaDodatkaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu vrstu dodatka?", vrstaDodatkaDto, propertiesInfo, () -> {
            vrstaDodatkaService.delete(vrstaDodatkaDto.getIdVrstaDodatka());
            refreshPageableTableView(true);
        }, "Vrsta dodatka je uspješno izbrisana.", "Desila se greška prilikom brisanja vrsta dodatka.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.VRSTA_DODATKA, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Vrste_Dodataka", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("VRSTE DODATAKA", vrstaDodatkaService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis vrsta dodataka je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa vrsta davanaja u PDF.");
    }
}
