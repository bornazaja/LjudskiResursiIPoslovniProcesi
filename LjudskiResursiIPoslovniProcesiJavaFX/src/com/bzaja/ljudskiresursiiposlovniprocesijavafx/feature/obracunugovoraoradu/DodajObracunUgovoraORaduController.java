/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunugovoraoradu;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.exception.customexception.InvalidObracunUgovoraException;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraoradu.ObracunUgovoraORaduService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace.ParametriZaObracunPlaceDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace.ParametriZaObracunPlaceService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrstaObracunaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
import com.bzaja.myjavafxlibrary.control.NumberField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
public class DodajObracunUgovoraORaduController implements Initializable {

    @FXML
    private ScrollPane root;

    @FXML
    private ComboBox<ItemDto> vrsteObracunaComboBox;

    @FXML
    private TextField opisTextField;

    @FXML
    private Label opisErrorLabel;

    @FXML
    private ClearableDatePicker datumObracunaClearableDatePicker;

    @FXML
    private Label datumObracunaErrorLabel;

    @FXML
    private ComboBox<ItemDto> valuteComboBox;

    @FXML
    private ClearableDatePicker datumOdClearableDatePicker;

    @FXML
    private Label datumOdErrorLabel;

    @FXML
    private ClearableDatePicker datumDoClearableDatePicker;

    @FXML
    private Label datumDoErrorLabel;

    @FXML
    private NumberField osnovniOsobniOdbitakNumberField;

    @FXML
    private NumberField osnovicaOsobnogOdbitkaNumberField;

    @Autowired
    private ObracunUgovoraORaduService obracunUgovoraORaduService;

    @Autowired
    private VrstaObracunaService vrstaObracunaService;

    @Autowired
    private ValutaService valutaService;

    @Autowired
    private ParametriZaObracunPlaceService parametriZaObracunPlaceService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        fetchData();
        clearErrorLabels();
    }

    private void fetchData() {
        try {
            ParametriZaObracunPlaceDto parametriZaObracunPlaceDto = parametriZaObracunPlaceService.findFirst();

            ControlUtils.fillComboBox(vrsteObracunaComboBox, vrstaObracunaService.findAll(), "idVrstaObracuna", "naziv", 0, false);
            ControlUtils.setSelectedComboBoxItem(vrsteObracunaComboBox, VrsteObracuna.OBRACUN_UGOVORA_O_RADU.getId().toString());
            ControlUtils.fillComboBox(valuteComboBox, valutaService.findAll(), "idValuta", "naziv", 0, false);
            ControlUtils.setSelectedComboBoxItem(valuteComboBox, valutaService.findByDrzaveJeDomovinaTrue().getIdValuta().toString());
            ControlUtils.setTextInTextField(osnovniOsobniOdbitakNumberField, parametriZaObracunPlaceDto, "osnovniOsobniOdbitak");
            ControlUtils.setTextInTextField(osnovicaOsobnogOdbitkaNumberField, parametriZaObracunPlaceDto, "osnovicaOsobnogOdbitka");
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom dohvaćanja podataka.");
        }
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(opisErrorLabel, datumObracunaErrorLabel, datumOdErrorLabel, datumDoErrorLabel);
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            ObracunUgovoraORaduDto obracunUgovoraORaduDto = new ObracunUgovoraORaduDto();
            obracunUgovoraORaduDto.setVrstaObracuna(ControlUtils.getSelectedItemFromComboBox(vrsteObracunaComboBox, id -> vrstaObracunaService.findById(id)));
            obracunUgovoraORaduDto.setOpis(ControlUtils.getTextFromTextField(opisTextField));
            obracunUgovoraORaduDto.setDatumObracuna(ControlUtils.getValueFromClearableDatePicker(datumObracunaClearableDatePicker));
            obracunUgovoraORaduDto.setValuta(ControlUtils.getSelectedItemFromComboBox(valuteComboBox, id -> valutaService.findById(id)));
            obracunUgovoraORaduDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdClearableDatePicker));
            obracunUgovoraORaduDto.setDatumDo(ControlUtils.getValueFromClearableDatePicker(datumDoClearableDatePicker));
            obracunUgovoraORaduDto.setOsnovniOsobniOdbitak(ControlUtils.getDoubleFromTextField(osnovniOsobniOdbitakNumberField, null));
            obracunUgovoraORaduDto.setOsnovicaOsobnogOdbitka(ControlUtils.getDoubleFromTextField(osnovicaOsobnogOdbitkaNumberField, null));

            Set<ConstraintViolation<ObracunUgovoraORaduDto>> constraintViolations = validator.validate(obracunUgovoraORaduDto);

            if (constraintViolations.isEmpty()) {
                obracunUgovoraORaduService.save(obracunUgovoraORaduDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Obračun ugovora o radu je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (InvalidObracunUgovoraException e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", String.format("Neispravan obračun ugovora: %s", e.getMessage()));
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja obračuna ugovora u radu.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<ObracunUgovoraORaduDto>> constraintViolations) {
        FormUtils.showErrorMessage("opis", constraintViolations, opisErrorLabel);
        FormUtils.showErrorMessage("datumObracuna", constraintViolations, datumObracunaErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
        FormUtils.showErrorMessage("datumDo", constraintViolations, datumDoErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
