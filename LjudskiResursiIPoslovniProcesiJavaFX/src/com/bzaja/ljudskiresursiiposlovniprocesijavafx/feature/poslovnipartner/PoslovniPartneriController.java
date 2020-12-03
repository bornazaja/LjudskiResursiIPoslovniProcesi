/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.poslovnipartner;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerService;
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
public class PoslovniPartneriController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<PoslovniPartnerDto> poslovniPartneriPageableTableView;

    @Autowired
    private PoslovniPartnerService poslovniPartnerService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<PoslovniPartnerDto> poslovniPartnerPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Poslovni partner", "Naziv", "OIB", "Email", "Broj telefona", "Ulica", "Grad");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idPoslovniPartner", "naziv", "oib", "email", "brojTelefona", "ulica", "grad.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), poslovniPartneriPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idPoslovniPartner"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(poslovniPartneriPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        poslovniPartneriPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new PoslovniPartnerDto()));
        poslovniPartneriPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        poslovniPartneriPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        poslovniPartneriPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        poslovniPartneriPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        poslovniPartneriPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(poslovniPartnerPage, pageIndex, () -> refreshPageableTableView(true)));
        poslovniPartneriPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(poslovniPartnerPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(PoslovniPartnerDto poslovniPartnerDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(poslovniPartnerDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(poslovniPartnerDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(poslovniPartnerDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), poslovniPartneriPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            poslovniPartnerPage = !enableSearchAndSort ? poslovniPartnerService.findAll(queryCriteriaDto.getPageable()) : poslovniPartnerService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(poslovniPartneriPageableTableView, poslovniPartnerPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja poslovnih partnera.");
    }

    private void onDodajUrediClick(PoslovniPartnerDto poslovniPartnerDto) {
        DodajUrediPoslovnogPartneraController dodajUrediPoslovnogPartneraController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_POSLOVNOG_PARTNERA, poslovniPartnerDto, AppUtils.getCssPathables());
        if (dodajUrediPoslovnogPartneraController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(PoslovniPartnerDto poslovniPartnerDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji poslovnog partnera", poslovniPartnerDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(PoslovniPartnerDto poslovniPartnerDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovog poslovnog partnera?", poslovniPartnerDto, propertiesInfo, () -> {
            poslovniPartnerService.delete(poslovniPartnerDto.getIdPoslovniPartner());
            refreshPageableTableView(true);
        }, "Poslovni partner je uspješno izbrisan.", "Desila se greška prilikom brisanja poslovnog partnera.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.POSLOVNI_PARTNER, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Poslovni_Partneri", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("POSLOVNI PARTNERI", poslovniPartnerService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis poslovnih partnera je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa poslovnih partnera u PDF.");
    }
}
