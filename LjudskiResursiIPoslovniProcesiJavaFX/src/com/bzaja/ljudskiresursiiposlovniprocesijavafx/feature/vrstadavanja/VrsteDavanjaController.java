/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstadavanja;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja.VrstaDavanjaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja.VrstaDavanjaService;
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
public class VrsteDavanjaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<VrstaDavanjaDto> vrsteDavanjaPageableTableView;

    @Autowired
    private VrstaDavanjaService vrstaDavanjaService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<VrstaDavanjaDto> vrstaDavanjaPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Vrsta davanja", "Naziv", "Stopa na plaću", "Stopa iz plaće", "Vrijedi do");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idVrstaDavanja", "naziv", "stopaNaPlacu", "stopaIzPlace", "vrijediDo");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), vrsteDavanjaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idVrstaDavanja"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(vrsteDavanjaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        vrsteDavanjaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new VrstaDavanjaDto()));
        vrsteDavanjaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(true));
        vrsteDavanjaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        vrsteDavanjaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        vrsteDavanjaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        vrsteDavanjaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(vrstaDavanjaPage, pageIndex, () -> refreshPageableTableView(true)));
        vrsteDavanjaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(vrstaDavanjaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(VrstaDavanjaDto vrstaDavanjaDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(vrstaDavanjaDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(vrstaDavanjaDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(vrstaDavanjaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), vrsteDavanjaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            vrstaDavanjaPage = !enableSearchAndSort ? vrstaDavanjaService.findAll(queryCriteriaDto.getPageable()) : vrstaDavanjaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(vrsteDavanjaPageableTableView, vrstaDavanjaPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja vrsta davanja.");
    }

    private void onDodajUrediClick(VrstaDavanjaDto vrstaDavanjaDto) {
        DodajUrediVrstuDavanjaController dodajUrediVrstuDavanjaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_VRSTU_DAVANJA, vrstaDavanjaDto, AppUtils.getCssPathables());
        if (dodajUrediVrstuDavanjaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(VrstaDavanjaDto vrstaDavanjaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji vrste davanja", vrstaDavanjaDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(VrstaDavanjaDto vrstaDavanjaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da žeilte izbrisati ovu vrstu davanja?", vrstaDavanjaDto, propertiesInfo, () -> {
            vrstaDavanjaService.delete(vrstaDavanjaDto.getIdVrstaDavanja());
            refreshPageableTableView(true);
        }, "Vrsta davanja je uspješno izbrisana.", "Desila se greška prilikom brisanja vrste davanja.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.VRSTA_DAVANJA, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Vrste_Davanja", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("VRSTE DAVANAJA", vrstaDavanjaService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis vrsta davanaja je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa vrste davanja u PDF.");
    }
}
