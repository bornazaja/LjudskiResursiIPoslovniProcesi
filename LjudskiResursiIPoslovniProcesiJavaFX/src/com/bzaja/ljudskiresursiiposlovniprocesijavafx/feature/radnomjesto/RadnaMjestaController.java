/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.radnomjesto;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoService;
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
public class RadnaMjestaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<RadnoMjestoDto> radnaMjestaPageableTableView;

    @Autowired
    private RadnoMjestoService radnoMjestoService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<RadnoMjestoDto> radnoMjestoPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Radno mjesto", "Naziv", "Odjel");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idRadnoMjesto", "naziv", "odjel.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), radnaMjestaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idRadnoMjesto"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(radnaMjestaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        radnaMjestaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new RadnoMjestoDto()));
        radnaMjestaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        radnaMjestaPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        radnaMjestaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        radnaMjestaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        radnaMjestaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(radnoMjestoPage, pageIndex, () -> refreshPageableTableView(true)));
        radnaMjestaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(radnoMjestoPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(RadnoMjestoDto radnoMjestoDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(radnoMjestoDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(radnoMjestoDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(radnoMjestoDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja radnih mjesta.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), radnaMjestaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            radnoMjestoPage = !enableSearchAndSort ? radnoMjestoService.findAll(queryCriteriaDto.getPageable()) : radnoMjestoService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(radnaMjestaPageableTableView, radnoMjestoPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(RadnoMjestoDto radnoMjestoDto) {
        DodajUrediRadnoMjestoController dodajUrediRadnoMjestoController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_RADNO_MJESTO, radnoMjestoDto);
        if (dodajUrediRadnoMjestoController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(RadnoMjestoDto radnoMjestoDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji radnog mjesta", radnoMjestoDto, propertiesInfo);
    }

    private void onIzbrisiClick(RadnoMjestoDto radnoMjestoDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da žeilte izbrisati ovo radno mjesto?", radnoMjestoDto, propertiesInfo, "Radno mjesto je uspješno izbrisano.", "Desila se greška prilikom brisanja radnog mjesta.", () -> {
            radnoMjestoService.delete(radnoMjestoDto.getIdRadnoMjesto());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("RadnoMjesto", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Radna_Mjesta"), f -> () -> GenericBeanReportPdf.create("RADNA MJESTA", radnoMjestoService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
