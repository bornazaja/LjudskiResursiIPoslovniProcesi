/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.drzava;

import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FormUtils;
import com.bzaja.myjavafxlibrary.util.ItemDto;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.drzava.DrzavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.myjavalibrary.util.NumberUtils;
import com.bzaja.myjavafxlibrary.control.SearchableComboBox;
import com.bzaja.myjavalibrary.util.LanguageFormat;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
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
public class DodajUrediDrzavuController implements Initializable, ControllerInterface<DrzavaDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idDrzavaLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> valuteSearchableComboBox;

    @FXML
    private Label valutaErrorLabel;

    @FXML
    private CheckBox jeDomovinaCheckBox;

    @Autowired
    private DrzavaService drzavaService;

    @Autowired
    private ValutaService valutaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idDrzavaLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(valuteSearchableComboBox, text -> valutaService.findAll(text, "naziv"), "idValuta", "naziv", "Desila se gre??ka prilikom dohva??anja valuta.", LanguageFormat.HR);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, valutaErrorLabel);
    }

    @Override
    public void initData(DrzavaDto t) {
        if (NumberUtils.isPositive(t.getIdDrzava())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi dr??avu");
            ControlUtils.setTextInLabel(idDrzavaLabel, t, "idDrzava");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setSelectedComboBoxItem(valuteSearchableComboBox, t, "valuta.idValuta");
            ControlUtils.setValueInCheckBox(jeDomovinaCheckBox, t, "jeDomovina");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj dr??avu");
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

            DrzavaDto drzavaDto = new DrzavaDto();
            drzavaDto.setIdDrzava(ControlUtils.getIntegerFromLabel(idDrzavaLabel, 0));
            drzavaDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            drzavaDto.setValuta(ControlUtils.getSelectedItemFromComboBox(valuteSearchableComboBox, id -> valutaService.findById(id)));
            drzavaDto.setJeDomovina(ControlUtils.getValueFromCheckBox(jeDomovinaCheckBox));

            Set<ConstraintViolation<DrzavaDto>> constraintViolations = validator.validate(drzavaDto);

            if (constraintViolations.isEmpty()) {
                drzavaService.save(drzavaDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Dr??ava je uspje??no spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Gre??ka", "Desila se gre??ka priikom spremanja dr??ave.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<DrzavaDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("valuta", constraintViolations, valutaErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
