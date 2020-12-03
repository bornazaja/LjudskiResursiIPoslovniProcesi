/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.isplatnalista;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.PaneManager;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.isplatnalista.IsplatnaListaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraPdfReportFactory;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanClass;
import com.bzaja.myjavalibrary.pdf.GenericBeanReportPdf;
import com.bzaja.myjavalibrary.springframework.data.query.Operator;
import com.bzaja.myjavalibrary.springframework.data.query.QueryCriteriaDto;
import com.bzaja.myjavalibrary.util.EnumUtils;
import com.bzaja.myjavalibrary.util.PropertyInfoDto;
import com.bzaja.myjavalibrary.util.PropertyInfoUtils;
import com.bzaja.myjavalibrary.util.StringUtils;
import com.bzaja.myjavafxlibrary.control.PageableTableView;
import com.bzaja.myjavalibrary.util.LanguageFormat;
import com.bzaja.myjavalibrary.util.LocalDateTimePattern;
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
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Controller
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
        TableUtils.addColumns(isplatneListePageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        isplatneListePageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        isplatneListePageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        isplatneListePageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        isplatneListePageableTableView.addMenuItem("Natrag na obračune ugovora", (e) -> paneManager.switchPanes(root, FxmlView.OBRACUNI_UGOVORA));
        isplatneListePageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        isplatneListePageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(isplatnaListaPage, pageIndex, () -> refreshPageableTableView(true)));
        isplatneListePageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(isplatnaListaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(IsplatnaListaDto isplatnaListaDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(isplatnaListaDto))
                .addMenuItem("Eksportiraj isplatnu listu u PDF", () -> onEksportirajIsplatnuListuUPdfClick(isplatnaListaDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), isplatneListePageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            isplatnaListaPage = !enableSearchAndSort ? isplatnaListaService.findAllByIdObracunUgovora(obracunUgovoraDto.getIdObracunUgovora(), queryCriteriaDto.getPageable()) : isplatnaListaService.findAllByIdObracunUgovora(obracunUgovoraDto.getIdObracunUgovora(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(isplatneListePageableTableView, isplatnaListaPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja isplatnih lista.");
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
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji isplatne liste", isplatnaListaDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onEksportirajIsplatnuListuUPdfClick(IsplatnaListaDto isplatnaListaDto) {
        ZaposlenikDetailsDto zaposlenikDetailsDto = isplatnaListaDto.getUgovor().getZaposlenik();
        String nazivDatoteke = String.format("Isplatna_Lista_%s_%s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime());
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime(nazivDatoteke, LocalDateTimePattern.HR),
                f -> () -> obracunUgovoraPdfReportFactory.get(vrsteObracuna).createByIdIsplatnaLista(isplatnaListaDto.getIdIsplatnaLista(), f.getAbsolutePath()),
                "*.pdf",
                f -> String.format("Ispatna lista je uspješno eksportirana u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja isplatne liste u PDF.");
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.ISPLATNA_LISTA, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Isplatne_Liste", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("ISPLATNE LISTE", isplatnaListaService.findAllByIdObracunUgovora(obracunUgovoraDto.getIdObracunUgovora()), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis isplatnih lista je uspjšno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se grešla prilikom eksportianja popisa u PDF.");
    }
}
