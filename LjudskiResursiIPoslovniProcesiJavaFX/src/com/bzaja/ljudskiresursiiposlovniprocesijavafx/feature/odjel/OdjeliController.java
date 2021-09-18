/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.odjel;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel.OdjelDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel.OdjelService;
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
public class OdjeliController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<OdjelDto> odjeliPageableTableView;

    @Autowired
    private OdjelService odjelService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<OdjelDto> odjelPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Odjel", "Naziv");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idOdjel", "naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), odjeliPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idOdjel"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(odjeliPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        odjeliPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new OdjelDto()));
        odjeliPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        odjeliPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        odjeliPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        odjeliPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        odjeliPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(odjelPage, pageIndex, () -> refreshPageableTableView(true)));
        odjeliPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(odjelPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(OdjelDto odjelDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(odjelDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(odjelDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(odjelDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja odjela.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), odjeliPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            odjelPage = !enableSearchAndSort ? odjelService.findAll(queryCriteriaDto.getPageable()) : odjelService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(odjeliPageableTableView, odjelPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(OdjelDto odjelDto) {
        DodajUrediOdjelController dodajUrediOdjelController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_ODJEL, odjelDto);
        if (dodajUrediOdjelController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(OdjelDto odjelDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji odjela", odjelDto, propertiesInfo);
    }

    private void onIzbrisiClick(OdjelDto odjelDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj odjel?", odjelDto, propertiesInfo, "Odjel je uspješno izbrisan.", "Desila se greška prilikom brisanja odjela.", () -> {
            odjelService.delete(odjelDto.getIdOdjel());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Odjel", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Odjeli"), f -> () -> GenericBeanReportPdf.create("ODJELI", odjelService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
