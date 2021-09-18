/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.prekovremenirad;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prekovremenirad.PrekovremeniRadService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaprekovremenograda.VrstaPrekovremenogRadaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
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
public class DodajUrediPrekovremeniRadZaposlenikaController implements Initializable, ControllerInterface<PrekovremeniRadDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idPrekovremeniRadLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> vrstePrekovremenihRadovaSearchableComboBox;

    @FXML
    private Label vrstaPrekovremenogRadaErrorLabel;

    @FXML
    private NumberField brojDodatnihSatiNumberField;

    @FXML
    private Label brojDodatnihSatiErrorLabel;

    @FXML
    private ClearableDatePicker datumOdClearableDatePicker;

    @FXML
    private Label datumOdErrorLabel;

    @FXML
    private ClearableDatePicker datumDoClearableDatePicker;

    @FXML
    private Label datumDoErrorLabel;

    @Autowired
    private PrekovremeniRadService prekovremeniRadService;

    @Autowired
    private VrstaPrekovremenogRadaService vrstaPrekovremenogRadaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idPrekovremeniRadLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(vrstePrekovremenihRadovaSearchableComboBox, "Desila se greška prilikom dohvaćanja vrste prekovremenih radova.", text -> vrstaPrekovremenogRadaService.findAll(text, "naziv"), "idVrstaPrekovremenogRada", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, vrstaPrekovremenogRadaErrorLabel, brojDodatnihSatiErrorLabel, datumOdErrorLabel, datumDoErrorLabel);
    }

    @Override
    public void initData(PrekovremeniRadDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdPrekovremeniRad())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi prekovremeni rad zaposlenika");
            ControlUtils.setTextInLabel(idPrekovremeniRadLabel, t, "idPrekovremeniRad");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setSelectedComboBoxItem(vrstePrekovremenihRadovaSearchableComboBox, t, "vrstaPrekovremenogRada.idVrstaPrekovremenogRada");
            ControlUtils.setTextInTextField(brojDodatnihSatiNumberField, t, "brojDodatnihSati");
            ControlUtils.setValueInClearableDatePicker(datumOdClearableDatePicker, t, "datumOd");
            ControlUtils.setValueInClearableDatePicker(datumDoClearableDatePicker, t, "datumDo");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj prekovremeni rad zapsolenika");
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

            PrekovremeniRadDto prekovremeniRadDto = new PrekovremeniRadDto();
            prekovremeniRadDto.setIdPrekovremeniRad(ControlUtils.getIntegerFromLabel(idPrekovremeniRadLabel, 0));
            prekovremeniRadDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            prekovremeniRadDto.setVrstaPrekovremenogRada(ControlUtils.getSelectedItemFromComboBox(vrstePrekovremenihRadovaSearchableComboBox, id -> vrstaPrekovremenogRadaService.findById(id)));
            prekovremeniRadDto.setBrojDodatnihSati(ControlUtils.getDoubleFromTextField(brojDodatnihSatiNumberField, null));
            prekovremeniRadDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdClearableDatePicker));
            prekovremeniRadDto.setDatumDo(ControlUtils.getValueFromClearableDatePicker(datumDoClearableDatePicker));
            prekovremeniRadDto.setZaposlenik(zaposlenikDetailsDto);

            Set<ConstraintViolation<PrekovremeniRadDto>> constraintViolations = validator.validate(prekovremeniRadDto);

            if (constraintViolations.isEmpty()) {
                prekovremeniRadService.save(prekovremeniRadDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Prekovremeni rad zaposlenika je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja prekovremenog rada zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<PrekovremeniRadDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("vrstaPrekovremenogRada", constraintViolations, vrstaPrekovremenogRadaErrorLabel);
        FormUtils.showErrorMessage("brojDodatnihSati", constraintViolations, brojDodatnihSatiErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
        FormUtils.showErrorMessage("datumDo", constraintViolations, datumDoErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
