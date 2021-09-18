/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.PaneManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraPdfReportFactory;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.GenericBeanReportPdf;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.EnumUtils;
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
public class IsplatneListeController implements Initializable, ControllerInterface<ObracunUgovoraDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<IsplatnaListaDto> isplatneListePageableTableView;

    @Autowired
    private IsplatnaListaService isplatnaListaService;

    @Autowired
    private ObracunUgovoraPdfReportFactory obracunUgovoraPdfReportFactory;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<IsplatnaListaDto> isplatnaListaPage;
    private ObracunUgovoraDto obracunUgovoraDto;
    private VrsteObracuna vrsteObracuna;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Isplatna lista", "Vrsta obračuna", "Ime zaposlenika", "Prezime zaposlenika");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idIsplatnaLista", "obracunUgovora.vrstaObracuna.naziv", "ugovor.zaposlenik.ime", "ugovor.zaposlenik.prezime");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), isplatneListePageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idIsplatnaLista"));
        initPageableTableView();
    }

    private void initPageableTableView() {
        TableUtils.addColumns(isplatneListePageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        isplatneListePageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        isplatneListePageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        isplatneListePageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        isplatneListePageableTableView.addMenuItem("Natrag na obračune ugovora", (e) -> paneManager.switchPanes(root, FxmlView.OBRACUNI_UGOVORA));
        isplatneListePageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        isplatneListePageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(isplatnaListaPage, pageIndex, () -> refreshPageableTableView(true)));
        isplatneListePageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(isplatnaListaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(IsplatnaListaDto isplatnaListaDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(isplatnaListaDto))
                .addMenuItem("Eksportiraj isplatnu listu u PDF", () -> onEksportirajIsplatnuListuUPdfClick(isplatnaListaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja isplatnih lista.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), isplatneListePageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            isplatnaListaPage = !enableSearchAndSort ? isplatnaListaService.findAllByIdObracunUgovora(obracunUgovoraDto.getIdObracunUgovora(), queryCriteriaDto.getPageable()) : isplatnaListaService.findAllByIdObracunUgovora(obracunUgovoraDto.getIdObracunUgovora(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(isplatneListePageableTableView, isplatnaListaPage, pageIndex.get()));
        });
    }

    @Override
    public void initData(ObracunUgovoraDto t) {
        obracunUgovoraDto = t;
        vrsteObracuna = EnumUtils.fromValue(t.getVrstaObracuna().getIdVrstaObracuna(), VrsteObracuna.values(), "getId");
        isplatneListePageableTableView.setTitle(getNaslov());
        refreshPageableTableView(true);
    }

    private String getNaslov() {
        switch (vrsteObracuna) {
            case OBRACUN_UGOVORA_O_RADU:
                return "Isplatne liste obračuna ugovora o radu";
            case OBRACUN_UGOVORA_O_DJELU:
                return "Isplatne liste obračuna ugovora o djelu";
            case OBRACUN_STUDENTSKIH_UGOVORA:
                return "Isplatne liste obračuna studentskih ugovora";
            default:
                return isplatneListePageableTableView.getTitle();
        }
    }

    private void onDetaljiClick(IsplatnaListaDto isplatnaListaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji isplatne liste", isplatnaListaDto, propertiesInfo);
    }

    private void onEksportirajIsplatnuListuUPdfClick(IsplatnaListaDto isplatnaListaDto) {
        ZaposlenikDetailsDto zaposlenikDetailsDto = isplatnaListaDto.getUgovor().getZaposlenik();
        String nazivDatoteke = String.format("Isplatna_Lista_%s_%s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime());
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime(nazivDatoteke), f -> () -> obracunUgovoraPdfReportFactory.get(vrsteObracuna).createByIdIsplatnaLista(isplatnaListaDto.getIdIsplatnaLista(), f.getAbsolutePath()), "*.pdf");
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("IsplatnaLista", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Isplatne_Liste"), f -> () -> GenericBeanReportPdf.create("ISPLATNE LISTE", isplatnaListaService.findAllByIdObracunUgovora(obracunUgovoraDto.getIdObracunUgovora()), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
