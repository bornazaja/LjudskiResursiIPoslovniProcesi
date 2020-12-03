/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.radnomjesto;

import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FormUtils;
import com.bzaja.myjavafxlibrary.util.ItemDto;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.odjel.OdjelService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoService;
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
public class DodajUrediRadnoMjestoController implements Initializable, ControllerInterface<RadnoMjestoDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idRadnoMjestoLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> odjeliSearchableComboBox;

    @FXML
    private Label odjelErrorLabel;

    @Autowired
    private RadnoMjestoService radnoMjestoService;

    @Autowired
    private OdjelService odjelService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idRadnoMjestoLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(odjeliSearchableComboBox, text -> odjelService.finaAll(text, "naziv"), "idOdjel", "naziv", "Desila se greška prilikom dohvaćanja odjela.", LanguageFormat.HR);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, odjelErrorLabel);
    }

    @Override
    public void initData(RadnoMjestoDto t) {
        if (NumberUtils.isPositive(t.getIdRadnoMjesto())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi radno mjesto");
            ControlUtils.setTextInLabel(idRadnoMjestoLabel, t, "idRadnoMjesto");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setSelectedComboBoxItem(odjeliSearchableComboBox, t, "odjel.idOdjel");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj radno mjesto");
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

            RadnoMjestoDto radnoMjestoDto = new RadnoMjestoDto();
            radnoMjestoDto.setIdRadnoMjesto(ControlUtils.getIntegerFromLabel(idRadnoMjestoLabel, 0));
            radnoMjestoDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            radnoMjestoDto.setOdjel(ControlUtils.getSelectedItemFromComboBox(odjeliSearchableComboBox, id -> odjelService.findById(id)));

            Set<ConstraintViolation<RadnoMjestoDto>> constraintViolations = validator.validate(radnoMjestoDto);

            if (constraintViolations.isEmpty()) {
                radnoMjestoService.save(radnoMjestoDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Radno mjesto je uspješno spremljeno.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilkom spremanja radnog mjesta.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<RadnoMjestoDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("odjel", constraintViolations, odjelErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
