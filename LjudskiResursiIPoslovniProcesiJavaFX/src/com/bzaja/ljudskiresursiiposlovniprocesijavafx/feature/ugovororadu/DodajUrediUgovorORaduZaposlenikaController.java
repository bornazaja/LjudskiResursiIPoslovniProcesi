/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.ugovororadu;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radniodnos.RadniOdnosService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.radnomjesto.RadnoMjestoService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.ugovororadu.UgovorORaduService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaugovora.VrsteUgovora;
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
public class DodajUrediUgovorORaduZaposlenikaController implements Initializable, ControllerInterface<UgovorORaduDto> {

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
    private NumberField brojRadnihSatiTjednoNumberField;

    @FXML
    private Label brojRadnihSatiTjednoErrorLabel;

    @FXML
    private NumberField brutoPlacaNumberField;

    @FXML
    private Label domacaValutaLabel;

    @FXML
    private Label brutoPlacaErrorLabel;

    @Autowired
    private UgovorORaduService ugovorORaduService;

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
        ControlUtils.setupSearchableComboBox(radniOdnosiSearchableComboBox, "Desila se greška prilikom dohvaćanja radnih odnosa.", text -> radniOdnosService.findAll(text, VrsteUgovora.UGOVOR_O_RADU.getId(), "naziv"), "idRadniOdnos", "naziv");
        ControlUtils.setupSearchableComboBox(radnaMjestaSearchableComboBox, "Desila se greška prilikom dohvaćanja radnih mjesta.", text -> radnoMjestoService.findAll(text, "naziv"), "idRadnoMjesto", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(radniOdnosErrorLabel, radnoMjestoErrorLabel, datumOdErrorLabel, datumDoErrorLabel, brojRadnihSatiTjednoErrorLabel, brutoPlacaErrorLabel);
    }

    @Override
    public void initData(UgovorORaduDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdUgovor())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi ugovor o radu zaposlenika");
            ControlUtils.setTextInLabel(idUgovorLabel, t, "idUgovor");
            ControlUtils.setSelectedComboBoxItem(radniOdnosiSearchableComboBox, t, "radniOdnos.idRadniOdnos");
            ControlUtils.setSelectedComboBoxItem(radnaMjestaSearchableComboBox, t, "radnoMjesto.idRadnoMjesto");
            ControlUtils.setValueInClearableDatePicker(datumOdClearableDatePicker, t, "datumOd");
            ControlUtils.setValueInClearableDatePicker(datumDoClearableDatePicker, t, "datumDo");
            ControlUtils.setTextInTextField(brojRadnihSatiTjednoNumberField, t, "brojRadnihSatiTjedno");
            ControlUtils.setTextInTextField(brutoPlacaNumberField, t, "brutoPlaca");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj ugovor o radu zaposlenika");
        }
        setupValuta(t);
    }

    private void setupValuta(UgovorORaduDto t) {
        AppUtils.runWithTryCatch("Desila se greška prilikom dohvaćanja valute.", () -> {
            valutaDto = NumberUtils.isPositive(t.getIdUgovor()) ? (ValutaDto) BeanUtils.getPropertyValue(t, "valuta") : valutaService.findByDrzaveJeDomovinaTrue();
            ControlUtils.setTextInLabel(domacaValutaLabel, valutaDto.getNaziv());
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

            UgovorORaduDto ugovorORaduDto = new UgovorORaduDto();
            ugovorORaduDto.setIdUgovor(ControlUtils.getIntegerFromLabel(idUgovorLabel, 0));
            ugovorORaduDto.setRadniOdnos(ControlUtils.getSelectedItemFromComboBox(radniOdnosiSearchableComboBox, id -> radniOdnosService.findById(id)));
            ugovorORaduDto.setZaposlenik(zaposlenikDetailsDto);
            ugovorORaduDto.setRadnoMjesto(ControlUtils.getSelectedItemFromComboBox(radnaMjestaSearchableComboBox, id -> radnoMjestoService.findById(id)));
            ugovorORaduDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdClearableDatePicker));
            ugovorORaduDto.setDatumDo(ControlUtils.getValueFromClearableDatePicker(datumDoClearableDatePicker));
            ugovorORaduDto.setBrojRadnihSatiTjedno(ControlUtils.getDoubleFromTextField(brojRadnihSatiTjednoNumberField, null));
            ugovorORaduDto.setBrutoPlaca(ControlUtils.getDoubleFromTextField(brutoPlacaNumberField, null));
            ugovorORaduDto.setValuta(valutaDto);

            Set<ConstraintViolation<UgovorORaduDto>> constraintViolations = validator.validate(ugovorORaduDto);

            if (constraintViolations.isEmpty()) {
                ugovorORaduService.save(ugovorORaduDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Ugovor o radu zaposlenika je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja ugovora o radu zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<UgovorORaduDto>> constraintViolations) {
        FormUtils.showErrorMessage("radniOdnos", constraintViolations, radniOdnosErrorLabel);
        FormUtils.showErrorMessage("radnoMjesto", constraintViolations, radnoMjestoErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
        FormUtils.showErrorMessage("datumDo", constraintViolations, datumDoErrorLabel);
        FormUtils.showErrorMessage("brojRadnihSatiTjedno", constraintViolations, brojRadnihSatiTjednoErrorLabel);
        FormUtils.showErrorMessage("brutoPlaca", constraintViolations, brutoPlacaErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
