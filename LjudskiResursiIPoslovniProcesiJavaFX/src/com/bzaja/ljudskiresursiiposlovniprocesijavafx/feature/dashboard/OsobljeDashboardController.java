/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.dashboard;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.UgovorService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.GenericBeanReportPdf;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.StringUtils;
import com.bzaja.myjavafxlibrary.control.DashboardTile;
import com.bzaja.myjavafxlibrary.control.PageableTableView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
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
public class OsobljeDashboardController implements Initializable, ControllerInterface<ZaposlenikDetailsDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private DashboardTile mojaDavanjaDashboardTile;

    @FXML
    private DashboardTile mojiDodatciDashboardTile;

    @FXML
    private DashboardTile mojeObustaveDashboardTile;

    @FXML
    private DashboardTile mojeOlaksiceDashboardTile;

    @FXML
    private PageableTableView<ZaposlenikDetailsDto> zaposleniciPageableTableView;

    @FXML
    private DashboardTile mojiPrekovremeniRadoviDashboardTile;

    @FXML
    private DashboardTile mojiUgovoriDashboardTile;

    @FXML
    private DashboardTile mojePrijaveDashboardTile;

    @FXML
    private DashboardTile mojaPlacaDashboardTile;

    @Autowired
    private ZaposlenikService zaposlenikService;

    @Autowired
    private DavanjeService davanjeService;

    @Autowired
    private DodatakService dodatakService;

    @Autowired
    private ObustavaService obustavaService;

    @Autowired
    private OlaksicaService olaksicaService;

    @Autowired
    private PrekovremeniRadService prekovremeniRadService;

    @Autowired
    private UgovorService ugovorService;

    @Autowired
    private PovijestPrijavaService povijestPrijavaService;

    @Autowired
    private StageManager stageManager;

    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private Long brojAktivnihDavanjaZaposlenika;
    private Long brojAktivnihDodatakaZaposlenika;
    private Long brojAktivnihObustavaZaposlenika;
    private Long brojAktivnihOlaksicaZaposlenika;
    private Long brojAktivnihPrekovremenihRadovaZaposlenika;
    private Long brojUgovoraZaposlenika;
    private Long brojPrijavaZaposlenika;
    private Double placa;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<ZaposlenikDetailsDto> zaposlenikPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("Ime", "Prezime", "Spol", "Email", "Broj telefona");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("ime", "prezime", "spol.naziv", "email", "brojTelefona");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), zaposleniciPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "prezime"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(zaposleniciPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        zaposleniciPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        zaposleniciPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        zaposleniciPageableTableView.addMenuItem("Exportiraj u PDF", (e) -> onExportirajUPdfClick());
        zaposleniciPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        zaposleniciPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(zaposlenikPage, pageIndex, () -> refreshPageableTableView(true)));
        zaposleniciPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(zaposlenikPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(ZaposlenikDetailsDto z) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Detalji", () -> onDetaljiClick(z))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja zaposlenika.", () -> {
            fetchZaposlenici(enableSearchAndSort);
            Platform.runLater(() -> TableUtils.refresh(zaposleniciPageableTableView, zaposlenikPage, pageIndex.get()));
        });
    }

    private void fetchZaposlenici(boolean enableSearchAndSort) {
        queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), zaposleniciPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
        zaposlenikPage = !enableSearchAndSort ? zaposlenikService.findAll(queryCriteriaDto.getPageable()) : zaposlenikService.findAll(queryCriteriaDto);
    }

    @Override
    public void initData(ZaposlenikDetailsDto t) {
        zaposlenikDetailsDto = t;
        refreshControls();
    }

    private void refreshControls() {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja podataka.", () -> {
            fetchData();
            Platform.runLater(() -> refreshData());
        });
    }

    private void fetchData() {
        brojAktivnihDavanjaZaposlenika = davanjeService.countAktivnaDavanjaByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojAktivnihDodatakaZaposlenika = dodatakService.countAkivneDodatkeByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojAktivnihObustavaZaposlenika = obustavaService.countAktivneObustaveByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojAktivnihOlaksicaZaposlenika = olaksicaService.countAktvneOlaksiceByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        fetchZaposlenici(false);
        brojAktivnihPrekovremenihRadovaZaposlenika = prekovremeniRadService.countAktivniPrekovremeniRadoviByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojUgovoraZaposlenika = ugovorService.countByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojPrijavaZaposlenika = povijestPrijavaService.countByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        placa = ugovorService.getPlacaByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
    }

    private void refreshData() {
        ControlUtils.setValueInDashboardTile(mojaDavanjaDashboardTile, brojAktivnihDavanjaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojiDodatciDashboardTile, brojAktivnihDodatakaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojeObustaveDashboardTile, brojAktivnihObustavaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojeOlaksiceDashboardTile, brojAktivnihOlaksicaZaposlenika);
        TableUtils.refresh(zaposleniciPageableTableView, zaposlenikPage, pageIndex.get());
        ControlUtils.setValueInDashboardTile(mojiPrekovremeniRadoviDashboardTile, brojAktivnihPrekovremenihRadovaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojiUgovoriDashboardTile, brojUgovoraZaposlenika);
        ControlUtils.setValueInDashboardTile(mojePrijaveDashboardTile, brojPrijavaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojaPlacaDashboardTile, placa);
    }

    private void onDetaljiClick(ZaposlenikDetailsDto zaposlenikDetailsDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji zaposlenika", zaposlenikDetailsDto, propertiesInfo);
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Zaposlenik", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onExportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Zaposlenici"), f -> () -> GenericBeanReportPdf.create("ZAPOSLENICI", zaposlenikService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }

    @FXML
    private void onOsvjeziClick(ActionEvent event) {
        refreshControls();
    }
}
