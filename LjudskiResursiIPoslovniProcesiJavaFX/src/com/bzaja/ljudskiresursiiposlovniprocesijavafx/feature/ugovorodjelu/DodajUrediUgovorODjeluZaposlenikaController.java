/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.ugovorodjelu;

import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FormUtils;
import com.bzaja.myjavafxlibrary.util.ItemDto;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos.RadniOdnosService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovorodjelu.UgovorODjeluService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaugovora.VrsteUgovora;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.myjavalibrary.util.BeanUtils;
import com.bzaja.myjavalibrary.util.NumberUtils;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
import com.bzaja.myjavafxlibrary.control.NumberField;
import com.bzaja.myjavafxlibrary.control.SearchableComboBox;
import com.bzaja.myjavalibrary.util.LanguageFormat;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
public class DodajUrediUgovorODjeluZaposlenikaController implements Initializable, ControllerInterface<UgovorODjeluDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idUgovorLabel;

    @FXML
    private SearchableComboBox<ItemDto> radniOdnosiSearchableComboBox;

    @FXML
    private Label radniOdnosErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> radnaMjestaSearchableComboBox;

    @FXML
    private Label radnoMjestoErrorLabel;

    @FXML
    private ClearableDatePicker datumOdClearableDatePicker;

    @FXML
    private Label datumOdErrorLabel;

    @FXML
    private ClearableDatePicker datumDoClearableDatePicker;

    @FXML
    private Label datumDoErrorLabel;

    @FXML
    private NumberField brutoIznosNumebrField;

    @FXML
    private Label valutaLabel;

    @FXML
    private Label brutoIznosErrorLabel;

    @FXML
    private NumberField stopaPausalnogPriznatogTroskaNumberField;

    @FXML
    private Label stopaPausalnogPriznatogTroskaErrorLabel;

    @FXML
    private CheckBox jeObracunatCheckBox;

    @Autowired
    private UgovorODjeluService ugovorODjeluService;

    @Autowired
    private RadniOdnosService radniOdnosService;

    @Autowired
    private RadnoMjestoService radnoMjestoService;

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
        idUgovorLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(radniOdnosiSearchableComboBox, text -> radniOdnosService.findAll(text, VrsteUgovora.UGOVOR_O_DJELU.getId(), "naziv"), "idRadniOdnos", "naziv", "Desila se greška prilikom dohvaćanja radnih odnosa.", LanguageFormat.HR);
        ControlUtils.setupSearchableComboBox(radnaMjestaSearchableComboBox, text -> radnoMjestoService.findAll(text, "naziv"), "idRadnoMjesto", "naziv", "Desila se greška prilikom dohvaćanja radnih mjesta.", LanguageFormat.HR);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(radniOdnosErrorLabel, radnoMjestoErrorLabel, datumOdErrorLabel, datumDoErrorLabel, brutoIznosErrorLabel, stopaPausalnogPriznatogTroskaErrorLabel);
    }

    @Override
    public void initData(UgovorODjeluDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdUgovor())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi ugovor o djelu zaposlenika");
            ControlUtils.setTextInLabel(idUgovorLabel, t, "idUgovor");
            ControlUtils.setSelectedComboBoxItem(radniOdnosiSearchableComboBox, t, "radniOdnos.idRadniOdnos");
            ControlUtils.setSelectedComboBoxItem(radnaMjestaSearchableComboBox, t, "radnoMjesto.idRadnoMjesto");
            ControlUtils.setValueInClearableDatePicker(datumOdClearableDatePicker, t, "datumOd");
            ControlUtils.setValueInClearableDatePicker(datumDoClearableDatePicker, t, "datumDo");
            ControlUtils.setTextInTextField(brutoIznosNumebrField, t, "brutoIznos");
            ControlUtils.setTextInTextField(stopaPausalnogPriznatogTroskaNumberField, t, "stopaPausalnogPriznatogTroska");
            ControlUtils.setValueInCheckBox(jeObracunatCheckBox, t, "jeObracunat");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj ugovor o djelu zaposlenika");
        }
        setupValuta(t);
    }

    private void setupValuta(UgovorODjeluDto t) {
        DialogUtils.tryShowErrorDialog(() -> {
            valutaDto = NumberUtils.isPositive(t.getIdUgovor()) ? (ValutaDto) BeanUtils.getPropertyValue(t, "valuta") : valutaService.findByDrzaveJeDomovinaTrue();
            ControlUtils.setTextInLabel(valutaLabel, valutaDto.getNaziv());
        }, "Desila se greška prilikom dohvaćanja valute.");
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            clearErrorLabels();

            UgovorODjeluDto ugovorODjeluDto = new UgovorODjeluDto();
            ugovorODjeluDto.setIdUgovor(ControlUtils.getIntegerFromLabel(idUgovorLabel, 0));
            ugovorODjeluDto.setRadniOdnos(ControlUtils.getSelectedItemFromComboBox(radniOdnosiSearchableComboBox, id -> radniOdnosService.findById(id)));
            ugovorODjeluDto.setZaposlenik(zaposlenikDetailsDto);
            ugovorODjeluDto.setRadnoMjesto(ControlUtils.getSelectedItemFromComboBox(radnaMjestaSearchableComboBox, id -> radnoMjestoService.findById(id)));
            ugovorODjeluDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdClearableDatePicker));
            ugovorODjeluDto.setDatumDo(ControlUtils.getValueFromClearableDatePicker(datumDoClearableDatePicker));
            ugovorODjeluDto.setBrutoIznos(ControlUtils.getDoubleFromTextField(brutoIznosNumebrField, null));
            ugovorODjeluDto.setValuta(valutaDto);
            ugovorODjeluDto.setStopaPausalnogPriznatogTroska(ControlUtils.getDoubleFromTextField(stopaPausalnogPriznatogTroskaNumberField, null));
            ugovorODjeluDto.setJeObracunat(ControlUtils.getValueFromCheckBox(jeObracunatCheckBox));

            Set<ConstraintViolation<UgovorODjeluDto>> constraintViolations = validator.validate(ugovorODjeluDto);

            if (constraintViolations.isEmpty()) {
                ugovorODjeluService.save(ugovorODjeluDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Ugovor o djelu je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja ugovora o djelu zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<UgovorODjeluDto>> constraintViolations) {
        FormUtils.showErrorMessage("radniOdnos", constraintViolations, radniOdnosErrorLabel);
        FormUtils.showErrorMessage("radnoMjesto", constraintViolations, radnoMjestoErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
        FormUtils.showErrorMessage("datumDo", constraintViolations, datumDoErrorLabel);
        FormUtils.showErrorMessage("brutoIznos", constraintViolations, brutoIznosErrorLabel);
        FormUtils.showErrorMessage("stopaPausalnogPriznatogTroska", constraintViolations, stopaPausalnogPriznatogTroskaErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
