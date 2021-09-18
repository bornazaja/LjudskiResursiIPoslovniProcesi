/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstaprekovremenograda;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda.VrstaPrekovremenogRadaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda.VrstaPrekovremenogRadaService;
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
public class VrstePrekovremenihRadovaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<VrstaPrekovremenogRadaDto> vrstePrekovremenihRadovaPageableTableView;

    @Autowired
    private VrstaPrekovremenogRadaService vrstaPrekovremenogRadaService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<VrstaPrekovremenogRadaDto> vrstaPrekovremenogRadaPage;

    private static final List<String> DIPSPLY_NAMES = Arrays.asList("ID Vrsta prekovremenog rada", "Naziv", "Koeficjent");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idVrstaPrekovremenogRada", "naziv", "koeficjent");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DIPSPLY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), vrstePrekovremenihRadovaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idVrstaPrekovremenogRada"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(vrstePrekovremenihRadovaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        vrstePrekovremenihRadovaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new VrstaPrekovremenogRadaDto()));
        vrstePrekovremenihRadovaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        vrstePrekovremenihRadovaPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        vrstePrekovremenihRadovaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        vrstePrekovremenihRadovaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        vrstePrekovremenihRadovaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(vrstaPrekovremenogRadaPage, pageIndex, () -> refreshPageableTableView(true)));
        vrstePrekovremenihRadovaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(vrstaPrekovremenogRadaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(vrstaPrekovremenogRadaDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(vrstaPrekovremenogRadaDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(vrstaPrekovremenogRadaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja vrste prekovremenih radova.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), vrstePrekovremenihRadovaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            vrstaPrekovremenogRadaPage = !enableSearchAndSort ? vrstaPrekovremenogRadaService.findAll(queryCriteriaDto.getPageable()) : vrstaPrekovremenogRadaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(vrstePrekovremenihRadovaPageableTableView, vrstaPrekovremenogRadaPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto) {
        DodajUrediVrstuPrekovremenogRadaController dodajUrediVrstuPrekovremenogRadaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_VRSTU_PREKOVREMENOG_RADA, vrstaPrekovremenogRadaDto);
        if (dodajUrediVrstuPrekovremenogRadaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji vrste prekovremenog rada", vrstaPrekovremenogRadaDto, propertiesInfo);
    }

    private void onIzbrisiClick(VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu vrstu prekovremenog rada?", vrstaPrekovremenogRadaDto, propertiesInfo, "Vrsta prekovremenog rada je uspješno izbrisana.", "Desila se greška prilikom brisanja vrste prekovremenog rada.", () -> {
            vrstaPrekovremenogRadaService.delete(vrstaPrekovremenogRadaDto.getIdVrstaPrekovremenogRada());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("VrstaPrekovremenogRada", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Vrste_Prekovremenih_Radova"), f -> () -> GenericBeanReportPdf.create("VRSTE PREKOVREMENIH RADOVA", vrstaPrekovremenogRadaService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
