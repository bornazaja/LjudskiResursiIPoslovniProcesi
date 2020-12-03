/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.ugovororadu;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.SourcePrijavljenogZaposlenika;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.ZaposlenikSourceResultDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.PaneManager;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Role;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduService;
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
import javafx.scene.layout.Pane;
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
public class UgovoriORaduZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<UgovorORaduDto> ugovoriORaduZaposlenikaPageableTableView;

    @Autowired
    private UgovorORaduService ugovorORaduService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<UgovorORaduDto> ugovorORaduPage;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenika;
    private Role trenutnaRola;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Ugovor", "Radni odnos", "Radno mjesto", "Datum od", "Datum do", "Broj radnih sati tjedno", "Bruto plaća", "Valuta");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idUgovor", "radniOdnos.naziv", "radnoMjesto.naziv", "datumOd", "datumDo", "brojRadnihSatiTjedno", "brutoPlaca", "valuta.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), ugovoriORaduZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idUgovor"));
    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        zaposlenikDetailsDto = t.getZaposlenikDetailsDto();
        sourcePrijavljenogZaposlenika = t.getSourcePrijavljenogZaposlenika();
        String naslov = sourcePrijavljenogZaposlenika.equals(SourcePrijavljenogZaposlenika.ZAPOSLENICI) ? String.format("Ugovori o radu zaposlenika: %s %s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()) : "Moji ugovori o radu";
        ugovoriORaduZaposlenikaPageableTableView.setTitle(naslov);
        trenutnaRola = EnumUtils.fromValue(AppUtils.getPrijavljeniZaposlenik().getTrenutnaRola().getIdRola(), Role.values(), "getId");
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(ugovoriORaduZaposlenikaPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> ugovoriORaduZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new UgovorORaduDto())));
        ugovoriORaduZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        ugovoriORaduZaposlenikaPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        ugovoriORaduZaposlenikaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> ugovoriORaduZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes((Pane) NodeUtils.getTabPaneFromInsideNode(root).getParent(), FxmlView.ZAPOSLENICI)));
        ugovoriORaduZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        ugovoriORaduZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(ugovorORaduPage, pageIndex, () -> refreshPageableTableView(true)));
        ugovoriORaduZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(ugovorORaduPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(UgovorORaduDto ugovorORaduDto) {
        MenuItemListBuilder menuItemListBuilder = new MenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(ugovorORaduDto)));
        menuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(ugovorORaduDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> menuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(ugovorORaduDto)));
        return menuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), ugovoriORaduZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            ugovorORaduPage = !enableSearchAndSort ? ugovorORaduService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : ugovorORaduService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(ugovoriORaduZaposlenikaPageableTableView, ugovorORaduPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja ugovora o radu zaposlenika.");
    }

    private void onDodajUrediClick(UgovorORaduDto ugovorORaduDto) {
        ugovorORaduDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediUgovorORaduZaposlenikaController dodajUrediUgovorORaduZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_UGOVOR_O_RADU_ZAPOSLENIKA, ugovorORaduDto, AppUtils.getCssPathables());
        if (dodajUrediUgovorORaduZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(UgovorORaduDto ugovorORaduDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji ugovora o radu zaposlenika", ugovorORaduDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(UgovorORaduDto ugovorORaduDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj ugovor o radu zaposlenika?", ugovorORaduDto, propertiesInfo, () -> {
            ugovorORaduService.delete(ugovorORaduDto.getIdUgovor());
            refreshPageableTableView(true);
        }, "Ugovor o radu zaposlenika je uspješno izbrisan.", "Desila se greška prilikom brisanja ugovora o radu zaposlenika.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.UGOVOR_O_DJELU, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Ugovori_O_Radu_Zaposlenika", LocalDateTimePattern.HR, zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()),
                f -> () -> GenericBeanReportPdf.create(String.format("UGOVORI O RADU ZAPOSLENIKA: %s %s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime().toUpperCase()), ugovorORaduService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis ugovora ugovora o radu je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksporitranja popisa ugovora o radu u PDF.");
    }
}
