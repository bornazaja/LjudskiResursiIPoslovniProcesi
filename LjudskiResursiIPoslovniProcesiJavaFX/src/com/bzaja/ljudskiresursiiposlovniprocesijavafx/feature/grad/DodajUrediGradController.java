/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.grad;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import com.bzaja.myjavafxlibrary.control.NumberField;
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
public class DodajUrediGradController implements Initializable, ControllerInterface<GradDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idGradLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private NumberField prirezNumberField;

    @FXML
    private Label prirezErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> drzaveSearchableComboBox;

    @FXML
    private Label drzavaErrorLabel;

    @Autowired
    private GradService gradService;

    @Autowired
    private DrzavaService drzavaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idGradLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(drzaveSearchableComboBox, "Desila se greška prilikom dohvaćanja država.", text -> drzavaService.findAll(text, "naziv"), "idDrzava", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, prirezErrorLabel, drzavaErrorLabel);
    }

    @Override
    public void initData(GradDto t) {
        if (NumberUtils.isPositive(t.getIdGrad())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi grad");
            ControlUtils.setTextInLabel(idGradLabel, t, "idGrad");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setTextInTextField(prirezNumberField, t, "prirez");
            ControlUtils.setSelectedComboBoxItem(drzaveSearchableComboBox, t, "drzava.idDrzava");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj grad");
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

            GradDto gradDto = new GradDto();
            gradDto.setIdGrad(ControlUtils.getIntegerFromLabel(idGradLabel, 0));
            gradDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            gradDto.setPrirez(ControlUtils.getDoubleFromTextField(prirezNumberField, null));
            gradDto.setDrzava(ControlUtils.getSelectedItemFromComboBox(drzaveSearchableComboBox, id -> drzavaService.findById(id)));

            Set<ConstraintViolation<GradDto>> constraintViolations = validator.validate(gradDto);

            if (constraintViolations.isEmpty()) {
                gradService.save(gradDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Grad je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja grada.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<GradDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("prirez", constraintViolations, prirezErrorLabel);
        FormUtils.showErrorMessage("drzava", constraintViolations, drzavaErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
