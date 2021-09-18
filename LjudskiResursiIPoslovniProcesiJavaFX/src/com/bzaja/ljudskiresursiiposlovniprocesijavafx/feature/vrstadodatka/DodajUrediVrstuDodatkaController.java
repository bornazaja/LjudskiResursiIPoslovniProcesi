/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstadodatka;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka.VrstaDodatkaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka.VrstaDodatkaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
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
public class DodajUrediVrstuDodatkaController implements Initializable, ControllerInterface<VrstaDodatkaDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idVrstaDodatkaLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @Autowired
    private VrstaDodatkaService vrstaDodatkaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idVrstaDodatkaLabel.setVisible(false);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel);
    }

    @Override
    public void initData(VrstaDodatkaDto t) {
        if (NumberUtils.isPositive(t.getIdVrstaDodatka())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi vrstu dodatka");
            ControlUtils.setTextInLabel(idVrstaDodatkaLabel, t, "idVrstaDodatka");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj vrstu dodatka");
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

            VrstaDodatkaDto vrstaDodatkaDto = new VrstaDodatkaDto();
            vrstaDodatkaDto.setIdVrstaDodatka(ControlUtils.getIntegerFromLabel(idVrstaDodatkaLabel, 0));
            vrstaDodatkaDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));

            Set<ConstraintViolation<VrstaDodatkaDto>> constraintViolations = validator.validate(vrstaDodatkaDto);

            if (constraintViolations.isEmpty()) {
                vrstaDodatkaService.save(vrstaDodatkaDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Vrsta dodatka je uspješno spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja vrste dodatka.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<VrstaDodatkaDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
