/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.vrstaobustave;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave.VrstaObustaveDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave.VrstaObustaveService;
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
public class DodajUrediVrstuObustaveController implements Initializable, ControllerInterface<VrstaObustaveDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idVrstaObustaveLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @Autowired
    private VrstaObustaveService vrstaObustaveService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idVrstaObustaveLabel.setVisible(false);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel);
    }

    @Override
    public void initData(VrstaObustaveDto t) {
        if (NumberUtils.isPositive(t.getIdVrstaObustave())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi vrstu obustave");
            ControlUtils.setTextInLabel(idVrstaObustaveLabel, t, "idVrstaObustave");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj vrstu obustave");
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

            VrstaObustaveDto vrstaObustaveDto = new VrstaObustaveDto();
            vrstaObustaveDto.setIdVrstaObustave(ControlUtils.getIntegerFromLabel(idVrstaObustaveLabel, 0));
            vrstaObustaveDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));

            Set<ConstraintViolation<VrstaObustaveDto>> constraintViolations = validator.validate(vrstaObustaveDto);

            if (constraintViolations.isEmpty()) {
                vrstaObustaveService.save(vrstaObustaveDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Vrsta obustave je uspješno spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja vrste obustave.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<VrstaObustaveDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
