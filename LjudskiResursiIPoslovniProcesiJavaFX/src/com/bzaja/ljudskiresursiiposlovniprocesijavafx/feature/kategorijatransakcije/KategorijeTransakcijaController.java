/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.kategorijatransakcije;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcijeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcijeService;
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
public class KategorijeTransakcijaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<KategorijaTransakcijeDto> kategorijeTransakcijaPageableTableView;

    @Autowired
    private KategorijaTransakcijeService kategorijaTransakcijeService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<KategorijaTransakcijeDto> kategorijaTransakcijaPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Kategorija transakcije", "Naziv");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idKategorijaTransakcije", "naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), kategorijeTransakcijaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idKategorijaTransakcije"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(kategorijeTransakcijaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        kategorijeTransakcijaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new KategorijaTransakcijeDto()));
        kategorijeTransakcijaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        kategorijeTransakcijaPageableTableView.addMenuItem("Pretraživanje i sortianje", (e) -> onPretrazivanjeISortiranjeClick());
        kategorijeTransakcijaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        kategorijeTransakcijaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        kategorijeTransakcijaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(kategorijaTransakcijaPage, pageIndex, () -> refreshPageableTableView(true)));
        kategorijeTransakcijaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(kategorijaTransakcijaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(KategorijaTransakcijeDto kategorijaTransakcijeDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(kategorijaTransakcijeDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(kategorijaTransakcijeDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(kategorijaTransakcijeDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja kategorija transakcija.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), kategorijeTransakcijaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            kategorijaTransakcijaPage = !enableSearchAndSort ? kategorijaTransakcijeService.findAll(queryCriteriaDto.getPageable()) : kategorijaTransakcijeService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(kategorijeTransakcijaPageableTableView, kategorijaTransakcijaPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(KategorijaTransakcijeDto kategorijaTransakcijeDto) {
        DodajUrediKategorijuTransakcijeController dodajUrediKategorijuTransakcijeController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_KATEGORIJU_TRANSAKCIJE, kategorijaTransakcijeDto);
        if (dodajUrediKategorijuTransakcijeController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(KategorijaTransakcijeDto kategorijaTransakcijeDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji kategorije transkacije", kategorijaTransakcijeDto, propertiesInfo);
    }

    private void onIzbrisiClick(KategorijaTransakcijeDto kategorijaTransakcijeDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurani da želite izbrisati ovu kategoriju transakcije?", kategorijaTransakcijeDto, propertiesInfo, "Kategorija transkcije je uspješno izbrisana.", "Desila se greška prilikom brisanja kategorije transakcije.", () -> {
            kategorijaTransakcijeService.delete(kategorijaTransakcijeDto.getIdKategorijaTransakcije());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("KategorijaTransakcije", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Kategorije_Transakcija"), f -> () -> GenericBeanReportPdf.create("KATEGORIJE TRANSAKCIJA", kategorijaTransakcijeService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
