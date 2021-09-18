/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.davanje;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.davanje.DavanjeService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstadavanja.VrstaDavanjaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
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
public class DodajUrediDavanjeZaposlenikaController implements Initializable, ControllerInterface<DavanjeDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idDavanjeLabel;

    @FXML
    private SearchableComboBox<ItemDto> vrstaDavanjaSearchableTableView;

    @FXML
    private Label vrstaDavanjaErrorLabel;

    @FXML
    private ClearableDatePicker datumOdClearableDatePicker;

    @FXML
    private Label datumOdErrorLabel;

    @FXML
    private ClearableDatePicker datumDoClearableDatePicker;

    @FXML
    private Label datumDoErrorLabel;

    @Autowired
    private DavanjeService davanjeService;

    @Autowired
    private VrstaDavanjaService vrstaDavanjaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idDavanjeLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(vrstaDavanjaSearchableTableView, "Desila se greška prilikom dohvaćanja vrsta davanja.", text -> vrstaDavanjaService.findAll(text, "naziv"), "idVrstaDavanja", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(vrstaDavanjaErrorLabel, datumOdErrorLabel, datumDoErrorLabel);
    }

    @Override
    public void initData(DavanjeDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdDavanje())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi davanje zaposlenika");
            ControlUtils.setTextInLabel(idDavanjeLabel, t, "idDavanje");
            ControlUtils.setSelectedComboBoxItem(vrstaDavanjaSearchableTableView, t, "vrstaDavanja.idVrstaDavanja");
            ControlUtils.setValueInClearableDatePicker(datumOdClearableDatePicker, t, "datumOd");
            ControlUtils.setValueInClearableDatePicker(datumDoClearableDatePicker, t, "datumDo");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj davanje zaposlenika");
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

            DavanjeDto davanjeDto = new DavanjeDto();
            davanjeDto.setIdDavanje(ControlUtils.getIntegerFromLabel(idDavanjeLabel, 0));
            davanjeDto.setVrstaDavanja(ControlUtils.getSelectedItemFromComboBox(vrstaDavanjaSearchableTableView, id -> vrstaDavanjaService.findById(id)));
            davanjeDto.setZaposlenik(zaposlenikDetailsDto);
            davanjeDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdClearableDatePicker));
            davanjeDto.setDatumDo(ControlUtils.getValueFromClearableDatePicker(datumDoClearableDatePicker));

            Set<ConstraintViolation<DavanjeDto>> constraintViolations = validator.validate(davanjeDto);

            if (constraintViolations.isEmpty()) {
                davanjeService.save(davanjeDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Davanja zaposlenika su uspješno spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja davanja zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<DavanjeDto>> constraintViolations) {
        FormUtils.showErrorMessage("vrstaDavanja", constraintViolations, vrstaDavanjaErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
        FormUtils.showErrorMessage("datumDo", constraintViolations, datumDoErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
