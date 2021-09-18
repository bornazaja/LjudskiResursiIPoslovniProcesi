/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.prebivaliste;

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
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.PrebivalisteDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.PrebivalisteService;
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
public class PrebivalistaZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<PrebivalisteDto> prebivalistaZaposlenikaPageableTableView;

    @Autowired
    private PrebivalisteService prebivalisteService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<PrebivalisteDto> prebivalistePage;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenika;
    private Role trenutnaRola;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Prebivalište", "Ulica", "Grad", "Datum od");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idPrebivaliste", "ulica", "grad.naziv", "datumOd");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), prebivalistaZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idPrebivaliste"));
    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        zaposlenikDetailsDto = t.getZaposlenikDetailsDto();
        sourcePrijavljenogZaposlenika = t.getSourcePrijavljenogZaposlenika();
        String naslov = sourcePrijavljenogZaposlenika.equals(SourcePrijavljenogZaposlenika.ZAPOSLENICI) ? String.format("Prebivališta zaposlenika: %s %s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()) : "Moja prebivališta";
        prebivalistaZaposlenikaPageableTableView.setTitle(naslov);
        trenutnaRola = EnumUtils.fromValue(AppUtils.getPrijavljeniZaposlenik().getTrenutnaRola().getIdRola(), Role.values(), "getId");
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(prebivalistaZaposlenikaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> prebivalistaZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new PrebivalisteDto())));
        prebivalistaZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        prebivalistaZaposlenikaPageableTableView.addMenuItem("Pretraživanje i sortrianje", (e) -> onPretrazivanjeISortiranjeClick());
        prebivalistaZaposlenikaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> prebivalistaZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes(root, FxmlView.ZAPOSLENICI)));
        prebivalistaZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        prebivalistaZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(prebivalistePage, pageIndex, () -> refreshPageableTableView(true)));
        prebivalistaZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(prebivalistePage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(PrebivalisteDto prebivalisteDto) {
        ContextMenuItemListBuilder contextMenuItemListBuilder = new ContextMenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(prebivalisteDto)));
        contextMenuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(prebivalisteDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(prebivalisteDto)));
        return contextMenuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohavćanja prebivališta zaposlenika.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), prebivalistaZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            prebivalistePage = !enableSearchAndSort ? prebivalisteService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : prebivalisteService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(prebivalistaZaposlenikaPageableTableView, prebivalistePage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(PrebivalisteDto prebivalisteDto) {
        prebivalisteDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediPrebivalisteZaposlenikaController dodajUrediPrebivalisteZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_PREBIVALISTE_ZAPSOLENIKA, prebivalisteDto);
        if (dodajUrediPrebivalisteZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(PrebivalisteDto prebivalisteDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji prebivališta zaposlenika", prebivalisteDto, propertiesInfo);
    }

    private void onIzbrisiClick(PrebivalisteDto prebivalisteDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovo prebivalište zaposlenika?", prebivalisteDto, propertiesInfo, "Prebivalište zaposlenika je upsješno izbrisano.", "Desila se greška prilikom brisanja zaposlenika.", () -> {
            prebivalisteService.delete(prebivalisteDto.getIdPrebivaliste());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Prebivaliste", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Prebivalista_Zaposlenika", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()), f -> () -> GenericBeanReportPdf.create(String.format("PREBIVALIŠTA ZAPOSLENIKA: %s %s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime().toUpperCase()), prebivalisteService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
