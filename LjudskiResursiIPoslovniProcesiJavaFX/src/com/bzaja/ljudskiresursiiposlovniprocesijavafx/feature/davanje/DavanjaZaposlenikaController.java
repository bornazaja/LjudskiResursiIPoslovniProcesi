/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.davanje;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.SourcePrijavljenogZaposlenika;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.ZaposlenikSourceResultDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Role;
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
import com.bzaja.myjavafxlibrary.springframework.manager.PaneManager;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
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
public class DavanjaZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<DavanjeDto> davanjaZaposlenikaPageableTableView;

    @Autowired
    private DavanjeService davanjeService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<DavanjeDto> davanjePage;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenika;
    private Role trenutnaRola;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Davanje", "Vrsta davanja", "Datum od", "Datum do");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idDavanje", "vrstaDavanja.naziv", "datumOd", "datumDo");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), davanjaZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idDavanje"));
    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        zaposlenikDetailsDto = t.getZaposlenikDetailsDto();
        sourcePrijavljenogZaposlenika = t.getSourcePrijavljenogZaposlenika();
        String naslov = t.getSourcePrijavljenogZaposlenika().equals(SourcePrijavljenogZaposlenika.ZAPOSLENICI) ? String.format("Davanja zaposlenika: %s %s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()) : "Moja davanja";
        davanjaZaposlenikaPageableTableView.setTitle(naslov);
        trenutnaRola = EnumUtils.fromValue(AppUtils.getPrijavljeniZaposlenik().getTrenutnaRola().getIdRola(), Role.values(), "getId");
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(davanjaZaposlenikaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> davanjaZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new DavanjeDto())));
        davanjaZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        davanjaZaposlenikaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        davanjaZaposlenikaPageableTableView.addMenuItem("Eksporitraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> davanjaZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes(root, FxmlView.ZAPOSLENICI)));
        davanjaZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        davanjaZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(davanjePage, pageIndex, () -> refreshPageableTableView(true)));
        davanjaZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(davanjePage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(DavanjeDto davanjeDto) {
        MenuItemListBuilder menuItemListBuilder = new MenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(davanjeDto)));
        menuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(davanjeDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(davanjeDto)));
        return menuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), davanjaZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            davanjePage = !enableSearchAndSort ? davanjeService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : davanjeService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(davanjaZaposlenikaPageableTableView, davanjePage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja davanja zaposlenika.");
    }

    private void onDodajUrediClick(DavanjeDto davanjeDto) {
        davanjeDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediDavanjeZaposlenikaController dodajUrediDavanjeZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_DAVANJE_ZAPOSLENIKA, davanjeDto, AppUtils.getCssPathables());
        if (dodajUrediDavanjeZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(DavanjeDto davanjeDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji davanja zaposlenika", davanjeDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(DavanjeDto davanjeDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da šelite izbrisati ovo davanje zaposlenika?", davanjeDto, propertiesInfo, () -> {
            davanjeService.delete(davanjeDto.getIdDavanje());
            refreshPageableTableView(true);
        }, "Davanje zaposlenika je uspješno izbrisano.", "Desila se greška prilikom brisanja davnaja zaposlenika.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.DAVANJE, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Davanja_Zaposlenika", LocalDateTimePattern.HR, zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()),
                f -> () -> GenericBeanReportPdf.create(String.format("DAVANJA ZAPOSLENIKA: %s %s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime().toUpperCase()), davanjeService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis davanja zaposlenika je uspješno eksporitran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa davanja zaposlenika u PDF.");
    }
}
