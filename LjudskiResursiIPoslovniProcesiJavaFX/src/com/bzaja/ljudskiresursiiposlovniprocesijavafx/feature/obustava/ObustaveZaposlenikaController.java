/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obustava;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.SourcePrijavljenogZaposlenika;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.ZaposlenikSourceResultDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.PaneManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Role;
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
public class ObustaveZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<ObustavaDto> obustaveZaposlenikaPageableTableView;

    @Autowired
    private ObustavaService obustavaService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<ObustavaDto> obustavaPage;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenika;
    private Role trenutnaRola;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Obustava", "Naziv", "Vrsta obustave", "Iznos", "Valuta", "Datum od", "Datum do");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idObustava", "naziv", "vrstaObustave.naziv", "iznos", "valuta.naziv", "datumOd", "datumDo");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), obustaveZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idObustava"));
    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        zaposlenikDetailsDto = t.getZaposlenikDetailsDto();
        sourcePrijavljenogZaposlenika = t.getSourcePrijavljenogZaposlenika();
        String naslov = sourcePrijavljenogZaposlenika.equals(SourcePrijavljenogZaposlenika.ZAPOSLENICI) ? String.format("Obustave zaposlenika: %s %s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()) : "Moje obustave";
        obustaveZaposlenikaPageableTableView.setTitle(naslov);
        trenutnaRola = EnumUtils.fromValue(AppUtils.getPrijavljeniZaposlenik().getTrenutnaRola().getIdRola(), Role.values(), "getId");
        intiPageableTableView();
        refreshPageableTableView(false);
    }

    private void intiPageableTableView() {
        TableUtils.addColumns(obustaveZaposlenikaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> obustaveZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new ObustavaDto())));
        obustaveZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        obustaveZaposlenikaPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        obustaveZaposlenikaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> obustaveZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes(root, FxmlView.ZAPOSLENICI)));
        obustaveZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        obustaveZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(obustavaPage, pageIndex, () -> refreshPageableTableView(true)));
        obustaveZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(obustavaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(ObustavaDto obustavaDto) {
        ContextMenuItemListBuilder contextMenuItemListBuilder = new ContextMenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(obustavaDto)));
        contextMenuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(obustavaDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(obustavaDto)));
        return contextMenuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja obustava zaposlenika.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), obustaveZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            obustavaPage = !enableSearchAndSort ? obustavaService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : obustavaService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(obustaveZaposlenikaPageableTableView, obustavaPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(ObustavaDto obustavaDto) {
        obustavaDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediObustavuZaposlenikaController dodajUrediObustavuZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_OBUSTAVU_ZAPOSLENIKA, obustavaDto);
        if (dodajUrediObustavuZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(ObustavaDto obustavaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji obustave zaposlenika", obustavaDto, propertiesInfo);
    }

    private void onIzbrisiClick(ObustavaDto obustavaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu obustavu zaposlenika?", obustavaDto, propertiesInfo, "Obustava zaposlenika je uspješno izbrisana.", "Desila se greška prilikom brisanja obustave zaposlenika.", () -> {
            obustavaService.delete(obustavaDto.getIdObustava());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Obustava", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Obustave_Zaposlenika", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()), f -> () -> GenericBeanReportPdf.create(String.format("OBUSTAVE ZAPOSLENIKA: %s %s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime().toUpperCase()), obustavaService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
