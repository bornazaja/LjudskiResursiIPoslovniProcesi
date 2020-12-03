/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.prekovremenirad;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.SourcePrijavljenogZaposlenika;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.ZaposlenikSourceResultDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.PaneManager;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadService;
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
public class PrekovremeniRadoviZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<PrekovremeniRadDto> prekovremeniRadoviZaposlenikaPageableTableView;

    @Autowired
    private PrekovremeniRadService prekovremeniRadService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<PrekovremeniRadDto> prekovremeniRadPage;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenika;
    private Role trenutnaRola;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Prekovremeni rad", "Naziv", "Vrsta prekovremenog rada", "Broj dodatnih sati", "Datum od", "Datum do");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idPrekovremeniRad", "naziv", "vrstaPrekovremenogRada.naziv", "brojDodatnihSati", "datumOd", "datumDo");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), prekovremeniRadoviZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idPrekovremeniRad"));
    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        zaposlenikDetailsDto = t.getZaposlenikDetailsDto();
        sourcePrijavljenogZaposlenika = t.getSourcePrijavljenogZaposlenika();
        String naslov = sourcePrijavljenogZaposlenika.equals(SourcePrijavljenogZaposlenika.ZAPOSLENICI) ? String.format("Prekovremeni radovi zaposlenika: %s %s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()) : "Moji prekovremeni radovi";
        prekovremeniRadoviZaposlenikaPageableTableView.setTitle(naslov);
        trenutnaRola = EnumUtils.fromValue(AppUtils.getPrijavljeniZaposlenik().getTrenutnaRola().getIdRola(), Role.values(), "getId");
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(prekovremeniRadoviZaposlenikaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> prekovremeniRadoviZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new PrekovremeniRadDto())));
        prekovremeniRadoviZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        prekovremeniRadoviZaposlenikaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        prekovremeniRadoviZaposlenikaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> prekovremeniRadoviZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes(root, FxmlView.ZAPOSLENICI)));
        prekovremeniRadoviZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        prekovremeniRadoviZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(prekovremeniRadPage, pageIndex, () -> refreshPageableTableView(true)));
        prekovremeniRadoviZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(prekovremeniRadPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(PrekovremeniRadDto prekovremeniRadDto) {
        MenuItemListBuilder menuItemListBuilder = new MenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(prekovremeniRadDto)));
        menuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(prekovremeniRadDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(prekovremeniRadDto)));
        return menuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), prekovremeniRadoviZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            prekovremeniRadPage = !enableSearchAndSort ? prekovremeniRadService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : prekovremeniRadService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(prekovremeniRadoviZaposlenikaPageableTableView, prekovremeniRadPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja prekovremenih radova zaposlenika.");
    }

    private void onDodajUrediClick(PrekovremeniRadDto prekovremeniRadDto) {
        prekovremeniRadDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediPrekovremeniRadZaposlenikaController dodajUrediPrekovremeniRadZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_PREKOVREMENI_RAD_ZAPOSLENIKA, prekovremeniRadDto, AppUtils.getCssPathables());
        if (dodajUrediPrekovremeniRadZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(PrekovremeniRadDto prekovremeniRadDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji prekovremenog rada zaposlenika", prekovremeniRadDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(PrekovremeniRadDto prekovremeniRadDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj prekovremeni rad zapsolenika?", prekovremeniRadDto, propertiesInfo, () -> {
            prekovremeniRadService.delete(prekovremeniRadDto.getIdPrekovremeniRad());
            refreshPageableTableView(true);
        }, "Prekovremeni rad zaposlenika je uspješno izbrisan.", "Desila se greška prilikom brisanja prekovremenog rada zaposlenika.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.PREKOVREMENI_RAD, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Prekovremeni_Radovi_Zaposlenika", LocalDateTimePattern.HR, zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()),
                f -> () -> GenericBeanReportPdf.create(String.format("PREKOVREMENI RADOVI ZAPOSLENIKA: %s %s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime().toUpperCase()), prekovremeniRadService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis prekeovremenih radova zaposlenika je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa prekovremenih radova zaposlenika u PDF.");
    }
}
