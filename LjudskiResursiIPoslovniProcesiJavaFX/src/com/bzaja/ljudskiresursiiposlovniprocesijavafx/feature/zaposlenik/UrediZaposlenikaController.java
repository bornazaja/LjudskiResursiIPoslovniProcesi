/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.spol.SpolService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.EditZaposlenikDto;
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
public class UrediZaposlenikaController implements Initializable, ControllerInterface<EditZaposlenikDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label idZaposlenikLabel;

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

    private Boolean prijavljeniZaposlenik;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idZaposlenikLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(spoloviSearchableComboBox, "Desila se greška prilikom dohvaćabja spolova.", text -> spolService.findAll(text, "naziv"), "idSpol", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(imeErrorLabel, prezimeErrorLabel, spolErrorLabel, datumRodjenjaErrorLabel, oibErrorLabel, emailErrorLabel, brojTelefonaErrorLabel);
    }

    @Override
    public void initData(EditZaposlenikDto t) {
        ControlUtils.setTextInLabel(idZaposlenikLabel, t, "idZaposlenik");
        ControlUtils.setTextInTextField(imeTextField, t, "ime");
        ControlUtils.setTextInTextField(prezimeTextField, t, "prezime");
        ControlUtils.setSelectedComboBoxItem(spoloviSearchableComboBox, t, "spol.idSpol");
        ControlUtils.setValueInClearableDatePicker(datumRodjenjaClearableDatePicker, t, "datumRodjenja");
        ControlUtils.setTextInTextField(oibTextField, t, "oib");
        ControlUtils.setTextInTextField(emailTextField, t, "email");
        ControlUtils.setTextInTextField(brojTelefonaTextField, t, "brojTelefona");
        prijavljeniZaposlenik = AppUtils.isPrijavljeniZaposlenik(ControlUtils.getIntegerFromLabel(idZaposlenikLabel, 0));
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            EditZaposlenikDto editZaposlenikDto = new EditZaposlenikDto();
            editZaposlenikDto.setIdZaposlenik(ControlUtils.getIntegerFromLabel(idZaposlenikLabel, 0));
            editZaposlenikDto.setIme(ControlUtils.getTextFromTextField(imeTextField));
            editZaposlenikDto.setPrezime(ControlUtils.getTextFromTextField(prezimeTextField));
            editZaposlenikDto.setSpol(ControlUtils.getSelectedItemFromComboBox(spoloviSearchableComboBox, id -> spolService.findById(id)));
            editZaposlenikDto.setDatumRodjenja(ControlUtils.getValueFromClearableDatePicker(datumRodjenjaClearableDatePicker));
            editZaposlenikDto.setOib(ControlUtils.getTextFromTextField(oibTextField));
            editZaposlenikDto.setEmail(ControlUtils.getTextFromTextField(emailTextField));
            editZaposlenikDto.setBrojTelefona(ControlUtils.getTextFromTextField(brojTelefonaTextField));

            Set<ConstraintViolation<EditZaposlenikDto>> constraintViolations = validator.validate(editZaposlenikDto);

            if (constraintViolations.isEmpty()) {
                zaposlenikService.update(editZaposlenikDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", getSuccessMessage());
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", getErrorMessage());
        }
    }

    private String getSuccessMessage() {
        return prijavljeniZaposlenik ? "Vaši podaci su uspješno uređeni." : "Zaposlenik je uspješno uređen.";
    }

    private String getErrorMessage() {
        return prijavljeniZaposlenik ? "Desila se greška prilikom uređivanja vaših podataka" : "Desila se greška prilikom uređivanja zaposlenika.";
    }

    private void showErrorMessages(Set<ConstraintViolation<EditZaposlenikDto>> constraintViolations) {
        FormUtils.showErrorMessage("ime", constraintViolations, imeErrorLabel);
        FormUtils.showErrorMessage("prezime", constraintViolations, prezimeErrorLabel);
        FormUtils.showErrorMessage("spol", constraintViolations, spolErrorLabel);
        FormUtils.showErrorMessage("datumRodjenja", constraintViolations, datumRodjenjaErrorLabel);
        FormUtils.showErrorMessage("oib", constraintViolations, oibErrorLabel);
        FormUtils.showErrorMessage("email", constraintViolations, emailErrorLabel);
        FormUtils.showErrorMessage("brojTelefona", constraintViolations, brojTelefonaErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
