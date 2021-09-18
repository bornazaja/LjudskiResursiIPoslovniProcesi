/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.dodatak;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.dodatak.DodatakService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadodatka.VrstaDodatkaService;
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
public class DodajUrediDodatakZaposlenikaController implements Initializable, ControllerInterface<DodatakDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idDodatakLabel;

    @FXML
    private TextField nazivTextField;

    @FXML
    private Label nazivErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> vrsteDodatakaSearchableComboBox;

    @FXML
    private Label vrstaDodatkaErrorLabel;

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
    private DodatakService dodatakService;

    @Autowired
    private VrstaDodatkaService vrstaDodatkaService;

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
        idDodatakLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(vrsteDodatakaSearchableComboBox, "Desila se greška prilikom dohvaćanja vrsta dodataka.", text -> vrstaDodatkaService.findAll(text, "naziv"), "idVrstaDodatka", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(nazivErrorLabel, vrstaDodatkaErrorLabel, iznosErrorLabel, datumOdErrorLabel, datumDoErrorLabel);
    }

    @Override
    public void initData(DodatakDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdDodatak())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi dodatak zaposlenika");
            ControlUtils.setTextInLabel(idDodatakLabel, t, "idDodatak");
            ControlUtils.setTextInTextField(nazivTextField, t, "naziv");
            ControlUtils.setSelectedComboBoxItem(vrsteDodatakaSearchableComboBox, t, "vrstaDodatka.idVrstaDodatka");
            ControlUtils.setTextInTextField(iznosNumberField, t, "iznos");
            ControlUtils.setValueInClearableDatePicker(datumOdClearableDatePicker, t, "datumOd");
            ControlUtils.setValueInClearableDatePicker(datumDoClearableDatePicker, t, "datumDo");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj dodatak zaposlenika");
        }
        setupValuta(t);
    }

    private void setupValuta(DodatakDto t) {
        AppUtils.runWithTryCatch("Desila se greška prilikom dohvaćanja valute.", () -> {
            valutaDto = NumberUtils.isPositive(t.getIdDodatak()) ? (ValutaDto) BeanUtils.getPropertyValue(t, "valuta") : valutaService.findByDrzaveJeDomovinaTrue();
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

            DodatakDto dodatakDto = new DodatakDto();
            dodatakDto.setIdDodatak(ControlUtils.getIntegerFromLabel(idDodatakLabel, 0));
            dodatakDto.setNaziv(ControlUtils.getTextFromTextField(nazivTextField));
            dodatakDto.setVrstaDodatka(ControlUtils.getSelectedItemFromComboBox(vrsteDodatakaSearchableComboBox, id -> vrstaDodatkaService.findById(id)));
            dodatakDto.setIznos(ControlUtils.getDoubleFromTextField(iznosNumberField, null));
            dodatakDto.setValuta(valutaDto);
            dodatakDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdClearableDatePicker));
            dodatakDto.setDatumDo(ControlUtils.getValueFromClearableDatePicker(datumDoClearableDatePicker));
            dodatakDto.setZaposlenik(zaposlenikDetailsDto);

            Set<ConstraintViolation<DodatakDto>> constraintViolations = validator.validate(dodatakDto);

            if (constraintViolations.isEmpty()) {
                dodatakService.save(dodatakDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Dodatak zaposlenika je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMesssages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja dodatka zaposlenika.");
        }
    }

    private void showErrorMesssages(Set<ConstraintViolation<DodatakDto>> constraintViolations) {
        FormUtils.showErrorMessage("naziv", constraintViolations, nazivErrorLabel);
        FormUtils.showErrorMessage("vrstaDodatka", constraintViolations, vrstaDodatkaErrorLabel);
        FormUtils.showErrorMessage("iznos", constraintViolations, iznosErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
        FormUtils.showErrorMessage("datumDo", constraintViolations, datumDoErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
