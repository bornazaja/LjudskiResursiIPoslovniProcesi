/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.studentskiposaocjenik;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenikService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
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
public class DodajUrediStudentskiPosaoCjenikController implements Initializable, ControllerInterface<StudentskiPosaoCjenikDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idStudentskiPosaoCjenikLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private NumberField cijenaPoSatuNumberField;

    @FXML
    private Label domacaValutaLabel;

    @FXML
    private Label cijenaPoSatuErrorLabel;

    @Autowired
    private StudentskiPosaoCjenikService studentskiPosaoCjenikService;

    @Autowired
    private ValutaService valutaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    private ValutaDto valutaDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idStudentskiPosaoCjenikLabel.setVisible(false);
        fetchDomacuValutu();
        clearErrorLabels();
    }

    private void fetchDomacuValutu() {
        try {
            valutaDto = valutaService.findByDrzaveJeDomovinaTrue();
            ControlUtils.setTextInLabel(domacaValutaLabel, valutaDto.getNaziv());
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Error", "Desila se greška prilikom dohavćanja domaće valute.");
        }
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, cijenaPoSatuErrorLabel);
    }

    @Override
    public void initData(StudentskiPosaoCjenikDto t) {
        if (NumberUtils.isPositive(t.getIdStudentskiPosaoCjenik())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi studentski posao cjenik");
            ControlUtils.setTextInLabel(idStudentskiPosaoCjenikLabel, t, "idStudentskiPosaoCjenik");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setTextInTextField(cijenaPoSatuNumberField, t, "cijenaPoSatu");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj studentski posao cjenik");
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

            StudentskiPosaoCjenikDto studentskiPosaoCjenikDto = new StudentskiPosaoCjenikDto();
            studentskiPosaoCjenikDto.setIdStudentskiPosaoCjenik(ControlUtils.getIntegerFromLabel(idStudentskiPosaoCjenikLabel, 0));
            studentskiPosaoCjenikDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            studentskiPosaoCjenikDto.setCijenaPoSatu(ControlUtils.getDoubleFromTextField(cijenaPoSatuNumberField, null));
            studentskiPosaoCjenikDto.setValuta(valutaDto);

            Set<ConstraintViolation<StudentskiPosaoCjenikDto>> constraintViolations = validator.validate(studentskiPosaoCjenikDto);

            if (constraintViolations.isEmpty()) {
                studentskiPosaoCjenikService.save(studentskiPosaoCjenikDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Studentski posao cjenik je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Erro", "Desila se greška prilikom spremena studentskog posla cjenik.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<StudentskiPosaoCjenikDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("cijenaPoSatu", constraintViolations, cijenaPoSatuErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
