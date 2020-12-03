/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.prijava;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.ItemDto;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prijava.PrijavaResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prijava.PrijavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.RolaService;
import com.bzaja.myjavafxlibrary.control.SearchableComboBox;
import com.bzaja.myjavalibrary.util.LanguageFormat;
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
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Controller
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
        ControlUtils.setupSearchableComboBox(roleSearchableComboBox, text -> rolaService.findAll(text, "naziv"), "idRola", "naziv", "Desila se greška prilikom dohvaćanja rola.", LanguageFormat.HR);
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
                stageManager.showPrimaryStage(NodeUtils.getStageFromNode(root), FxmlView.GLAVNI_IZBORNIK, prijavaResult.getPrijavljeniZaposlenikDto(), AppUtils.getCssPathables());
            } else {
                DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Prijava nije uspjela.");
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom prijave.");
        }
    }
}
