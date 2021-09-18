/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.prijava;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prijava.PrijavaResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prijava.PrijavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.RolaService;
import com.bzaja.myjavafxlibrary.control.SearchableComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
public class PrijavaController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField lozinkaPasswordField;

    @FXML
    private SearchableComboBox<ItemDto> roleSearchableComboBox;

    @Autowired
    private PrijavaService prijavaService;

    @Autowired
    private RolaService rolaService;

    @Autowired
    private StageManager stageManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ControlUtils.setupSearchableComboBox(roleSearchableComboBox, "Desila se greška prilikom dohvaćanja rola.", text -> rolaService.findAll(text, "naziv"), "idRola", "naziv");
    }

    @FXML
    private void onPrijavaClick(ActionEvent event) {
        try {
            String email = ControlUtils.getTextFromTextField(emailTextField);
            String lozinka = ControlUtils.getTextFromTextField(lozinkaPasswordField);
            Integer idRola = ControlUtils.getSelectedItemKeyFromComboBoxToInteger(roleSearchableComboBox);

            PrijavaResult prijavaResult = prijavaService.pokusajPrijave(email, lozinka, idRola);
            if (prijavaResult.getJePrijavaUspjela()) {
                AppUtils.setPrijavljeniZapolsenik(prijavaResult.getPrijavljeniZaposlenikDto());
                stageManager.showPrimaryStage(NodeUtils.getStageFromNode(root), FxmlView.GLAVNI_IZBORNIK, prijavaResult.getPrijavljeniZaposlenikDto());
            } else {
                DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Prijava nije uspjela.");
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom prijave.");
        }
    }
}
