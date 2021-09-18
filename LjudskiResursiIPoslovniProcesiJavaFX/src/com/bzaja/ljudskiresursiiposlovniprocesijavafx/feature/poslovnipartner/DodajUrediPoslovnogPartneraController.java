/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.poslovnipartner;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
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
public class DodajUrediPoslovnogPartneraController implements Initializable, ControllerInterface<PoslovniPartnerDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idPoslovniPartnerLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private TextField oibTextField;

    @FXML
    private Label oibErrorLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label emailErrrorLabel;

    @FXML
    private TextField brojTelefonaTextField;

    @FXML
    private Label brojTelefonaErrorLabel;

    @FXML
    private TextField ulicaTextField;

    @FXML
    private Label ulicaErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> gradoviSearchableComboBox;

    @FXML
    private Label gradErrorLabel;

    @Autowired
    private PoslovniPartnerService poslovniPartnerService;

    @Autowired
    private GradService gradService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idPoslovniPartnerLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(gradoviSearchableComboBox, "Desila se greška prilikom dohvaćanja gradova.", text -> gradService.findAll(text, "naziv"), "idGrad", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, oibErrorLabel, emailErrrorLabel, brojTelefonaErrorLabel, ulicaErrorLabel, gradErrorLabel);
    }

    @Override
    public void initData(PoslovniPartnerDto t) {
        if (NumberUtils.isPositive(t.getIdPoslovniPartner())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi poslovnog partnera");
            ControlUtils.setTextInLabel(idPoslovniPartnerLabel, t, "idPoslovniPartner");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setTextInTextField(oibTextField, t, "oib");
            ControlUtils.setTextInTextField(emailTextField, t, "email");
            ControlUtils.setTextInTextField(brojTelefonaTextField, t, "brojTelefona");
            ControlUtils.setTextInTextField(ulicaTextField, t, "ulica");
            ControlUtils.setSelectedComboBoxItem(gradoviSearchableComboBox, t, "grad.idGrad");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj poslovnog partnera");
        }
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            PoslovniPartnerDto poslovniPartnerDto = new PoslovniPartnerDto();
            poslovniPartnerDto.setIdPoslovniPartner(ControlUtils.getIntegerFromLabel(idPoslovniPartnerLabel, 0));
            poslovniPartnerDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            poslovniPartnerDto.setOib(ControlUtils.getTextFromTextField(oibTextField));
            poslovniPartnerDto.setEmail(ControlUtils.getTextFromTextField(emailTextField));
            poslovniPartnerDto.setBrojTelefona(ControlUtils.getTextFromTextField(brojTelefonaTextField));
            poslovniPartnerDto.setUlica(ControlUtils.getTextFromTextField(ulicaTextField));
            poslovniPartnerDto.setGrad(ControlUtils.getSelectedItemFromComboBox(gradoviSearchableComboBox, id -> gradService.findById(id)));

            Set<ConstraintViolation<PoslovniPartnerDto>> constraintViolations = validator.validate(poslovniPartnerDto);

            if (constraintViolations.isEmpty()) {
                poslovniPartnerService.save(poslovniPartnerDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Poslovni partner je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja poslovnog partnera.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<PoslovniPartnerDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("oib", constraintViolations, oibErrorLabel);
        FormUtils.showErrorMessage("email", constraintViolations, emailErrrorLabel);
        FormUtils.showErrorMessage("brojTelefona", constraintViolations, brojTelefonaErrorLabel);
        FormUtils.showErrorMessage("ulica", constraintViolations, ulicaErrorLabel);
        FormUtils.showErrorMessage("grad", constraintViolations, gradErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
