/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoUtils;
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
        TableUtils.addColumns(transakcijePageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        transakcijePageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new TransakcijaDto()));
        transakcijePageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        transakcijePageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        transakcijePageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        transakcijePageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        transakcijePageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(transakcijaPage, pageIndex, () -> refreshPageableTableView(true)));
        transakcijePageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(transakcijaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(TransakcijaDto transakcijaDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(transakcijaDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(transakcijaDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(transakcijaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja transakcija", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), transakcijePageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            transakcijaPage = !enableSearchAndSort ? transakcijaService.findAll(queryCriteriaDto.getPageable()) : transakcijaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(transakcijePageableTableView, transakcijaPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(TransakcijaDto transakcijaDto) {
        DodajUrediTransakcijuController dodajUrediTransakcijuController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_TRANSKACIJU, transakcijaDto);
        if (dodajUrediTransakcijuController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(TransakcijaDto transakcijaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji transakcije", transakcijaDto, propertiesInfo);
    }

    private void onIzbrisiClick(TransakcijaDto transakcijaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da žeite izbrisati ovu transakciju?", transakcijaDto, propertiesInfo, "Transakcija je uspješno izbrisana.", "Desila se greška prilikom brisanja transakcije.", () -> {
            transakcijaService.delete(transakcijaDto.getIdTransakcija());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Transakcija", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        stageManager.showSecondaryStage(FxmlView.EKSPORTIRAJ_TRANSAKCIJE_U_PDF, propertiesInfo);
    }
}
