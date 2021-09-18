/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstadavanja;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja.VrstaDavanjaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja.VrstaDavanjaService;
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
public class VrsteDavanjaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<VrstaDavanjaDto> vrsteDavanjaPageableTableView;

    @Autowired
    private VrstaDavanjaService vrstaDavanjaService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<VrstaDavanjaDto> vrstaDavanjaPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Vrsta davanja", "Naziv", "Stopa na plaću", "Stopa iz plaće", "Vrijedi do");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idVrstaDavanja", "naziv", "stopaNaPlacu", "stopaIzPlace", "vrijediDo");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), vrsteDavanjaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idVrstaDavanja"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(vrsteDavanjaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        vrsteDavanjaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new VrstaDavanjaDto()));
        vrsteDavanjaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(true));
        vrsteDavanjaPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        vrsteDavanjaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        vrsteDavanjaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        vrsteDavanjaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(vrstaDavanjaPage, pageIndex, () -> refreshPageableTableView(true)));
        vrsteDavanjaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(vrstaDavanjaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(VrstaDavanjaDto vrstaDavanjaDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(vrstaDavanjaDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(vrstaDavanjaDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(vrstaDavanjaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja vrsta davanja.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), vrsteDavanjaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            vrstaDavanjaPage = !enableSearchAndSort ? vrstaDavanjaService.findAll(queryCriteriaDto.getPageable()) : vrstaDavanjaService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(vrsteDavanjaPageableTableView, vrstaDavanjaPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(VrstaDavanjaDto vrstaDavanjaDto) {
        DodajUrediVrstuDavanjaController dodajUrediVrstuDavanjaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_VRSTU_DAVANJA, vrstaDavanjaDto);
        if (dodajUrediVrstuDavanjaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(VrstaDavanjaDto vrstaDavanjaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji vrste davanja", vrstaDavanjaDto, propertiesInfo);
    }

    private void onIzbrisiClick(VrstaDavanjaDto vrstaDavanjaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da žeilte izbrisati ovu vrstu davanja?", vrstaDavanjaDto, propertiesInfo, "Vrsta davanja je uspješno izbrisana.", "Desila se greška prilikom brisanja vrste davanja.", () -> {
            vrstaDavanjaService.delete(vrstaDavanjaDto.getIdVrstaDavanja());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("VrstaDavanja", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Vrste_Davanja"), f -> () -> GenericBeanReportPdf.create("VRSTE DAVANAJA", vrstaDavanjaService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
