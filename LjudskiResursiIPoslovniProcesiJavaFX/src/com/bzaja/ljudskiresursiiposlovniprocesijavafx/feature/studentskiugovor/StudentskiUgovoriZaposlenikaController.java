/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.studentskiugovor;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.SourcePrijavljenogZaposlenika;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.ZaposlenikSourceResultDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.PaneManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Role;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorService;
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
import javafx.scene.layout.Pane;
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
public class StudentskiUgovoriZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<StudentskiUgovorDto> studentskiUgovoriZaposlenikaPageableTableView;

    @Autowired
    private StudentskiUgovorService studentskiUgovorService;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private PaneManager paneManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<StudentskiUgovorDto> studentskiUgovorPage;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private SourcePrijavljenogZaposlenika sourcePrijavljenogZaposlenika;
    private Role trenutnaRola;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Ugovor", "Radni odnos", "Radno mjesto", "Datum od", "Datum do", "Studentski posao cjenik", "Broj odrađenih sati", "Cijena po satu", "Dosad zarađeni iznos u ovoj godini", "Valuta", "Je obračunat");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idUgovor", "radniOdnos.naziv", "radnoMjesto.naziv", "datumOd", "datumDo", "studentskiPosaoCjenik.naziv", "brojOdradjenihSati", "cijenaPoSatu", "dosadZaradjeniIznosUOvojGodini", "valuta.naziv", "jeObracunat");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), studentskiUgovoriZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idUgovor"));
    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        zaposlenikDetailsDto = t.getZaposlenikDetailsDto();
        sourcePrijavljenogZaposlenika = t.getSourcePrijavljenogZaposlenika();
        String naslov = sourcePrijavljenogZaposlenika.equals(SourcePrijavljenogZaposlenika.ZAPOSLENICI) ? String.format("Studentski ugovori zaposlenika: %s %s", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()) : "Moji studentski ugovori";
        studentskiUgovoriZaposlenikaPageableTableView.setTitle(naslov);
        trenutnaRola = EnumUtils.fromValue(AppUtils.getPrijavljeniZaposlenik().getTrenutnaRola().getIdRola(), Role.values(), "getId");
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(studentskiUgovoriZaposlenikaPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> studentskiUgovoriZaposlenikaPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new StudentskiUgovorDto())));
        studentskiUgovoriZaposlenikaPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        studentskiUgovoriZaposlenikaPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        studentskiUgovoriZaposlenikaPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        AppUtils.runIfMatchesSourcePrijavljenogZaposlenika(sourcePrijavljenogZaposlenika, SourcePrijavljenogZaposlenika.ZAPOSLENICI, () -> studentskiUgovoriZaposlenikaPageableTableView.addMenuItem("Natrag na popis zaposlenika", (e) -> paneManager.switchPanes((Pane) NodeUtils.getTabPaneFromInsideNode(root).getParent(), FxmlView.ZAPOSLENICI)));
        studentskiUgovoriZaposlenikaPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        studentskiUgovoriZaposlenikaPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(studentskiUgovorPage, pageIndex, () -> refreshPageableTableView(true)));
        studentskiUgovoriZaposlenikaPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(studentskiUgovorPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(StudentskiUgovorDto studentskiUgovorDto) {
        ContextMenuItemListBuilder contextMenuItemListBuilder = new ContextMenuItemListBuilder();
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Uredi", () -> onDodajUrediClick(studentskiUgovorDto)));
        contextMenuItemListBuilder.addMenuItem("Detalji", () -> onDetaljiClick(studentskiUgovorDto));
        AppUtils.runIfMatchesRole(trenutnaRola, Role.ADMINISTRATOR, () -> contextMenuItemListBuilder.addMenuItem("Izbriši", () -> onIzbrisiClick(studentskiUgovorDto)));
        return contextMenuItemListBuilder.build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja studentskih ugovora zaposlenika.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), studentskiUgovoriZaposlenikaPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            studentskiUgovorPage = !enableSearchAndSort ? studentskiUgovorService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto.getPageable()) : studentskiUgovorService.findAll(zaposlenikDetailsDto.getIdZaposlenik(), queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(studentskiUgovoriZaposlenikaPageableTableView, studentskiUgovorPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(StudentskiUgovorDto studentskiUgovorDto) {
        studentskiUgovorDto.setZaposlenik(zaposlenikDetailsDto);
        DodajUrediStudentskiUgovorZaposlenikaController dodajUrediStudentskiUgovorZaposlenikaController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_STUDENTSKI_UGOVOR_ZAPOSLENIKA, studentskiUgovorDto);
        if (dodajUrediStudentskiUgovorZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(StudentskiUgovorDto studentskiUgovorDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji studentskog ugovora zaposlenika", studentskiUgovorDto, propertiesInfo);
    }

    private void onIzbrisiClick(StudentskiUgovorDto studentskiUgovorDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj studentski ugovor zaposlenika?", studentskiUgovorDto, propertiesInfo, "Studentski ugovor zaposlenika je uspješno izbrisan.", "Desila se greška prilikom brisanja studentskog ugovora zaposlenika.", () -> {
            studentskiUgovorService.delete(studentskiUgovorDto.getIdUgovor());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("StudentskiUgovor", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Studentski_Ugovori_Zaposlenika", zaposlenikDetailsDto.getIme(), zaposlenikDetailsDto.getPrezime()), f -> () -> GenericBeanReportPdf.create(String.format("STUDENTSKI UGOVORI ZAPOSLENIKA: %s $s", zaposlenikDetailsDto.getIme().toUpperCase(), zaposlenikDetailsDto.getPrezime()), studentskiUgovorService.findAll(zaposlenikDetailsDto.getIdZaposlenik()), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
