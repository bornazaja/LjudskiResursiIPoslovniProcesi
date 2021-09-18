/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.povijestprijava;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijavaService;
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
public class PovijestPrijavaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<PovijestPrijavaDto> povijestPrijavaPageableTableView;

    @Autowired
    private PovijestPrijavaService povijestPrijavaService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<PovijestPrijavaDto> povijestPrijavaPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Povijest prijava", "Vrijeme prijave", "Ime zaposlenika", "Prezime zaposlenika", "Rola");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idPovijestPrijava", "vrijemePrijave", "zaposlenik.ime", "zaposlenik.prezime", "rola.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), povijestPrijavaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idPovijestPrijava"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(povijestPrijavaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        povijestPrijavaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        povijestPrijavaPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        povijestPrijavaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        povijestPrijavaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        povijestPrijavaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(povijestPrijavaPage, pageIndex, () -> refreshPageableTableView(true)));
        povijestPrijavaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(povijestPrijavaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(PovijestPrijavaDto povijestPrijavaDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(povijestPrijavaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja povijesti prijava.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), povijestPrijavaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            povijestPrijavaPage = !enableSearchAndSort ? povijestPrijavaService.findAll(queryCriteriaDto.getPageable()) : povijestPrijavaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(povijestPrijavaPageableTableView, povijestPrijavaPage, pageIndex.get()));
        });
    }

    private void onDetaljiClick(PovijestPrijavaDto povijestPrijavaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji povijesti prijave", povijestPrijavaDto, propertiesInfo);
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("PovijestPrijava", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Povijest_Prijava"), f -> () -> GenericBeanReportPdf.create("POVIJEST PRIJAVA", povijestPrijavaService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
