/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstaprekovremenograda;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda.VrstaPrekovremenogRadaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda.VrstaPrekovremenogRadaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
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
public class DodajUrediVrstuPrekovremenogRadaController implements Initializable, ControllerInterface<VrstaPrekovremenogRadaDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idVrstaPrekovremenogRada;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private NumberField koeficjentNumberField;

    @FXML
    private Label koeficjentErrorLabel;

    @Autowired
    private VrstaPrekovremenogRadaService vrstaPrekovremenogRadaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idVrstaPrekovremenogRada.setVisible(false);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, koeficjentErrorLabel);
    }

    @Override
    public void initData(VrstaPrekovremenogRadaDto t) {
        if (NumberUtils.isPositive(t.getIdVrstaPrekovremenogRada())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi vrstu prekovremenog rada");
            ControlUtils.setTextInLabel(idVrstaPrekovremenogRada, t, "idVrstaPrekovremenogRada");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setTextInTextField(koeficjentNumberField, t, "koeficjent");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj vrstu prekovremenog rada");
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

            VrstaPrekovremenogRadaDto vrstaPrekovremenogRadaDto = new VrstaPrekovremenogRadaDto();
            vrstaPrekovremenogRadaDto.setIdVrstaPrekovremenogRada(ControlUtils.getIntegerFromLabel(idVrstaPrekovremenogRada, 0));
            vrstaPrekovremenogRadaDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            vrstaPrekovremenogRadaDto.setKoeficjent(ControlUtils.getDoubleFromTextField(koeficjentNumberField, null));

            Set<ConstraintViolation<VrstaPrekovremenogRadaDto>> constraintViolations = validator.validate(vrstaPrekovremenogRadaDto);

            if (constraintViolations.isEmpty()) {
                vrstaPrekovremenogRadaService.save(vrstaPrekovremenogRadaDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Vrsta prekovremenog rada je uspješno spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja vrste prekovremenog rada.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<VrstaPrekovremenogRadaDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("koeficjent", constraintViolations, koeficjentErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
