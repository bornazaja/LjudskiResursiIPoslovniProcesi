/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.dodatak;

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
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakService;
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
public class DodatciZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<DodatakDto> dodatciZaposlenikaPageableTableView;

    @Autowired
    private DodatakService dodatakService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<DodatakDto> dodatakPage;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenika;
    private Role trenutnaRola;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Dodatak", "Naziv", "Vrsta dodatka", "Iznos", "Valuta", "Datum od", "Datum do");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idDodatak", "naziv", "vrstaDodatka.naziv", "iznos", "valuta.naziv", "datumOd", "datumDo");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), dodatciZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idDodatak"));
    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        zaposlenikDetailsDto = t.getZaposlenikDetailsDto();
        sourcePrijavljenogZaposlenika = t.getSourcePrijavljenogZaposlenika();
        String naslov = sourcePrijavljenogZaposlenika.equals(SourcePrijavljenogZaposlenika.ZAPOSLENICI) ? String.format("Dodatci zaposlenika: %s %s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()) : "Moji dodatci";
        dodatciZaposlenikaPageableTableView.setTitle(naslov);
        trenutnaRola = EnumUtils.fromValue(AppUtils.getPrijavljeniZaposlenik().getTrenutnaRola().getIdRola(), Role.values(), "getId");
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(dodatciZaposlenikaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> dodatciZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new DodatakDto())));
        dodatciZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        dodatciZaposlenikaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        dodatciZaposlenikaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> dodatciZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes(root, FxmlView.ZAPOSLENICI)));
        dodatciZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        dodatciZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(dodatakPage, pageIndex, () -> refreshPageableTableView(true)));
        dodatciZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(dodatakPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(DodatakDto dodatakDto) {
        MenuItemListBuilder menuItemListBuilder = new MenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(dodatakDto)));
        menuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(dodatakDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(dodatakDto)));
        return menuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), dodatciZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            dodatakPage = !enableSearchAndSort ? dodatakService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : dodatakService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(dodatciZaposlenikaPageableTableView, dodatakPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja dodataka zaposlenika.");
    }

    private void onDodajUrediClick(DodatakDto dodatakDto) {
        dodatakDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediDodatakZaposlenikaController dodajUrediDodatakZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_DODATAK_ZAPOSLENIKA, dodatakDto, AppUtils.getCssPathables());
        if (dodajUrediDodatakZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(DodatakDto dodatakDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji dodatka zaposlenika", dodatakDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(DodatakDto dodatakDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj dodatak zaposlenika?", dodatakDto, propertiesInfo, () -> {
            dodatakService.delete(dodatakDto.getIdDodatak());
            refreshPageableTableView(true);
        }, "Dodatak zaposlenika je uspješno izbrisan.", "Desila se greška prilikom brisanja dodatka zaposlenika.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.DODATAK, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Dodatci_Zaposlenika", LocalDateTimePattern.HR, zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()),
                f -> () -> GenericBeanReportPdf.create(String.format("DODATCI ZAPOSLENIKA: %s %s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime().toUpperCase()), dodatakService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis dodataka zapolsenika je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiraja u popisa dodataka zaposlenika u PDF.");
    }
}
