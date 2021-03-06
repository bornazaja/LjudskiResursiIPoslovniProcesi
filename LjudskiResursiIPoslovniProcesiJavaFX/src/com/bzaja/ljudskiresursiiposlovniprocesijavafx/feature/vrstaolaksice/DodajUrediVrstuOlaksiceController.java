/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstaolaksice;

import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FormUtils;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice.VrstaOlaksiceDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice.VrstaOlaksiceService;
import com.bzaja.myjavalibrary.util.NumberUtils;
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
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Controller
public class DodajUrediVrstuOlaksiceController implements Initializable, ControllerInterface<VrstaOlaksiceDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idVrstaOlaksiceLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private NumberField koeficjentNumberField;

    @FXML
    private Label koeficjentErrorLabel;

    @FXML
    private ClearableDatePicker vrijediDoClearableDatePicker;

    @FXML
    private Label vrijediDoErrorLabel;

    @Autowired
    private VrstaOlaksiceService vrstaOlaksiceService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idVrstaOlaksiceLabel.setVisible(false);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, koeficjentErrorLabel, vrijediDoErrorLabel);
    }

    @Override
    public void initData(VrstaOlaksiceDto t) {
        if (NumberUtils.isPositive(t.getIdVrstaOlaksice())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi vrstu olak??ice");
            ControlUtils.setTextInLabel(idVrstaOlaksiceLabel, t, "idVrstaOlaksice");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setTextInTextField(koeficjentNumberField, t, "koeficjent");
            ControlUtils.setValueInClearableDatePicker(vrijediDoClearableDatePicker, t, "vrijediDo");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj vrstu olak??ice");
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

            VrstaOlaksiceDto vrstaOlaksiceDto = new VrstaOlaksiceDto();
            vrstaOlaksiceDto.setIdVrstaOlaksice(ControlUtils.getIntegerFromLabel(idVrstaOlaksiceLabel, 0));
            vrstaOlaksiceDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            vrstaOlaksiceDto.setKoeficjent(ControlUtils.getDoubleFromTextField(koeficjentNumberField, null));
            vrstaOlaksiceDto.setVrijediDo(ControlUtils.getValueFromClearableDatePicker(vrijediDoClearableDatePicker));

            Set<ConstraintViolation<VrstaOlaksiceDto>> constraintViolations = validator.validate(vrstaOlaksiceDto);

            if (constraintViolations.isEmpty()) {
                vrstaOlaksiceService.save(vrstaOlaksiceDto);
                NodeUtils.closeCurrentStageByNode(root);
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Vrsta olak??ice je uspje??no spremljena.");
                stageResult = StageResult.OK;
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Error", "Desila se gre??ka prilikom spremanja vrste olak??ice.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<VrstaOlaksiceDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("koeficjent", constraintViolations, koeficjentErrorLabel);
        FormUtils.showErrorMessage("vrijediDo", constraintViolations, vrijediDoErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
