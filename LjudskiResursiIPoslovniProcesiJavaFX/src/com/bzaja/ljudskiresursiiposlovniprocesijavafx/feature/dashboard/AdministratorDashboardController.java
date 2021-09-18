/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.dashboard;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovora.ObracunUgovoraService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel.OdjelService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.povijestprijava.PovijestPrijavaStatisticsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaStatisticsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovor.UgovorService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.LocalDatePattern;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.LocalDateUtils;
import com.bzaja.myjavafxlibrary.control.DashboardTile;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
public class AdministratorDashboardController implements Initializable, ControllerInterface<ZaposlenikDetailsDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private DashboardTile prijaveDashoardTile;

    @FXML
    private DashboardTile zaposleniciDashboardTile;

    @FXML
    private DashboardTile trenutniProfitDashboardTile;

    @FXML
    private DashboardTile obracuniDashboardTile;

    @FXML
    private DashboardTile odjeliDashboardTile;

    @FXML
    private DashboardTile radnaMjestaDashboardTile;

    @FXML
    private DashboardTile poslovniPartneriDasboardTile;

    @FXML
    private DashboardTile transkacijeDashboardTile;

    @FXML
    private PieChart transakcijePieChart;

    @FXML
    private BarChart<String, Long> povijestPrijavaBarChart;

    @FXML
    private TableView<PovijestPrijavaDto> povijestPrijavaTableView;

    @FXML
    private TableColumn<PovijestPrijavaDto, String> idPovijestPrijavaTableColumn;

    @FXML
    private TableColumn<PovijestPrijavaDto, String> vrijemePrijaveTableColumn;

    @FXML
    private TableColumn<PovijestPrijavaDto, String> zaposlenikTableColumn;

    @FXML
    private TableColumn<PovijestPrijavaDto, String> rolaTableColumn;

    @FXML
    private DashboardTile mojaDavanjaDashboardTile;

    @FXML
    private DashboardTile mojiDodatciDashboardTile;

    @FXML
    private DashboardTile mojeObustaveDashboardTile;

    @FXML
    private DashboardTile mojeOlaksiceDashboardTile;

    @FXML
    private DashboardTile mojiPrekovremeniRadoviDashboardTile;

    @FXML
    private DashboardTile mojiUgovoriDashboardTile;

    @FXML
    private DashboardTile mojePrijaveDashboardTile;

    @FXML
    private DashboardTile placaDashboardTile;

    @Autowired
    private PovijestPrijavaService povijestPrijavaService;

    @Autowired
    private ZaposlenikService zaposlenikService;

    @Autowired
    private TransakcijaService transakcijaService;

    @Autowired
    private ObracunUgovoraService obracunUgovoraService;

    @Autowired
    private OdjelService odjelService;

    @Autowired
    private RadnoMjestoService radnoMjestoService;

    @Autowired
    private PoslovniPartnerService poslovniPartnerService;

    @Autowired
    private UgovorService ugovorService;

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

    private ZaposlenikDetailsDto zaposlenikDetailsDto;
    private Long brojTransakcija;
    private Long brojPoslovnihPartnera;
    private Long brojRadnihMjesta;
    private Long brojOdjela;
    private Long brojObracuna;
    private Double profit;
    private Long brojAktivnihZaposlenika;
    private Long brojPovjestPrijava;
    private List<PovijestPrijavaDto> povijestPrijavaList;
    private List<PovijestPrijavaStatisticsDto> povijestPrijavaStatisticsList;
    private List<TransakcijaStatisticsDto> transakcijaStatisticsList;
    private Long brojAktivnihPrekovremenihRadovaZaposlenika;
    private Long brojAktivnihOlaksicaZaposlenika;
    private Long brojAktivnihObustavaZaposlenika;
    private Long brojAktivnihDodatakaZaposlenika;
    private Long brojAkitvnihDavanjaZaposlenika;
    private Long brojUgovoraZaposlenika;
    private Long brojPrijavaZaposlenika;
    private Double placa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void initData(ZaposlenikDetailsDto t) {
        zaposlenikDetailsDto = t;
        initPovijestPrijavaTableView();
        refreshControls();
    }

    private void initPovijestPrijavaTableView() {
        TableUtils.setupPropertyColumn(idPovijestPrijavaTableColumn, "idPovijestPrijava");
        TableUtils.setupPropertyColumn(vrijemePrijaveTableColumn, "vrijemePrijave");
        TableUtils.setupPropertyColumn(zaposlenikTableColumn, "zaposlenik.ime+zaposlenik.prezime");
        TableUtils.setupPropertyColumn(rolaTableColumn, "rola.naziv");
    }

    private void refreshControls() {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja podataka.", () -> {
            fetchData();
            Platform.runLater(() -> refreshData());
        });
    }

    private void fetchData() {
        placa = ugovorService.getPlacaByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojPovjestPrijava = povijestPrijavaService.count();
        brojAktivnihZaposlenika = zaposlenikService.countAktivneZaposlenike();
        profit = transakcijaService.getTrenutniProfit();
        brojObracuna = obracunUgovoraService.count();
        brojOdjela = odjelService.count();
        brojRadnihMjesta = radnoMjestoService.count();
        brojPoslovnihPartnera = poslovniPartnerService.count();
        brojTransakcija = transakcijaService.count();

        transakcijaStatisticsList = transakcijaService.findBrojTransakcijaPoVrstiUTrenutnojGodini();
        povijestPrijavaStatisticsList = povijestPrijavaService.findRecentDnevniBrojPrijava(10);
        povijestPrijavaList = povijestPrijavaService.findRecentPovijestPrijava(20);

        brojAkitvnihDavanjaZaposlenika = davanjeService.countAktivnaDavanjaByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojAktivnihDodatakaZaposlenika = dodatakService.countAkivneDodatkeByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojAktivnihObustavaZaposlenika = obustavaService.countAktivneObustaveByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojAktivnihOlaksicaZaposlenika = olaksicaService.countAktvneOlaksiceByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojAktivnihPrekovremenihRadovaZaposlenika = prekovremeniRadService.countAktivniPrekovremeniRadoviByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojUgovoraZaposlenika = ugovorService.countByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
        brojPrijavaZaposlenika = povijestPrijavaService.countByZaposlenikId(zaposlenikDetailsDto.getIdZaposlenik());
    }

    private void refreshData() {
        ControlUtils.setValueInDashboardTile(prijaveDashoardTile, brojPovjestPrijava);
        ControlUtils.setValueInDashboardTile(zaposleniciDashboardTile, brojAktivnihZaposlenika);
        ControlUtils.setValueInDashboardTile(trenutniProfitDashboardTile, profit);
        ControlUtils.setValueInDashboardTile(obracuniDashboardTile, brojObracuna);
        ControlUtils.setValueInDashboardTile(odjeliDashboardTile, brojOdjela);
        ControlUtils.setValueInDashboardTile(radnaMjestaDashboardTile, brojRadnihMjesta);
        ControlUtils.setValueInDashboardTile(poslovniPartneriDasboardTile, brojPoslovnihPartnera);
        ControlUtils.setValueInDashboardTile(transkacijeDashboardTile, brojTransakcija);

        List<PieChart.Data> pieChartDataList = transakcijaStatisticsList.stream().map(x -> new PieChart.Data(String.format("%s (%d)", x.getVrstaTransakcije(), x.getBroj()), x.getBroj())).collect(Collectors.toList());
        transakcijePieChart.setData(FXCollections.observableList(pieChartDataList));

        List<BarChart.Data> barChartDataList = povijestPrijavaStatisticsList.stream().map(x -> new BarChart.Data(LocalDateUtils.format(x.getDatum(), LocalDatePattern.HR), x.getBroj())).collect(Collectors.toList());
        ObservableList<XYChart.Series<String, Long>> barChartDataObservableList = FXCollections.observableArrayList();
        BarChart.Series series = new XYChart.Series(FXCollections.observableList(barChartDataList));
        barChartDataObservableList.setAll(series);
        povijestPrijavaBarChart.setData(barChartDataObservableList);

        povijestPrijavaTableView.setItems(FXCollections.observableList(povijestPrijavaList));

        ControlUtils.setValueInDashboardTile(mojaDavanjaDashboardTile, brojAkitvnihDavanjaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojiDodatciDashboardTile, brojAktivnihDodatakaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojeObustaveDashboardTile, brojAktivnihObustavaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojeOlaksiceDashboardTile, brojAktivnihOlaksicaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojiPrekovremeniRadoviDashboardTile, brojAktivnihPrekovremenihRadovaZaposlenika);
        ControlUtils.setValueInDashboardTile(mojiUgovoriDashboardTile, brojUgovoraZaposlenika);
        ControlUtils.setValueInDashboardTile(mojePrijaveDashboardTile, brojPrijavaZaposlenika);
        ControlUtils.setValueInDashboardTile(placaDashboardTile, placa);
    }

    @FXML
    private void onOsvjeziClick(ActionEvent event) {
        refreshControls();
    }
}
