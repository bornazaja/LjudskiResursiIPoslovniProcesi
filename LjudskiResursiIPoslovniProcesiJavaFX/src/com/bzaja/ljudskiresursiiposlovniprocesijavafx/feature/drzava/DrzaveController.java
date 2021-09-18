/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.drzava;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaService;
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
public class DrzaveController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<DrzavaDto> drzavePageableTableView;

    @Autowired
    private DrzavaService drzavaService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<DrzavaDto> drzavaPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Država", "Naziv", "Valuta", "Je domovina");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idDrzava", "naziv", "valuta.naziv", "jeDomovina");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), drzavePageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idDrzava"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(drzavePageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        drzavePageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new DrzavaDto()));
        drzavePageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        drzavePageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        drzavePageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        drzavePageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        drzavePageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(drzavaPage, pageIndex, () -> refreshPageableTableView(true)));
        drzavePageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(drzavaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(DrzavaDto drzavaDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(drzavaDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(drzavaDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(drzavaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja država.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), drzavePageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            drzavaPage = !enableSearchAndSort ? drzavaService.findAll(queryCriteriaDto.getPageable()) : drzavaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(drzavePageableTableView, drzavaPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(DrzavaDto drzavaDto) {
        DodajUrediDrzavuController dodajUrediDrzavuController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_DRZAVU, drzavaDto);
        if (dodajUrediDrzavuController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(DrzavaDto drzavaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji države", drzavaDto, propertiesInfo);
    }

    private void onIzbrisiClick(DrzavaDto drzavaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu državu?", drzavaDto, propertiesInfo, "Država je uspješno izbrisana.", "Desila se greška prilikom brisanja države.", () -> {
            drzavaService.delete(drzavaDto.getIdDrzava());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Drzava", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Drzave"), f -> () -> GenericBeanReportPdf.create("DRŽAVE", drzavaService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
