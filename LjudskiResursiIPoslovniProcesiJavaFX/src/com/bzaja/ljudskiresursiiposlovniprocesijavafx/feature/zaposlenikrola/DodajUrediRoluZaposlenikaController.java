/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenikrola;

import com.bzaja.myjavafxlibrary.util.ControlUtils;
import com.bzaja.myjavafxlibrary.util.ControllerInterface;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FormUtils;
import com.bzaja.myjavafxlibrary.util.ItemDto;
import com.bzaja.myjavafxlibrary.util.NodeUtils;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.rola.RolaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikDetailsDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola.ZaposlenikRolaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenikrola.ZaposlenikRolaService;
import com.bzaja.myjavalibrary.util.NumberUtils;
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
public class DodajUrediRoluZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikRolaDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idZaposlenikRolaLabel;

    @FXML
    private SearchableComboBox<ItemDto> roleSearchableComboBox;

    @FXML
    private Label rolaErrorLabel;

    @Autowired
    private ZaposlenikRolaService zaposlenikRolaService;

    @Autowired
    private RolaService rolaService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;
    private ZaposlenikDetailsDto zaposlenikDetailsDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idZaposlenikRolaLabel.setVisible(false);
        ControlUtils.setupSearchableComboBox(roleSearchableComboBox, text -> rolaService.findAll(text, "naziv"), "idRola", "naziv", "Desila se gre??ka prilikom dohva??anja rola.", LanguageFormat.HR);
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(rolaErrorLabel);
    }

    @Override
    public void initData(ZaposlenikRolaDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdZaposlenikRola())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi rolu zaposlenika");
            ControlUtils.setTextInLabel(idZaposlenikRolaLabel, t, "idZaposlenikRola");
            ControlUtils.setSelectedComboBoxItem(roleSearchableComboBox, t, "rola.idRola");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj rolu zaposleniku");
        }
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onSpremiClick(ActionEvent event) {
        try {
            ZaposlenikRolaDto zaposlenikRolaDto = new ZaposlenikRolaDto();
            zaposlenikRolaDto.setIdZaposlenikRola(ControlUtils.getIntegerFromLabel(idZaposlenikRolaLabel, 0));
            zaposlenikRolaDto.setRola(ControlUtils.getSelectedItemFromComboBox(roleSearchableComboBox, id -> rolaService.findById(id)));
            zaposlenikRolaDto.setZaposlenik(zaposlenikDetailsDto);

            Set<ConstraintViolation<ZaposlenikRolaDto>> constraintViolations = validator.validate(zaposlenikRolaDto);

            if (constraintViolations.isEmpty()) {
                zaposlenikRolaService.save(zaposlenikRolaDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Rola zaposlenika je uspje??no spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Gre??ka", "Desila se gre??ka prilikom spremanja role zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<ZaposlenikRolaDto>> constraintViolations) {
        FormUtils.showErrorMessage("rola", constraintViolations, rolaErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
