/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.kategorijatransakcije;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcijeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcijeService;
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
public class DodajUrediKategorijuTransakcijeController implements Initializable, ControllerInterface<KategorijaTransakcijeDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idKategorijaTransakcijeLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @Autowired
    private KategorijaTransakcijeService kategorijaTransakcijeService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idKategorijaTransakcijeLabel.setVisible(false);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel);
    }

    @Override
    public void initData(KategorijaTransakcijeDto t) {
        if (NumberUtils.isPositive(t.getIdKategorijaTransakcije())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi kategoriju transakcije");
            ControlUtils.setTextInLabel(idKategorijaTransakcijeLabel, t, "idKategorijaTransakcije");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj kategoriju transakcije");
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
            KategorijaTransakcijeDto kategorijaTransakcijeDto = new KategorijaTransakcijeDto();
            kategorijaTransakcijeDto.setIdKategorijaTransakcije(ControlUtils.getIntegerFromLabel(idKategorijaTransakcijeLabel, 0));
            kategorijaTransakcijeDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));

            Set<ConstraintViolation<KategorijaTransakcijeDto>> constraintViolations = validator.validate(kategorijaTransakcijeDto);

            if (constraintViolations.isEmpty()) {
                kategorijaTransakcijeService.save(kategorijaTransakcijeDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Kategorija transakcije je uspješno spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja kategorije transakcije.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<KategorijaTransakcijeDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
