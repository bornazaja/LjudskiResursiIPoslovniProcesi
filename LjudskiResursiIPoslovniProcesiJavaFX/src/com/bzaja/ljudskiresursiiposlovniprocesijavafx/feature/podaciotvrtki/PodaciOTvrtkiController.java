/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.podaciotvrtki;

import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FormUtils;
import com.bzaja.myjavafxlibrary.util.ItemDto;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.podaciotvrtki.PodaciOTvrtkiService;
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
public class PodaciOTvrtkiController implements Initializable {

    @FXML
    private ScrollPane root;

    @FXML
    private Label idPodaciOTvrtkiLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private TextField ulicaTextField;

    @FXML
    private Label ulicaErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> gradoviSearchableComboBox;

    @FXML
    private Label gradErrorLabel;

    @FXML
    private TextField oibTextField;

    @FXML
    private Label oibErrorLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private TextField brojTelefonaTextField;

    @FXML
    private Label brojTelefonaErrorLabel;

    @Autowired
    private PodaciOTvrtkiService podaciOTvrtkiService;

    @Autowired
    private GradService gradService;

    @Autowired
    private Validator validator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idPodaciOTvrtkiLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(gradoviSearchableComboBox, text -> gradService.findAllByDrzavaJeDomovinaTrue(text, "naziv"), "idGrad", "naziv", "Desila se gre??ka prilikom dohva??anja gradova.", LanguageFormat.HR);
        fetchData();
        clearErrorLabels();
    }

    private void fetchData() {
        try {
            PodaciOTvrtkiDto podaciOTvrtkiDto = podaciOTvrtkiService.findFirst();
            if (podaciOTvrtkiDto != null) {
                ControlUtils.setTextInLabel(idPodaciOTvrtkiLabel, podaciOTvrtkiDto, "idPodaciOTvrtki");
                ControlUtils.setTextInTextField(nazivTextField, podaciOTvrtkiDto, "naziv");
                ControlUtils.setTextInTextField(ulicaTextField, podaciOTvrtkiDto, "ulica");
                ControlUtils.setSelectedComboBoxItem(gradoviSearchableComboBox, podaciOTvrtkiDto, "grad.idGrad");
                ControlUtils.setTextInTextField(oibTextField, podaciOTvrtkiDto, "oib");
                ControlUtils.setTextInTextField(emailTextField, podaciOTvrtkiDto, "email");
                ControlUtils.setTextInTextField(brojTelefonaTextField, podaciOTvrtkiDto, "brojTelefona");
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Gre??ka", "Desila se gre??ka prilikom dohva??anja podataka o tvrtki.");
        }
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, ulicaErrorLabel, gradErrorLabel, oibErrorLabel, emailErrorLabel, brojTelefonaErrorLabel);
    }

    @FXML
    public void onOdustaniClick(ActionEvent actionEvent) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            PodaciOTvrtkiDto podaciOTvrtkiDto = new PodaciOTvrtkiDto();
            podaciOTvrtkiDto.setIdPodaciOTvrtki(ControlUtils.getIntegerFromLabel(idPodaciOTvrtkiLabel, 0));
            podaciOTvrtkiDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            podaciOTvrtkiDto.setUlica(ControlUtils.getTextFromTextField(ulicaTextField));
            podaciOTvrtkiDto.setGrad(ControlUtils.getSelectedItemFromComboBox(gradoviSearchableComboBox, id -> gradService.findById(id)));
            podaciOTvrtkiDto.setOib(ControlUtils.getTextFromTextField(oibTextField));
            podaciOTvrtkiDto.setEmail(ControlUtils.getTextFromTextField(emailTextField));
            podaciOTvrtkiDto.setBrojTelefona(ControlUtils.getTextFromTextField(brojTelefonaTextField));

            Set<ConstraintViolation<PodaciOTvrtkiDto>> constraintViolations = validator.validate(podaciOTvrtkiDto);

            if (constraintViolations.isEmpty()) {
                podaciOTvrtkiService.save(podaciOTvrtkiDto);
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Podaci o tvrtki su uspje??no spremljeni.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Gre??ka", "Desila se gre??ka prilikom spremanja podataka o tvrki.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<PodaciOTvrtkiDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("ulica", constraintViolations, ulicaErrorLabel);
        FormUtils.showErrorMessage("grad", constraintViolations, gradErrorLabel);
        FormUtils.showErrorMessage("oib", constraintViolations, oibErrorLabel);
        FormUtils.showErrorMessage("email", constraintViolations, emailErrorLabel);
        FormUtils.showErrorMessage("brojTelefona", constraintViolations, brojTelefonaErrorLabel);
    }
}
