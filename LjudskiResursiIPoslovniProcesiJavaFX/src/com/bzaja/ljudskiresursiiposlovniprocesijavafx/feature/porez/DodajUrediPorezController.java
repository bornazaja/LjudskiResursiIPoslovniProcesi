/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.porez;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.porez.PorezService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaporeza.VrstaPorezaService;
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
public class DodajUrediPorezController implements Initializable, ControllerInterface<PorezDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idPorezLabel;

    @FXML
    private SearchableComboBox<ItemDto> vrstePorezaSearchableComboBox;

    @FXML
    private Label vrstaPorezaErrorLabel;

    @FXML
    private NumberField stopaNumberField;

    @FXML
    private Label stopaErrorLabel;

    @FXML
    private NumberField minOsnovicaNumberField;

    @FXML
    private Label minOsnovicaErrorLabel;

    @FXML
    private NumberField maxOsnovicaNumberField;

    @FXML
    private Label maxOsnovicaErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> drzaveSearchableComboBox;

    @FXML
    private Label drzavaErrorLabel;

    @Autowired
    private PorezService porezService;

    @Autowired
    private VrstaPorezaService vrstaPorezaService;

    @Autowired
    private DrzavaService drzavaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idPorezLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(vrstePorezaSearchableComboBox, "Desila se greška prilikom dohvaćanja vrste poreza.", text -> vrstaPorezaService.findAll(text, "naziv"), "idVrstaPoreza", "naziv");
        ControlUtils.setupSearchableComboBox(drzaveSearchableComboBox, "Desila se greška prilikom dohvaćanja država.", text -> drzavaService.findAll(text, "naziv"), "idDrzava", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(vrstaPorezaErrorLabel, stopaErrorLabel, minOsnovicaErrorLabel, maxOsnovicaErrorLabel, drzavaErrorLabel);
    }

    @Override
    public void initData(PorezDto t) {
        if (NumberUtils.isPositive(t.getIdPorez())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi porez");
            ControlUtils.setTextInLabel(idPorezLabel, t, "idPorez");
            ControlUtils.setSelectedComboBoxItem(vrstePorezaSearchableComboBox, t, "vrstaPoreza.idVrstaPoreza");
            ControlUtils.setTextInTextField(stopaNumberField, t, "stopa");
            ControlUtils.setTextInTextField(minOsnovicaNumberField, t, "minOsnovica");
            ControlUtils.setTextInTextField(maxOsnovicaNumberField, t, "maxOsnovica");
            ControlUtils.setSelectedComboBoxItem(drzaveSearchableComboBox, t, "drzava.idDrzava");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj porez");
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

            PorezDto porezDto = new PorezDto();
            porezDto.setIdPorez(ControlUtils.getIntegerFromLabel(idPorezLabel, 0));
            porezDto.setVrstaPoreza(ControlUtils.getSelectedItemFromComboBox(vrstePorezaSearchableComboBox, id -> vrstaPorezaService.findById(id)));
            porezDto.setStopa(ControlUtils.getDoubleFromTextField(stopaNumberField, null));
            porezDto.setMinOsnovica(ControlUtils.getDoubleFromTextField(minOsnovicaNumberField, null));
            porezDto.setMaxOsnovica(ControlUtils.getDoubleFromTextField(maxOsnovicaNumberField, null));
            porezDto.setDrzava(ControlUtils.getSelectedItemFromComboBox(drzaveSearchableComboBox, id -> drzavaService.findById(id)));

            Set<ConstraintViolation<PorezDto>> constraintViolations = validator.validate(porezDto);

            if (constraintViolations.isEmpty()) {
                porezService.save(porezDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Porez je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja poreza.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<PorezDto>> constraintViolations) {
        FormUtils.showErrorMessage("vrstaPoreza", constraintViolations, vrstaPorezaErrorLabel);
        FormUtils.showErrorMessage("stopa", constraintViolations, stopaErrorLabel);
        FormUtils.showErrorMessage("minOsnovica", constraintViolations, minOsnovicaErrorLabel);
        FormUtils.showErrorMessage("maxOsnovica", constraintViolations, maxOsnovicaErrorLabel);
        FormUtils.showErrorMessage("drzava", constraintViolations, drzavaErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
