/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstaobustave;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave.VrstaObustaveDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave.VrstaObustaveService;
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
        TableUtils.addColumns(vrsteObustavaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        vrsteObustavaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new VrstaObustaveDto()));
        vrsteObustavaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        vrsteObustavaPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        vrsteObustavaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        vrsteObustavaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        vrsteObustavaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(vrstaObustavePage, pageIndex, () -> refreshPageableTableView(true)));
        vrsteObustavaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(vrstaObustavePage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(VrstaObustaveDto vrstaObustaveDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(vrstaObustaveDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(vrstaObustaveDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(vrstaObustaveDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja vrsta obustava.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), vrsteObustavaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            vrstaObustavePage = !enableSearchAndSort ? vrstaObustaveService.findAll(queryCriteriaDto.getPageable()) : vrstaObustaveService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(vrsteObustavaPageableTableView, vrstaObustavePage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(VrstaObustaveDto vrstaObustaveDto) {
        DodajUrediVrstuObustaveController dodajUrediVrstuObustaveController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_VRSTU_OBUSTAVE, vrstaObustaveDto);
        if (dodajUrediVrstuObustaveController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(VrstaObustaveDto vrstaObustaveDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji vrste obustave", vrstaObustaveDto, propertiesInfo);
    }

    private void onIzbrisiClick(VrstaObustaveDto vrstaObustaveDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu vrstu obustave?", vrstaObustaveDto, propertiesInfo, "Vrsta obustave je uspješno izbrisana.", "Desila se greška prilkom brisanja vrste obustave.", () -> {
            vrstaObustaveService.delete(vrstaObustaveDto.getIdVrstaObustave());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("VrstaObustave", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Vrste_Obustava"), f -> () -> GenericBeanReportPdf.create("VRSTE OBUSTAVA", vrstaObustaveService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
