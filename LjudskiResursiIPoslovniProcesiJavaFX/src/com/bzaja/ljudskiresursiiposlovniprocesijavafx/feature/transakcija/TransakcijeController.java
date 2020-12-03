/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanClass;
import com.bzaja.myjavalibrary.springframework.data.query.Operator;
import com.bzaja.myjavalibrary.springframework.data.query.QueryCriteriaDto;
import com.bzaja.myjavalibrary.util.PropertyInfoDto;
import com.bzaja.myjavalibrary.util.PropertyInfoUtils;
import com.bzaja.myjavafxlibrary.control.PageableTableView;
import com.bzaja.myjavalibrary.util.LanguageFormat;
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
public class TransakcijeController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<TransakcijaDto> transakcijePageableTableView;

    @Autowired
    private TransakcijaService transakcijaService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<TransakcijaDto> transakcijaPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Transakcija", "Poslovni partner", "Opis", "Iznos", "Valuta", "Srednji tecaj", "Vrsta transakcije", "Kategorija transakcije", "Datum transakcije");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idTransakcija", "poslovniPartner.naziv", "opis", "iznos", "valuta.naziv", "srednjiTecaj", "vrstaTransakcije.naziv", "kategorijaTransakcije.naziv", "datumTransakcije");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), transakcijePageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idTransakcija"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(transakcijePageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        transakcijePageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new TransakcijaDto()));
        transakcijePageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        transakcijePageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        transakcijePageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        transakcijePageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        transakcijePageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(transakcijaPage, pageIndex, () -> refreshPageableTableView(true)));
        transakcijePageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(transakcijaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(TransakcijaDto transakcijaDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(transakcijaDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(transakcijaDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(transakcijaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), transakcijePageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            transakcijaPage = !enableSearchAndSort ? transakcijaService.findAll(queryCriteriaDto.getPageable()) : transakcijaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(transakcijePageableTableView, transakcijaPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja transakcija.");
    }

    private void onDodajUrediClick(TransakcijaDto transakcijaDto) {
        DodajUrediTransakcijuController dodajUrediTransakcijuController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_TRANSKACIJU, transakcijaDto, AppUtils.getCssPathables());
        if (dodajUrediTransakcijuController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(TransakcijaDto transakcijaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji transakcije", transakcijaDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(TransakcijaDto transakcijaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da žeite izbrisati ovu transakciju?", transakcijaDto, propertiesInfo, () -> {
            transakcijaService.delete(transakcijaDto.getIdTransakcija());
            refreshPageableTableView(true);
        }, "Transakcija je uspješno izbrisana.", "Desila se greška prilikom brisanja transakcije.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.TRANSAKCIJA, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        stageManager.showSecondaryStage(FxmlView.EKSPORTIRAJ_TRANSAKCIJE_U_PDF, propertiesInfo, AppUtils.getCssPathables());
    }
}
