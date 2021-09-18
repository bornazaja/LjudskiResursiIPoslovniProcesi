/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.porez;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezService;
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
public class PoreziController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<PorezDto> poreziPageableTableView;

    @Autowired
    private PorezService porezService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<PorezDto> porezPage;

    private static final List<String> DISPLAY_MAMES = Arrays.asList("ID Porez", "Vrsta poreza", "Stopa", "Min. osnovica", "Max. osnovica", "Država");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idPorez", "vrstaPoreza.naziv", "stopa", "minOsnovica", "maxOsnovica", "drzava.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_MAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), poreziPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idPorez"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(poreziPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        poreziPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new PorezDto()));
        poreziPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        poreziPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        poreziPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        poreziPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        poreziPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(porezPage, pageIndex, () -> refreshPageableTableView(true)));
        poreziPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(porezPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(PorezDto porezDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(porezDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(porezDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(porezDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja poreza.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), poreziPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            porezPage = !enableSearchAndSort ? porezService.findAll(queryCriteriaDto.getPageable()) : porezService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(poreziPageableTableView, porezPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(PorezDto porezDto) {
        DodajUrediPorezController dodajUrediPorezController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_POREZ, porezDto);
        if (dodajUrediPorezController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(PorezDto porezDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji poreza", porezDto, propertiesInfo);
    }

    private void onIzbrisiClick(PorezDto porezDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da žeilte izbrisati ovaj porez?", porezDto, propertiesInfo, "Porez je uspješno izbrisan.", "Desila se greška prilikom brisanja poreza.", () -> {
            porezService.delete(porezDto.getIdPorez());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Porez", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Porezi"), f -> () -> GenericBeanReportPdf.create("POREZI", porezService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
