/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ChangePasswordZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikService;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
public class PromjenaLozinkeZaposlenikaController implements Initializable, ControllerInterface<Integer> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label idZaposlenikLabel;

    @FXML
    private PasswordField staraLozinkaPasswordField;

    @FXML
    private Label staraLozinkaErrorLabel;

    @FXML
    private PasswordField novaLozinkaPasswordField;

    @FXML
    private Label novaLozinkaErrorLabel;

    @FXML
    private PasswordField ponoviNovuLozinkuPasswordField;

    @FXML
    private Label ponoviNovuLozinkuErrorLabel;

    @Autowired
    private ZaposlenikService zaposlenikService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    private Boolean prijavljeniZaposlenik;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idZaposlenikLabel.setVisible(false);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(staraLozinkaErrorLabel, novaLozinkaErrorLabel, ponoviNovuLozinkuErrorLabel);
    }

    @Override
    public void initData(Integer t) {
        ControlUtils.setTextInLabel(idZaposlenikLabel, t);
        prijavljeniZaposlenik = AppUtils.isPrijavljeniZaposlenik(ControlUtils.getIntegerFromLabel(idZaposlenikLabel, 0));
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            ChangePasswordZaposlenikDto changePasswordZaposlenikDto = new ChangePasswordZaposlenikDto();
            changePasswordZaposlenikDto.setIdZaposlenik(ControlUtils.getIntegerFromLabel(idZaposlenikLabel, 0));
            changePasswordZaposlenikDto.setStaraLozinka(ControlUtils.getTextFromTextField(staraLozinkaPasswordField));
            changePasswordZaposlenikDto.setNovaLozinka(ControlUtils.getTextFromTextField(novaLozinkaPasswordField));
            changePasswordZaposlenikDto.setPonoviNovuLozinku(ControlUtils.getTextFromTextField(ponoviNovuLozinkuPasswordField));

            Set<ConstraintViolation<ChangePasswordZaposlenikDto>> constraintViolations = validator.validate(changePasswordZaposlenikDto);

            if (constraintViolations.isEmpty()) {
                zaposlenikService.changePassword(changePasswordZaposlenikDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", getSuccessMessage());
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", getErrorMessage());
        }
    }

    private String getSuccessMessage() {
        return prijavljeniZaposlenik ? "Vaša lozinka je uspješno promjenjena." : "Lozinka zaposlenika je uspješno promjenjena.";
    }

    public String getErrorMessage() {
        return prijavljeniZaposlenik ? "Desila se greška prilikom promjene vaše lozinke." : "Desila se greška prilikom promjene lozinke zaposlenika.";
    }

    private void showErrorMessages(Set<ConstraintViolation<ChangePasswordZaposlenikDto>> constraintViolations) {
        FormUtils.showErrorMessage("staraLozinka", constraintViolations, staraLozinkaErrorLabel);
        FormUtils.showErrorMessage("novaLozinka", constraintViolations, novaLozinkaErrorLabel);
        FormUtils.showErrorMessage("ponoviNovuLozinku", constraintViolations, ponoviNovuLozinkuErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
