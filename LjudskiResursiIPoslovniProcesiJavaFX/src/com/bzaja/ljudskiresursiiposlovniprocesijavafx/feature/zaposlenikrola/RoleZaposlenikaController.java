/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenikrola;

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
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Role;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola.ZaposlenikRolaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola.ZaposlenikRolaService;
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
public class RoleZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<ZaposlenikRolaDto> roleZaposlenikaPageableTableView;

    @Autowired
    private ZaposlenikRolaService zaposlenikRolaService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<ZaposlenikRolaDto> zaposlenikRolaPage;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenika;
    private Role trenutnaRola;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Rola zaposlenika", "Rola");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idZaposlenikRola", "rola.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), roleZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idZaposlenikRola"));
    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        zaposlenikDetailsDto = t.getZaposlenikDetailsDto();
        sourcePrijavljenogZaposlenika = t.getSourcePrijavljenogZaposlenika();
        String naslov = sourcePrijavljenogZaposlenika.equals(SourcePrijavljenogZaposlenika.ZAPOSLENICI) ? String.format("Role zaposlenika: %s %s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()) : "Moje role";
        roleZaposlenikaPageableTableView.setTitle(naslov);
        trenutnaRola = EnumUtils.fromValue(AppUtils.getPrijavljeniZaposlenik().getTrenutnaRola().getIdRola(), Role.values(), "getId");
        initPagableTableView();
        refreshPageableTableView(false);
    }

    private void initPagableTableView() {
        TableUtils.addColumns(roleZaposlenikaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> roleZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new ZaposlenikRolaDto())));
        roleZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        roleZaposlenikaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        roleZaposlenikaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> roleZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes(root, FxmlView.ZAPOSLENICI)));
        roleZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        roleZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(zaposlenikRolaPage, pageIndex, () -> refreshPageableTableView(true)));
        roleZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(zaposlenikRolaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(ZaposlenikRolaDto zaposlenikRolaDto) {
        MenuItemListBuilder menuItemListBuilder = new MenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(zaposlenikRolaDto)));
        menuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(zaposlenikRolaDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(zaposlenikRolaDto)));
        return menuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), roleZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            zaposlenikRolaPage = !enableSearchAndSort ? zaposlenikRolaService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : zaposlenikRolaService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(roleZaposlenikaPageableTableView, zaposlenikRolaPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja rola zaposlenika.");
    }

    private void onDodajUrediClick(ZaposlenikRolaDto zaposlenikRolaDto) {
        zaposlenikRolaDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediRoluZaposlenikaController dodajUrediRoluZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_ROLU_ZAPOSLENIKA, zaposlenikRolaDto, AppUtils.getCssPathables());
        if (dodajUrediRoluZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(ZaposlenikRolaDto zaposlenikRolaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji role zaposlenika", zaposlenikRolaDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(ZaposlenikRolaDto zaposlenikRolaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovo rolu zaposleniku?", zaposlenikRolaDto, propertiesInfo, () -> {
            zaposlenikRolaService.delete(zaposlenikRolaDto.getIdZaposlenikRola());
            refreshPageableTableView(true);
        }, "Rola zaposlenika je uspješno spremljena.", "Desila se greška prilikom brisanja role zaposlenika.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.ZAPOSLENIK_ROLA, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Role_Zaposlenika", LocalDateTimePattern.HR, zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()),
                f -> () -> GenericBeanReportPdf.create(String.format("ROLE ZAPOSLENIKA: %s %s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime().toUpperCase()), zaposlenikRolaService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis rola zaposlenika je upsješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa role zaposlenika u PDF.");
    }
}
