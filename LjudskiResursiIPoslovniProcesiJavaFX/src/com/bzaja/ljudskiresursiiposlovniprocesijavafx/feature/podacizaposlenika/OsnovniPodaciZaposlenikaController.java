/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.podacizaposlenika;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.PromjenaLozinkeZaposlenikaController;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.UrediZaposlenikaController;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.Role;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.EditZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.PrijavljeniZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.EnumUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.LocalDatePattern;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.LocalDateUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.ObjectMapperUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
public class OsnovniPodaciZaposlenikaController implements Initializable, ControllerInterface<PrijavljeniZaposlenikDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private Label imeLabel;

    @FXML
    private Label prezimeLabel;

    @FXML
    private Label spolLabel;

    @FXML
    private Label datumRodjenjaLabel;

    @FXML
    private Label oibLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label brojTelefonaLabel;

    @FXML
    private Label trenutnaRolaLabel;

    @FXML
    private Button urediButton;

    @FXML
    private Button promjeniLozinkuButton;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private ZaposlenikService zaposlenikService;

    private PrijavljeniZaposlenikDto prijavljeniZaposlenikDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initData(PrijavljeniZaposlenikDto t) {
        prijavljeniZaposlenikDto = t;
        Role trenutnaRola = EnumUtils.fromValue(t.getTrenutnaRola().getIdRola(), Role.values(), "getId");
        refreshZaposlenikData(t.getZaposlenik());
        ControlUtils.setTextInLabel(trenutnaRolaLabel, t.getTrenutnaRola().getNaziv());
        AppUtils.runIfMatchesRole(trenutnaRola, Role.OSOBLJE, () -> {
            urediButton.setVisible(false);
            promjeniLozinkuButton.setVisible(false);
        });
    }

    @FXML
    private void onPromjeniLozinkuClick(ActionEvent event) {
        PromjenaLozinkeZaposlenikaController promjenaLozinkeZaposlenikaController = stageManager.showSecondaryStage(FxmlView.PROMJENA_LOZINKE_ZAPOSLENIKA, prijavljeniZaposlenikDto.getZaposlenik().getIdZaposlenik());
    }

    @FXML
    private void onUrediClick(ActionEvent event) {
        EditZaposlenikDto editZaposlenikDto = ObjectMapperUtils.map(prijavljeniZaposlenikDto.getZaposlenik(), EditZaposlenikDto.class);
        UrediZaposlenikaController urediZaposlenikaController = stageManager.showSecondaryStage(FxmlView.UREDI_ZAPOSLENIKA, editZaposlenikDto);
        if (urediZaposlenikaController.getStageResult().equals(StageResult.OK)) {
            AppUtils.runWithTryCatch("Desila se greška prilikom dohvaćanja zaposlenika.", () -> refreshZaposlenikData(zaposlenikService.findById(editZaposlenikDto.getIdZaposlenik())));
        }
    }

    private void refreshZaposlenikData(ZaposlenikDetailsDto zaposlenikDetailsDto) {
        ControlUtils.setTextInLabel(imeLabel, zaposlenikDetailsDto.getIme());
        ControlUtils.setTextInLabel(prezimeLabel, zaposlenikDetailsDto.getPrezime());
        ControlUtils.setTextInLabel(spolLabel, zaposlenikDetailsDto.getSpol().getNaziv());
        ControlUtils.setTextInLabel(datumRodjenjaLabel, LocalDateUtils.format(zaposlenikDetailsDto.getDatumRodjenja(), LocalDatePattern.HR));
        ControlUtils.setTextInLabel(oibLabel, zaposlenikDetailsDto.getOib());
        ControlUtils.setTextInLabel(emailLabel, zaposlenikDetailsDto.getEmail());
        ControlUtils.setTextInLabel(brojTelefonaLabel, zaposlenikDetailsDto.getBrojTelefona());
    }
}
