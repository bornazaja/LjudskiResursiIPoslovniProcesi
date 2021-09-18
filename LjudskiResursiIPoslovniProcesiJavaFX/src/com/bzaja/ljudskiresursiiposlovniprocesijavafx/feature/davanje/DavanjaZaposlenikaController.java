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
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.PaneManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeService;
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
        TableUtils.addColumns(davanjaZaposlenikaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> davanjaZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new DavanjeDto())));
        davanjaZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        davanjaZaposlenikaPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        davanjaZaposlenikaPageableTableView.addMenuItem("Eksporitraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> davanjaZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes(root, FxmlView.ZAPOSLENICI)));
        davanjaZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        davanjaZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(davanjePage, pageIndex, () -> refreshPageableTableView(true)));
        davanjaZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(davanjePage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(DavanjeDto davanjeDto) {
        ContextMenuItemListBuilder contextMenuItemListBuilder = new ContextMenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(davanjeDto)));
        contextMenuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(davanjeDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(davanjeDto)));
        return contextMenuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja davanja zaposlenika.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), davanjaZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            davanjePage = !enableSearchAndSort ? davanjeService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : davanjeService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(davanjaZaposlenikaPageableTableView, davanjePage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(DavanjeDto davanjeDto) {
        davanjeDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediDavanjeZaposlenikaController dodajUrediDavanjeZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_DAVANJE_ZAPOSLENIKA, davanjeDto);
        if (dodajUrediDavanjeZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(DavanjeDto davanjeDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji davanja zaposlenika", davanjeDto, propertiesInfo);
    }

    private void onIzbrisiClick(DavanjeDto davanjeDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da šelite izbrisati ovo davanje zaposlenika?", davanjeDto, propertiesInfo, "Davanje zaposlenika je uspješno izbrisano.", "Desila se greška prilikom brisanja davnaja zaposlenika.", () -> {
            davanjeService.delete(davanjeDto.getIdDavanje());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Davanje", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Davanja_Zaposlenika", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()), f -> () -> GenericBeanReportPdf.create(String.format("DAVANJA ZAPOSLENIKA: %s %s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime().toUpperCase()), davanjeService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
