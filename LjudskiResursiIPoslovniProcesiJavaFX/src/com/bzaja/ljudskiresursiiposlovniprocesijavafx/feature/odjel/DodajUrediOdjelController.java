/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.odjel;

import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FormUtils;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel.OdjelDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel.OdjelService;
import com.bzaja.myjavalibrary.util.NumberUtils;
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
public class DodajUrediOdjelController implements Initializable, ControllerInterface<OdjelDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idOdjelLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @Autowired
    private OdjelService odjelService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idOdjelLabel.setVisible(false);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel);
    }

    @Override
    public void initData(OdjelDto t) {
        if (NumberUtils.isPositive(t.getIdOdjel())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi odjel");
            ControlUtils.setTextInLabel(idOdjelLabel, t, "idOdjel");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj odjel");
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

            OdjelDto odjelDto = new OdjelDto();
            odjelDto.setIdOdjel(ControlUtils.getIntegerFromLabel(idOdjelLabel, 0));
            odjelDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));

            Set<ConstraintViolation<OdjelDto>> constraintViolations = validator.validate(odjelDto);

            if (constraintViolations.isEmpty()) {
                odjelService.save(odjelDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Odjel je uspje??no spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Gre??ka", "Desila se gre??ka prilikom spremanja odjela.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<OdjelDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
