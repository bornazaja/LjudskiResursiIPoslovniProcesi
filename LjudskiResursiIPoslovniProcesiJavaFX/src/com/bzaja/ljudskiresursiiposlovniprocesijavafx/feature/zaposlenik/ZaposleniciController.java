/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.PaneManager;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.EditZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanClass;
import com.bzaja.myjavalibrary.pdf.GenericBeanReportPdf;
import com.bzaja.myjavalibrary.springframework.data.query.Operator;
import com.bzaja.myjavalibrary.springframework.data.query.QueryCriteriaDto;
import com.bzaja.myjavalibrary.util.ObjectMapperUtils;
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
public class ZaposleniciController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<ZaposlenikDetailsDto> zaposleniciPageableTableView;

    @Autowired
    private ZaposlenikService zaposlenikService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<ZaposlenikDetailsDto> zaposlenikPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Zaposlenik", "Ime", "Prezime", "Spol", "Datum rođenja", "OIB", "Email", "Broj telefona");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idZaposlenik", "ime", "prezime", "spol.naziv", "datumRodjenja", "oib", "email", "brojTelefona");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), zaposleniciPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idZaposlenik"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(zaposleniciPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        zaposleniciPageableTableView.addMenuItem("Dodaj", (e) -> onDodajClick());
        zaposleniciPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        zaposleniciPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        zaposleniciPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        zaposleniciPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        zaposleniciPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(zaposlenikPage, pageIndex, () -> refreshPageableTableView(true)));
        zaposleniciPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(zaposlenikPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(ZaposlenikDetailsDto zaposlenikDetailsDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onUrediClick(ObjectMapperUtils.map(zaposlenikDetailsDto, EditZaposlenikDto.class)))
                .addMenuItem("Promjeni lozinku", () -> onPromjeniLozinkuClick(zaposlenikDetailsDto.getIdZaposlenik()))
                .addMenuItem("Detalji", () -> onDetaljiClick(zaposlenikDetailsDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(zaposlenikDetailsDto))
                .addMenuItem("Davanja", () -> paneManager.switchPanes(root, FxmlView.DAVANJA_ZAPOSLENIKA, new ZaposlenikSourceResultDto(zaposlenikDetailsDto, SourcePrijavljenogZaposlenika.ZAPOSLENICI)))
                .addMenuItem("Olakšice", () -> paneManager.switchPanes(root, FxmlView.OLAKSICE_ZAPOSLENIKA, new ZaposlenikSourceResultDto(zaposlenikDetailsDto, SourcePrijavljenogZaposlenika.ZAPOSLENICI)))
                .addMenuItem("Dodatci", () -> paneManager.switchPanes(root, FxmlView.DODATCI_ZAPOSLENIKA, new ZaposlenikSourceResultDto(zaposlenikDetailsDto, SourcePrijavljenogZaposlenika.ZAPOSLENICI)))
                .addMenuItem("Obustave", () -> paneManager.switchPanes(root, FxmlView.OBUSTAVE_ZAPOSLENIKA, new ZaposlenikSourceResultDto(zaposlenikDetailsDto, SourcePrijavljenogZaposlenika.ZAPOSLENICI)))
                .addMenuItem("Prebivališta", () -> paneManager.switchPanes(root, FxmlView.PREBIVALISTA_ZAPOSLENIKA, new ZaposlenikSourceResultDto(zaposlenikDetailsDto, SourcePrijavljenogZaposlenika.ZAPOSLENICI)))
                .addMenuItem("Prekovremeni radovi", () -> paneManager.switchPanes(root, FxmlView.PREKOVREMENI_RADOVI_ZAPOSLENIKA, new ZaposlenikSourceResultDto(zaposlenikDetailsDto, SourcePrijavljenogZaposlenika.ZAPOSLENICI)))
                .addMenuItem("Role", () -> paneManager.switchPanes(root, FxmlView.ROLE_ZAPOSLENIKA, new ZaposlenikSourceResultDto(zaposlenikDetailsDto, SourcePrijavljenogZaposlenika.ZAPOSLENICI)))
                .addMenuItem("Ugovori", () -> paneManager.switchPanes(root, FxmlView.UGOVORI_ZAPOSLENIKA, new ZaposlenikSourceResultDto(zaposlenikDetailsDto, SourcePrijavljenogZaposlenika.ZAPOSLENICI)))
                .addMenuItem("Isplatne liste", () -> paneManager.switchPanes(root, FxmlView.ISPLATNE_LISTE_ZAPOSLENIKA, new ZaposlenikSourceResultDto(zaposlenikDetailsDto, SourcePrijavljenogZaposlenika.ZAPOSLENICI)))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), zaposleniciPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            zaposlenikPage = !enableSearchAndSort ? zaposlenikService.findAll(queryCriteriaDto.getPageable()) : zaposlenikService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(zaposleniciPageableTableView, zaposlenikPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja zaposlenika.");
    }

    private void onDodajClick() {
        DodajZaposlenikaController dodajZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_ZAPOSLENIKA, AppUtils.getCssPathables());
        if (dodajZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onUrediClick(EditZaposlenikDto editZaposlenikDto) {
        UrediZaposlenikaController urediZaposlenikaController = stageManager.showSecondaryStage(FxmlView.UREDI_ZAPOSLENIKA, editZaposlenikDto, AppUtils.getCssPathables());
        if (urediZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(ZaposlenikDetailsDto zaposlenikDetailsDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji zaposlenika", zaposlenikDetailsDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(ZaposlenikDetailsDto zaposlenikDetailsDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovog zaposlenika?", zaposlenikDetailsDto, propertiesInfo, () -> {
            zaposlenikService.delete(zaposlenikDetailsDto.getIdZaposlenik());
            refreshPageableTableView(true);
        }, "Zaposlenik je uspješno izbrisan.", "Desila se grešla prilkom brisanja zaposlenika.", LanguageFormat.HR);
    }

    private void onPromjeniLozinkuClick(Integer idZaposlenik) {
        PromjenaLozinkeZaposlenikaController promjenaLozinkeZaposlenikaController = stageManager.showSecondaryStage(FxmlView.PROMJENA_LOZINKE_ZAPOSLENIKA, idZaposlenik, AppUtils.getCssPathables());
        if (promjenaLozinkeZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.ZAPOSLENIK, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Zaposlenici", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("ZAPOSLENICI", zaposlenikService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis zaposlenika je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa zaposlenika u PDF.");
    }
}
