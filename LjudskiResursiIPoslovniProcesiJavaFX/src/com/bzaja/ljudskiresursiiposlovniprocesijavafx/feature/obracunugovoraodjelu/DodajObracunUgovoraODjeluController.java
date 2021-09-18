/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunugovoraodjelu;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.exception.customexception.InvalidObracunUgovoraException;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.AddObracunUgovoraODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.obracunugovoraodjelu.ObracunUgovoraODjeluService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrstaObracunaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaobracuna.VrsteObracuna;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
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
public class DodajObracunUgovoraODjeluController implements Initializable {

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
    private SearchableListSelectionView<ItemDto> ugovoriODjeluSearchableListSelectionView;

    @FXML
    private Label ugovoriODjeluErrorLabel;

    @Autowired
    private ObracunUgovoraODjeluService obracunUgovoraODjeluService;

    @Autowired
    private VrstaObracunaService vrstaObracunaService;

    @Autowired
    private ValutaService valutaService;

    @Autowired
    private UgovorODjeluService ugovorODjeluService;

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
            ControlUtils.fillComboBox(vrsteObracunaComboBox, vrstaObracunaService.findAll(), "idVrstaObracuna", "naziv", 0, false);
            ControlUtils.setSelectedComboBoxItem(vrsteObracunaComboBox, VrsteObracuna.OBRACUN_UGOVORA_O_DJELU.getId().toString());
            ControlUtils.fillComboBox(valuteComboBox, valutaService.findAll(), "idValuta", "naziv", 0, false);
            ControlUtils.setSelectedComboBoxItem(valuteComboBox, valutaService.findByDrzaveJeDomovinaTrue().getIdValuta().toString());
            ControlUtils.setupSearchableListSelectionView(ugovoriODjeluSearchableListSelectionView, "Desila se greška prilikom dohvačanja ugovora o djelu.", text -> ugovorODjeluService.findAllNeObraucunateUgovore(text, "zaposlenik.prezime", "zaposlenik.oib"), "idUgovor", "zaposlenik.prezime+zaposlenik.oib");
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom dohvaćanja podataka.");
        }
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(opisErrorLabel, datumObracunaErrorLabel, ugovoriODjeluErrorLabel);
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            AddObracunUgovoraODjeluDto addObracunUgovoraODjeluDto = new AddObracunUgovoraODjeluDto();
            addObracunUgovoraODjeluDto.setVrstaObracuna(ControlUtils.getSelectedItemFromComboBox(vrsteObracunaComboBox, id -> vrstaObracunaService.findById(id)));
            addObracunUgovoraODjeluDto.setOpis(ControlUtils.getTextFromTextField(opisTextField));
            addObracunUgovoraODjeluDto.setDatumObracuna(ControlUtils.getValueFromClearableDatePicker(datumObracunaClearableDatePicker));
            addObracunUgovoraODjeluDto.setValuta(ControlUtils.getSelectedItemFromComboBox(valuteComboBox, id -> valutaService.findById(id)));
            addObracunUgovoraODjeluDto.setIdeviUgovora(ControlUtils.getKeysFromListViewAsIntegers(ugovoriODjeluSearchableListSelectionView.getListView()));

            Set<ConstraintViolation<AddObracunUgovoraODjeluDto>> constraintViolations = validator.validate(addObracunUgovoraODjeluDto);

            if (constraintViolations.isEmpty()) {
                obracunUgovoraODjeluService.save(addObracunUgovoraODjeluDto);
                stageResult = StageResult.OK;
                NodeUtils.closeCurrentStageByNode(root);
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Obračun ugovora o djelu je uspješno spremljen.");
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (InvalidObracunUgovoraException e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", String.format("Neispravan obračun ugovora: %s", e.getMessage()));
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja obračuna ugoovora o djelu.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<AddObracunUgovoraODjeluDto>> constraintViolations) {
        FormUtils.showErrorMessage("opis", constraintViolations, opisErrorLabel);
        FormUtils.showErrorMessage("datumObracuna", constraintViolations, datumObracunaErrorLabel);
        FormUtils.showErrorMessage("ideviUgovora", constraintViolations, ugovoriODjeluErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
