/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.podacizaposlenika;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.SourcePrijavljenogZaposlenika;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.ZaposlenikSourceResultDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FXMLManager;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.PrijavljeniZaposlenikDto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
public class OpsezniPodaciZaposlenikaController implements Initializable, ControllerInterface<PrijavljeniZaposlenikDto> {

    @FXML
    private Tab mojiOsobniPodaciTab;

    @FXML
    private Tab mojaDavanjaTab;

    @FXML
    private Tab mojeOlaksiceTab;

    @FXML
    private Tab mojiDodatciTab;

    @FXML
    private Tab mojeObustaveTab;

    @FXML
    private Tab mojaPrebivalistaTab;

    @FXML
    private Tab mojiPrekovremeniRadoviTab;

    @FXML
    private Tab mojeRoleTab;

    @FXML
    private Tab mojiUgovoriTab;

    @FXML
    private Tab mojeIsplatneListeTab;

    @Autowired
    private FXMLManager fXMLManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initData(PrijavljeniZaposlenikDto t) {
        mojiOsobniPodaciTab.setOnSelectionChanged((e) -> mojiOsobniPodaciTab.setContent(fXMLManager.load(FxmlView.OSNOVNI_PODACI_ZAPOSLENIKA, t)));
        mojaDavanjaTab.setOnSelectionChanged((e) -> mojaDavanjaTab.setContent(fXMLManager.load(FxmlView.DAVANJA_ZAPOSLENIKA, new ZaposlenikSourceResultDto(t.getZaposlenik(), SourcePrijavljenogZaposlenika.OSOBNI_PODACI))));
        mojeOlaksiceTab.setOnSelectionChanged((e) -> mojeOlaksiceTab.setContent(fXMLManager.load(FxmlView.OLAKSICE_ZAPOSLENIKA, new ZaposlenikSourceResultDto(t.getZaposlenik(), SourcePrijavljenogZaposlenika.OSOBNI_PODACI))));
        mojiDodatciTab.setOnSelectionChanged((e) -> mojiDodatciTab.setContent(fXMLManager.load(FxmlView.DODATCI_ZAPOSLENIKA, new ZaposlenikSourceResultDto(t.getZaposlenik(), SourcePrijavljenogZaposlenika.OSOBNI_PODACI))));
        mojeObustaveTab.setOnSelectionChanged((e) -> mojeObustaveTab.setContent(fXMLManager.load(FxmlView.OBUSTAVE_ZAPOSLENIKA, new ZaposlenikSourceResultDto(t.getZaposlenik(), SourcePrijavljenogZaposlenika.OSOBNI_PODACI))));
        mojaPrebivalistaTab.setOnSelectionChanged((e) -> mojaPrebivalistaTab.setContent(fXMLManager.load(FxmlView.PREBIVALISTA_ZAPOSLENIKA, new ZaposlenikSourceResultDto(t.getZaposlenik(), SourcePrijavljenogZaposlenika.OSOBNI_PODACI))));
        mojiPrekovremeniRadoviTab.setOnSelectionChanged((e) -> mojiPrekovremeniRadoviTab.setContent(fXMLManager.load(FxmlView.PREKOVREMENI_RADOVI_ZAPOSLENIKA, new ZaposlenikSourceResultDto(t.getZaposlenik(), SourcePrijavljenogZaposlenika.OSOBNI_PODACI))));
        mojeRoleTab.setOnSelectionChanged((e) -> mojeRoleTab.setContent(fXMLManager.load(FxmlView.ROLE_ZAPOSLENIKA, new ZaposlenikSourceResultDto(t.getZaposlenik(), SourcePrijavljenogZaposlenika.OSOBNI_PODACI))));
        mojiUgovoriTab.setOnSelectionChanged((e) -> mojiUgovoriTab.setContent(fXMLManager.load(FxmlView.UGOVORI_ZAPOSLENIKA, new ZaposlenikSourceResultDto(t.getZaposlenik(), SourcePrijavljenogZaposlenika.OSOBNI_PODACI))));
        mojeIsplatneListeTab.setOnSelectionChanged((e) -> mojeIsplatneListeTab.setContent(fXMLManager.load(FxmlView.ISPLATNE_LISTE_ZAPOSLENIKA, new ZaposlenikSourceResultDto(t.getZaposlenik(), SourcePrijavljenogZaposlenika.OSOBNI_PODACI))));
        mojiOsobniPodaciTab.setContent(fXMLManager.load(FxmlView.OSNOVNI_PODACI_ZAPOSLENIKA, t));
    }

}
