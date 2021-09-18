/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obustava;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obustava.ObustavaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobustave.VrstaObustaveService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanUtils;
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
public class DodajUrediObustavuZaposlenikaController implements Initializable, ControllerInterface<ObustavaDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idObustavaLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> vrsteObustaveSearchabeComboBox;

    @FXML
    private Label vrstaObustaveErrorLabel;

    @FXML
    private NumberField iznosNumberField;

    @FXML
    private Label valutaLabel;

    @FXML
    private Label iznosErrorLabel;

    @FXML
    private ClearableDatePicker datumOdClearableDatePicker;

    @FXML
    private Label datumOdErrorLabel;

    @FXML
    private ClearableDatePicker datumDoClearableDatePicker;

    @FXML
    private Label datumDoErrorLabel;

    @Autowired
    private ObustavaService obustavaService;

    @Autowired
    private VrstaObustaveService vrstaObustaveService;

    @Autowired
    private ValutaService valutaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;
    private ValutaDto valutaDto;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idObustavaLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(vrsteObustaveSearchabeComboBox, "Desila se greška prilikom dohvaćanja vrsta obustava.", text -> vrstaObustaveService.findAll(text, "naziv"), "idVrstaObustave", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, vrstaObustaveErrorLabel, iznosErrorLabel, datumOdErrorLabel, datumDoErrorLabel);
    }

    @Override
    public void initData(ObustavaDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdObustava())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi obustavu zaposlenika");
            ControlUtils.setTextInLabel(idObustavaLabel, t, "idObustava");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setSelectedComboBoxItem(vrsteObustaveSearchabeComboBox, t, "vrstaObustave.idVrstaObustave");
            ControlUtils.setTextInTextField(iznosNumberField, t, "iznos");
            ControlUtils.setValueInClearableDatePicker(datumOdClearableDatePicker, t, "datumOd");
            ControlUtils.setValueInClearableDatePicker(datumDoClearableDatePicker, t, "datumDo");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj obustavu zaposlenika");
        }
        setupValuta(t);
    }

    private void setupValuta(ObustavaDto t) {
        AppUtils.runWithTryCatch("Desila se greška prilikom dohvačanja valute.", () -> {
            valutaDto = NumberUtils.isPositive(t.getIdObustava()) ? (ValutaDto) BeanUtils.getPropertyValue(t, "valuta") : valutaService.findByDrzaveJeDomovinaTrue();
            ControlUtils.setTextInLabel(valutaLabel, valutaDto.getNaziv());
        });
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            ObustavaDto obustavaDto = new ObustavaDto();
            obustavaDto.setIdObustava(ControlUtils.getIntegerFromLabel(idObustavaLabel, 0));
            obustavaDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            obustavaDto.setVrstaObustave(ControlUtils.getSelectedItemFromComboBox(vrsteObustaveSearchabeComboBox, id -> vrstaObustaveService.findById(id)));
            obustavaDto.setIznos(ControlUtils.getDoubleFromTextField(iznosNumberField, null));
            obustavaDto.setValuta(valutaDto);
            obustavaDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdClearableDatePicker));
            obustavaDto.setDatumDo(ControlUtils.getValueFromClearableDatePicker(datumDoClearableDatePicker));
            obustavaDto.setZaposlenik(zaposlenikDetailsDto);

            Set<ConstraintViolation<ObustavaDto>> constraintViolations = validator.validate(obustavaDto);

            if (constraintViolations.isEmpty()) {
                obustavaService.save(obustavaDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Obustava zaposlenika je uspješno spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja obustave zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<ObustavaDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("vrstaObustave", constraintViolations, vrstaObustaveErrorLabel);
        FormUtils.showErrorMessage("iznos", constraintViolations, iznosErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
        FormUtils.showErrorMessage("datumDo", constraintViolations, datumDoErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
