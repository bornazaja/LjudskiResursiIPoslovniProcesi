/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.prebivaliste;

import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FormUtils;
import com.bzaja.myjavafxlibrary.util.ItemDto;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.PrebivalisteDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.prebivaliste.PrebivalisteService;
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
public class DodajUrediPrebivalisteZaposlenikaController implements Initializable, ControllerInterface<PrebivalisteDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idPrebivalisteLabel;

    @FXML
    private TextField ulicaTextField;

    @FXML
    private Label ulicaErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> gradoviSearchableComboBox;

    @FXML
    private Label gradErrorLabel;

    @FXML
    private ClearableDatePicker datumOdSearchableDatePicker;

    @FXML
    private Label datumOdErrorLabel;

    @Autowired
    private PrebivalisteService prebivalisteService;

    @Autowired
    private GradService gradService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idPrebivalisteLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(gradoviSearchableComboBox, text -> gradService.findAllByDrzavaJeDomovinaTrue(text, "naziv"), "idGrad", "naziv", "Desila se greška prilikom dohvaćanja gradova.", LanguageFormat.HR);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(ulicaErrorLabel, gradErrorLabel, datumOdErrorLabel);
    }

    @Override
    public void initData(PrebivalisteDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdPrebivaliste())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi prebivalište zaposlenika");
            ControlUtils.setTextInLabel(idPrebivalisteLabel, t, "idPrebivaliste");
            ControlUtils.setTextInTextField(ulicaTextField, t, "ulica");
            ControlUtils.setSelectedComboBoxItem(gradoviSearchableComboBox, t, "grad.idGrad");
            ControlUtils.setValueInClearableDatePicker(datumOdSearchableDatePicker, t, "datumOd");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj prebivalište zaposlenika");
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

            PrebivalisteDto prebivalisteDto = new PrebivalisteDto();
            prebivalisteDto.setIdPrebivaliste(ControlUtils.getIntegerFromLabel(idPrebivalisteLabel, 0));
            prebivalisteDto.setUlica(ControlUtils.getTextFromTextField(ulicaTextField));
            prebivalisteDto.setGrad(ControlUtils.getSelectedItemFromComboBox(gradoviSearchableComboBox, id -> gradService.findById(id)));
            prebivalisteDto.setZaposlenik(zaposlenikDetailsDto);
            prebivalisteDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdSearchableDatePicker));

            Set<ConstraintViolation<PrebivalisteDto>> constraintViolations = validator.validate(prebivalisteDto);

            if (constraintViolations.isEmpty()) {
                prebivalisteService.save(prebivalisteDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Prebivalište zaposlenika je uspješno spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja prebivališta zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<PrebivalisteDto>> constraintViolations) {
        FormUtils.showErrorMessage("ulica", constraintViolations, ulicaErrorLabel);
        FormUtils.showErrorMessage("grad", constraintViolations, gradErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
