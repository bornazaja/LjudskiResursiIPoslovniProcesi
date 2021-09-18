/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunstudentskihugovora;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.exception.customexception.InvalidObracunUgovoraException;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.AddObracunStudentskihUgovoraDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunstudentskihugovora.ObracunStudentskihUgovoraService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.parametrizaobracunplace.ParametriZaObracunPlaceService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrstaObracunaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
import com.bzaja.myjavafxlibrary.control.NumberField;
import com.bzaja.myjavafxlibrary.control.SearchableListSelectionView;
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
public class DodajObracunStudentskihUgovoraController implements Initializable {

    @FXML
    private ScrollPane root;

    @FXML
    private ComboBox<ItemDto> vrsteObracunaCombBox;

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
    private SearchableListSelectionView<ItemDto> studentskiUgovoriSearchableListSelectionView;

    @FXML
    private Label studentskiUgovoriErrorLabel;

    @FXML
    private NumberField limitGodisnjegIznosaZaStudentaNumberField;

    @Autowired
    private ObracunStudentskihUgovoraService obracunStudentskihUgovoraService;

    @Autowired
    private VrstaObracunaService vrstaObracunaService;

    @Autowired
    private ValutaService valutaService;

    @Autowired
    private StudentskiUgovorService studentskiUgovorService;

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
            ControlUtils.fillComboBox(vrsteObracunaCombBox, vrstaObracunaService.findAll(), "idVrstaObracuna", "naziv", 0, false);
            ControlUtils.setSelectedComboBoxItem(vrsteObracunaCombBox, VrsteObracuna.OBRACUN_STUDENTSKIH_UGOVORA.getId().toString());
            ControlUtils.fillComboBox(valuteComboBox, valutaService.findAll(), "idValuta", "naziv", 0, false);
            ControlUtils.setSelectedComboBoxItem(valuteComboBox, valutaService.findByDrzaveJeDomovinaTrue().getIdValuta().toString());
            ControlUtils.setupSearchableListSelectionView(studentskiUgovoriSearchableListSelectionView, "Desila se greška prilikom dohvaćanja studentskih ugovora.", text -> studentskiUgovorService.findAllNeObracunateUgovore(text, "zaposlenik.prezime", "zaposlenik.oib"), "idUgovor", "zaposlenik.prezime+zaposlenik.oib");
            ControlUtils.setTextInTextField(limitGodisnjegIznosaZaStudentaNumberField, parametriZaObracunPlaceService.findFirst().getLimitGodisnjegIznosaZaStudenta());
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom dohvaćanja podataka.");
        }
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(opisErrorLabel, datumObracunaErrorLabel, studentskiUgovoriErrorLabel);
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            AddObracunStudentskihUgovoraDto addObracunStudentskihUgovoraDto = new AddObracunStudentskihUgovoraDto();
            addObracunStudentskihUgovoraDto.setVrstaObracuna(ControlUtils.getSelectedItemFromComboBox(vrsteObracunaCombBox, id -> vrstaObracunaService.findById(id)));
            addObracunStudentskihUgovoraDto.setOpis(ControlUtils.getTextFromTextField(opisTextField));
            addObracunStudentskihUgovoraDto.setDatumObracuna(ControlUtils.getValueFromClearableDatePicker(datumObracunaClearableDatePicker));
            addObracunStudentskihUgovoraDto.setValuta(ControlUtils.getSelectedItemFromComboBox(valuteComboBox, id -> valutaService.findById(id)));
            addObracunStudentskihUgovoraDto.setIdeviUgovora(ControlUtils.getKeysFromListViewAsIntegers(studentskiUgovoriSearchableListSelectionView.getListView()));
            addObracunStudentskihUgovoraDto.setLimitGodisnjegIznosaZaStudenta(ControlUtils.getDoubleFromTextField(limitGodisnjegIznosaZaStudentaNumberField, null));

            Set<ConstraintViolation<AddObracunStudentskihUgovoraDto>> constraintViolations = validator.validate(addObracunStudentskihUgovoraDto);

            if (constraintViolations.isEmpty()) {
                obracunStudentskihUgovoraService.save(addObracunStudentskihUgovoraDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Obračun studentskog ugovora je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (InvalidObracunUgovoraException e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", String.format("Neispravan obračun ugovora: %s", e.getMessage()));
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja obračuna studentskih ugovora.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<AddObracunStudentskihUgovoraDto>> constraintViolations) {
        FormUtils.showErrorMessage("opis", constraintViolations, opisErrorLabel);
        FormUtils.showErrorMessage("datumObracuna", constraintViolations, datumObracunaErrorLabel);
        FormUtils.showErrorMessage("ideviUgovora", constraintViolations, studentskiUgovoriErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
