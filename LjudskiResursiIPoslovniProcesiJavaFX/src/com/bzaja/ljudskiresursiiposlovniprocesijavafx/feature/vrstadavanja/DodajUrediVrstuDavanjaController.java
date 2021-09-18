/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstadavanja;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja.VrstaDavanjaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja.VrstaDavanjaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
import com.bzaja.myjavafxlibrary.control.NumberField;
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
public class DodajUrediVrstuDavanjaController implements Initializable, ControllerInterface<VrstaDavanjaDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idVrstaDavanjaLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private NumberField stopaNaPlacuNumberField;

    @FXML
    private Label stopaNaPlacuErrorLabel;

    @FXML
    private NumberField stopaIzPlaceNumberField;

    @FXML
    private Label stopaIzPlaceErrorLabel;

    @FXML
    private ClearableDatePicker vrijediDoClearableDatePicker;

    @FXML
    private Label vrijediDoErrorLabel;

    @Autowired
    private VrstaDavanjaService vrstaDavanjaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idVrstaDavanjaLabel.setVisible(false);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, stopaNaPlacuErrorLabel, stopaIzPlaceErrorLabel, vrijediDoErrorLabel);
    }

    @Override
    public void initData(VrstaDavanjaDto t) {
        if (NumberUtils.isPositive(t.getIdVrstaDavanja())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi vrstu davanja");
            ControlUtils.setTextInLabel(idVrstaDavanjaLabel, t, "idVrstaDavanja");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setTextInTextField(stopaNaPlacuNumberField, t, "stopaNaPlacu");
            ControlUtils.setTextInTextField(stopaIzPlaceNumberField, t, "stopaIzPlace");
            ControlUtils.setValueInClearableDatePicker(vrijediDoClearableDatePicker, t, "vrijediDo");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj vrstu davanja");
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

            VrstaDavanjaDto vrstaDavanjaDto = new VrstaDavanjaDto();
            vrstaDavanjaDto.setIdVrstaDavanja(ControlUtils.getIntegerFromLabel(idVrstaDavanjaLabel, 0));
            vrstaDavanjaDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            vrstaDavanjaDto.setStopaNaPlacu(ControlUtils.getDoubleFromTextField(stopaNaPlacuNumberField, null));
            vrstaDavanjaDto.setStopaIzPlace(ControlUtils.getDoubleFromTextField(stopaIzPlaceNumberField, null));
            vrstaDavanjaDto.setVrijediDo(ControlUtils.getValueFromClearableDatePicker(vrijediDoClearableDatePicker));

            Set<ConstraintViolation<VrstaDavanjaDto>> constraintViolations = validator.validate(vrstaDavanjaDto);

            if (constraintViolations.isEmpty()) {
                vrstaDavanjaService.save(vrstaDavanjaDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Vrsta davanja je uspješno spremljenja.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja vrste davanja.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<VrstaDavanjaDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("stopaNaPlacu", constraintViolations, stopaNaPlacuErrorLabel);
        FormUtils.showErrorMessage("stopaIzPlace", constraintViolations, stopaIzPlaceErrorLabel);
        FormUtils.showErrorMessage("vrijediDo", constraintViolations, vrijediDoErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
