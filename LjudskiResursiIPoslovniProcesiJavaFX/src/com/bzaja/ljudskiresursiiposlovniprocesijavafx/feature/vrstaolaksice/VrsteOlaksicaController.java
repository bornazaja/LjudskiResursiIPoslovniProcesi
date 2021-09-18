/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstaolaksice;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice.VrstaOlaksiceDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice.VrstaOlaksiceService;
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
public class VrsteOlaksicaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<VrstaOlaksiceDto> vrsteOlaksicaPageableTableView;

    @Autowired
    private VrstaOlaksiceService vrstaOlaksiceService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<VrstaOlaksiceDto> vrstaOlaksicePage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Vrsta olakšice", "Naziv", "Koeficjent", "Vrijedi do");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idVrstaOlaksice", "naziv", "koeficjent", "vrijediDo");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), vrsteOlaksicaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idVrstaOlaksice"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(vrsteOlaksicaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        vrsteOlaksicaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new VrstaOlaksiceDto()));
        vrsteOlaksicaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        vrsteOlaksicaPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        vrsteOlaksicaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        vrsteOlaksicaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        vrsteOlaksicaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(vrstaOlaksicePage, pageIndex, () -> refreshPageableTableView(true)));
        vrsteOlaksicaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(vrstaOlaksicePage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(VrstaOlaksiceDto vrstaOlaksiceDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(vrstaOlaksiceDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(vrstaOlaksiceDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(vrstaOlaksiceDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja vrste olakšica.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), vrsteOlaksicaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            vrstaOlaksicePage = !enableSearchAndSort ? vrstaOlaksiceService.findAll(queryCriteriaDto.getPageable()) : vrstaOlaksiceService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(vrsteOlaksicaPageableTableView, vrstaOlaksicePage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(VrstaOlaksiceDto vrstaOlaksiceDto) {
        DodajUrediVrstuOlaksiceController dodajUrediVrstuOlaksiceController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_VRSTU_OLAKSICE, vrstaOlaksiceDto);
        if (dodajUrediVrstuOlaksiceController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(VrstaOlaksiceDto vrstaOlaksiceDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji vrste olakšice", vrstaOlaksiceDto, propertiesInfo);
    }

    private void onIzbrisiClick(VrstaOlaksiceDto vrstaOlaksiceDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu vrstu olakšice?", vrstaOlaksiceDto, propertiesInfo, "Vrste olakšice je uspješno izbrisana.", "Desila se greška prilikom brisanja vrste olakšice.", () -> {
            vrstaOlaksiceService.delete(vrstaOlaksiceDto.getIdVrstaOlaksice());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("VrstaOlaksice", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Vrste_Olaksica"), f -> () -> GenericBeanReportPdf.create("VRSTE OLAKŠICA", vrstaOlaksiceService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
