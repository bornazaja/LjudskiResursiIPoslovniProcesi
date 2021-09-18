/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.olaksica;

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
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaService;
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
public class OlaksiceZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<OlaksicaDto> olaskiceZaposlenikaPageableTableView;

    @Autowired
    private OlaksicaService olaksicaService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<OlaksicaDto> olaksicaPage;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenika;
    private Role trenutnaRola;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Olakšica", "Vrsta olakšice", "Datum od", "Datum do");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idOlaksica", "vrstaOlaksice.naziv", "datumOd", "datumDo");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), olaskiceZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idOlaksica"));
    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        zaposlenikDetailsDto = t.getZaposlenikDetailsDto();
        sourcePrijavljenogZaposlenika = t.getSourcePrijavljenogZaposlenika();
        String naslov = t.getSourcePrijavljenogZaposlenika().equals(SourcePrijavljenogZaposlenika.ZAPOSLENICI) ? String.format("Olakšice zaposlenika: %s %s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()) : "Moje Olakšice";
        olaskiceZaposlenikaPageableTableView.setTitle(naslov);
        trenutnaRola = EnumUtils.fromValue(AppUtils.getPrijavljeniZaposlenik().getTrenutnaRola().getIdRola(), Role.values(), "getId");
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(olaskiceZaposlenikaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> olaskiceZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new OlaksicaDto())));
        olaskiceZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        olaskiceZaposlenikaPageableTableView.addMenuItem("Pretrazivanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        olaskiceZaposlenikaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> olaskiceZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes(root, FxmlView.ZAPOSLENICI)));
        olaskiceZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        olaskiceZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(olaksicaPage, pageIndex, () -> refreshPageableTableView(true)));
        olaskiceZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(olaksicaPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(OlaksicaDto olaksicaDto) {
        ContextMenuItemListBuilder contextMenuItemListBuilder = new ContextMenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(olaksicaDto)));
        contextMenuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(olaksicaDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(olaksicaDto)));
        return contextMenuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja olakšica zaposlenika.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), olaskiceZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            olaksicaPage = !enableSearchAndSort ? olaksicaService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : olaksicaService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(olaskiceZaposlenikaPageableTableView, olaksicaPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(OlaksicaDto olaksicaDto) {
        olaksicaDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediOlaksicuZaposlenikaController dodajUrediOlaksicuZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_OLAKSICU_ZAPOSLENIKA, olaksicaDto);
        if (dodajUrediOlaksicuZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(OlaksicaDto olaksicaDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji olaksice zaposlenika", olaksicaDto, propertiesInfo);
    }

    private void onIzbrisiClick(OlaksicaDto olaksicaDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovu olakšicu zaposlenika?", olaksicaDto, propertiesInfo, "Olakšica zaposlenika je uspješno izbrisana.", "Desila se greška prilikom brisanja olakšice zaposlenika.", () -> {
            olaksicaService.delete(olaksicaDto.getIdOlaksica());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Olaksica", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Olaksice_Zaposlenika", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()), f -> () -> GenericBeanReportPdf.create(String.format("OLAKŠICE ZAPOSLENIKA: %s %s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime().toUpperCase()), olaksicaService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
