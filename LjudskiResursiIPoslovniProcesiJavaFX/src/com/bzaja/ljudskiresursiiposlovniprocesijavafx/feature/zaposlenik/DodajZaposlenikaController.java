/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol.SpolService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.AddZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikService;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
import com.bzaja.myjavafxlibrary.control.SearchableComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
public class DodajZaposlenikaController implements Initializable {

    @FXML
    private ScrollPane root;

    @FXML
    private TextField imeTextField;

    @FXML
    private Label imeErrorLabel;

    @FXML
    private TextField prezimeTextField;

    @FXML
    private Label prezimeErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> spoloviSearchableComboBox;

    @FXML
    private Label spolErrorLabel;

    @FXML
    private ClearableDatePicker datumRodjenjaClearableDatePicker;

    @FXML
    private Label datumRodjenjaErrorLabel;

    @FXML
    private TextField oibTextField;

    @FXML
    private Label oibErrorLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private PasswordField lozinkaPasswordField;

    @FXML
    private Label lozinkaErrorLabel;

    @FXML
    private PasswordField ponoviLozinkuPasswordField;

    @FXML
    private Label ponoviLozinkuErrorLabel;

    @FXML
    private TextField brojTelefonaTextField;

    @FXML
    private Label brojTelefonaErrorLabel;

    @Autowired
    private ZaposlenikService zaposlenikService;

    @Autowired
    private SpolService spolService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        ControlUtils.setupSearchableComboBox(spoloviSearchableComboBox, "Desila se greška prilikom dohvaćanja spolova.", text -> spolService.findAll(text, "naziv"), "idSpol", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(imeErrorLabel, prezimeErrorLabel, spolErrorLabel, datumRodjenjaErrorLabel, oibErrorLabel, emailErrorLabel, lozinkaErrorLabel, ponoviLozinkuErrorLabel, brojTelefonaErrorLabel);
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            AddZaposlenikDto addZaposlenikDto = new AddZaposlenikDto();
            addZaposlenikDto.setIme(ControlUtils.getTextFromTextField(imeTextField));
            addZaposlenikDto.setPrezime(ControlUtils.getTextFromTextField(prezimeTextField));
            addZaposlenikDto.setSpol(ControlUtils.getSelectedItemFromComboBox(spoloviSearchableComboBox, id -> spolService.findById(id)));
            addZaposlenikDto.setDatumRodjenja(ControlUtils.getValueFromClearableDatePicker(datumRodjenjaClearableDatePicker));
            addZaposlenikDto.setOib(ControlUtils.getTextFromTextField(oibTextField));
            addZaposlenikDto.setEmail(ControlUtils.getTextFromTextField(emailTextField));
            addZaposlenikDto.setLozinka(ControlUtils.getTextFromTextField(lozinkaPasswordField));
            addZaposlenikDto.setPonoviLozinku(ControlUtils.getTextFromTextField(ponoviLozinkuPasswordField));
            addZaposlenikDto.setBrojTelefona(ControlUtils.getTextFromTextField(brojTelefonaTextField));

            Set<ConstraintViolation<AddZaposlenikDto>> constraintViolations = validator.validate(addZaposlenikDto);

            if (constraintViolations.isEmpty()) {
                zaposlenikService.insert(addZaposlenikDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Zaposlenik je uspješno dodan.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<AddZaposlenikDto>> constraintViolations) {
        FormUtils.showErrorMessage("ime", constraintViolations, imeErrorLabel);
        FormUtils.showErrorMessage("prezime", constraintViolations, prezimeErrorLabel);
        FormUtils.showErrorMessage("spol", constraintViolations, spolErrorLabel);
        FormUtils.showErrorMessage("datumRodjenja", constraintViolations, datumRodjenjaErrorLabel);
        FormUtils.showErrorMessage("oib", constraintViolations, oibErrorLabel);
        FormUtils.showErrorMessage("email", constraintViolations, emailErrorLabel);
        FormUtils.showErrorMessage("lozinka", constraintViolations, lozinkaErrorLabel);
        FormUtils.showErrorMessage("ponoviLozinku", constraintViolations, ponoviLozinkuErrorLabel);
        FormUtils.showErrorMessage("brojTelefona", constraintViolations, brojTelefonaErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
