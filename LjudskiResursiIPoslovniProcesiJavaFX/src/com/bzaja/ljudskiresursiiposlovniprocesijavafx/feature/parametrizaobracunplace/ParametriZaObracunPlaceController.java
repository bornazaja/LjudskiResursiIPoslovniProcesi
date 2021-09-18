/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.parametrizaobracunplace;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace.ParametriZaObracunPlaceDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace.ParametriZaObracunPlaceService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.myjavafxlibrary.control.NumberField;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
public class ParametriZaObracunPlaceController implements Initializable {

    @FXML
    private ScrollPane root;

    @FXML
    private Label idParametriZaObracunPlaceLabel;

    @FXML
    private NumberField osnovniOsobniOdbitakNumberField;

    @FXML
    private Label valutaLabel1;

    @FXML
    private Label osnovniOsobniOdbitakErrorLabel;

    @FXML
    private NumberField osnovicaOsobnogOdbitkaNumberField;

    @FXML
    private Label valutaLabel2;

    @FXML
    private Label osnovicaOsobnogOdbitkaErrorLabel;

    @FXML
    private NumberField limitGodisnjegIznosZaStudentaNumberField;

    @FXML
    private Label valutaLabel3;

    @FXML
    private Label limitGodisnjegIznosaZaStudentaErrorLabel;

    @Autowired
    private ParametriZaObracunPlaceService parametriZaObracunPlaceService;

    @Autowired
    private ValutaService valutaService;

    @Autowired
    private Validator validator;

    private ValutaDto valutaDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idParametriZaObracunPlaceLabel.setVisible(false);
        fetchData();
        fetchDomacuValutu();
        clearErrorLabels();
    }

    private void fetchData() {
        try {
            ParametriZaObracunPlaceDto parametriZaObracunPlaceDto = parametriZaObracunPlaceService.findFirst();
            if (parametriZaObracunPlaceDto != null) {
                ControlUtils.setTextInLabel(idParametriZaObracunPlaceLabel, parametriZaObracunPlaceDto, "idParametriZaObracunPlace");
                ControlUtils.setTextInTextField(osnovniOsobniOdbitakNumberField, parametriZaObracunPlaceDto, "osnovniOsobniOdbitak");
                ControlUtils.setTextInTextField(osnovicaOsobnogOdbitkaNumberField, parametriZaObracunPlaceDto, "osnovicaOsobnogOdbitka");
                ControlUtils.setTextInTextField(limitGodisnjegIznosZaStudentaNumberField, parametriZaObracunPlaceDto, "limitGodisnjegIznosaZaStudenta");
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška priliom dohvaćanja parametara za obračun plaće.");
        }
    }

    private void fetchDomacuValutu() {
        try {
            valutaDto = valutaService.findByDrzaveJeDomovinaTrue();
            List<Label> labels = Arrays.asList(valutaLabel1, valutaLabel2, valutaLabel3);
            labels.forEach((l) -> l.setText(valutaDto.getNaziv()));
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom dohvaćanja domaće valute.");
        }
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(osnovniOsobniOdbitakErrorLabel, osnovicaOsobnogOdbitkaErrorLabel, limitGodisnjegIznosaZaStudentaErrorLabel);
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            ParametriZaObracunPlaceDto parametriZaObracunPlaceDto = new ParametriZaObracunPlaceDto();
            parametriZaObracunPlaceDto.setIdParametriZaObracunPlace(ControlUtils.getIntegerFromLabel(idParametriZaObracunPlaceLabel, 0));
            parametriZaObracunPlaceDto.setOsnovniOsobniOdbitak(ControlUtils.getDoubleFromTextField(osnovniOsobniOdbitakNumberField, null));
            parametriZaObracunPlaceDto.setOsnovicaOsobnogOdbitka((ControlUtils.getDoubleFromTextField(osnovicaOsobnogOdbitkaNumberField, null)));
            parametriZaObracunPlaceDto.setLimitGodisnjegIznosaZaStudenta(ControlUtils.getDoubleFromTextField(limitGodisnjegIznosZaStudentaNumberField, null));
            parametriZaObracunPlaceDto.setValuta(valutaDto);

            Set<ConstraintViolation<ParametriZaObracunPlaceDto>> constraintViolations = validator.validate(parametriZaObracunPlaceDto);

            if (constraintViolations.isEmpty()) {
                parametriZaObracunPlaceService.save(parametriZaObracunPlaceDto);
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Parametri za obračun plače su uspješno spremljeni.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja parametara za obračun plaće.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<ParametriZaObracunPlaceDto>> constraintViolations) {
        FormUtils.showErrorMessage("osnovniOsobniOdbitak", constraintViolations, osnovniOsobniOdbitakErrorLabel);
        FormUtils.showErrorMessage("osnovicaOsobnogOdbitka", constraintViolations, osnovicaOsobnogOdbitkaErrorLabel);
        FormUtils.showErrorMessage("limitGodisnjegIznosaZaStudenta", constraintViolations, limitGodisnjegIznosaZaStudentaErrorLabel);
    }
}
