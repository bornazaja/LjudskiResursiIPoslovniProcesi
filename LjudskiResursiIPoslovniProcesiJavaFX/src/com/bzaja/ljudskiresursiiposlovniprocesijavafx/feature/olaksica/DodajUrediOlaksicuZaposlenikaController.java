/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.olaksica;

import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FormUtils;
import com.bzaja.myjavafxlibrary.util.ItemDto;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.olaksica.OlaksicaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstaolaksice.VrstaOlaksiceService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.myjavalibrary.util.NumberUtils;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
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
public class DodajUrediOlaksicuZaposlenikaController implements Initializable, ControllerInterface<OlaksicaDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idOlaksicaLabel;

    @FXML
    private SearchableComboBox<ItemDto> olaksiceSearchableComboBox;

    @FXML
    private Label vrstaOlaksiceErrorLabel;

    @FXML
    private ClearableDatePicker datumOdClearableDatePicker;

    @FXML
    private Label datumOdErrorLabel;

    @FXML
    private ClearableDatePicker datumDoClearableDatePicker;

    @FXML
    private Label datumDoErrorLabel;

    @Autowired
    private OlaksicaService olaksicaService;

    @Autowired
    private VrstaOlaksiceService vrstaOlaksiceService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idOlaksicaLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(olaksiceSearchableComboBox, text -> vrstaOlaksiceService.findAll(text, "naziv"), "idVrstaOlaksice", "naziv", "Desila se gre??ka prilikom dohva??anja vrsta olak??ica.", LanguageFormat.HR);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(vrstaOlaksiceErrorLabel, datumOdErrorLabel, datumDoErrorLabel);
    }

    @Override
    public void initData(OlaksicaDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdOlaksica())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi olak??icu zaposlenika");
            ControlUtils.setTextInLabel(idOlaksicaLabel, t, "idOlaksica");
            ControlUtils.setSelectedComboBoxItem(olaksiceSearchableComboBox, t, "idOlaksica");
            ControlUtils.setValueInClearableDatePicker(datumOdClearableDatePicker, t, "datumOd");
            ControlUtils.setValueInClearableDatePicker(datumDoClearableDatePicker, t, "datumDo");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj olak??icu zaposleniku");
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

            OlaksicaDto olaksicaDto = new OlaksicaDto();
            olaksicaDto.setIdOlaksica(ControlUtils.getIntegerFromLabel(idOlaksicaLabel, 0));
            olaksicaDto.setVrstaOlaksice(ControlUtils.getSelectedItemFromComboBox(olaksiceSearchableComboBox, id -> vrstaOlaksiceService.findById(id)));
            olaksicaDto.setZaposlenik(zaposlenikDetailsDto);
            olaksicaDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdClearableDatePicker));
            olaksicaDto.setDatumDo(ControlUtils.getValueFromClearableDatePicker(datumDoClearableDatePicker));

            Set<ConstraintViolation<OlaksicaDto>> constraintViolations = validator.validate(olaksicaDto);
            if (constraintViolations.isEmpty()) {
                olaksicaService.save(olaksicaDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Olak??ica zaposlenika je uspje??no spremljenja.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Gre??ka", "Desila se gre??ka prilikom spremanja olak??ice zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<OlaksicaDto>> constraintViolations) {
        FormUtils.showErrorMessage("vrstaOlaksice", constraintViolations, vrstaOlaksiceErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
        FormUtils.showErrorMessage("datumDo", constraintViolations, datumDoErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
