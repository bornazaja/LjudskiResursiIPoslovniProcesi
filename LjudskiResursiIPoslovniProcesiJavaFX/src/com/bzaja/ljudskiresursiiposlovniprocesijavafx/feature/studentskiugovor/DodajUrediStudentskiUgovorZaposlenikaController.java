/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.studentskiugovor;

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
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenikService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorService;
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
import javafx.scene.control.CheckBox;
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
public class DodajUrediStudentskiUgovorZaposlenikaController implements Initializable, ControllerInterface<StudentskiUgovorDto> {

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
    private SearchableComboBox<ItemDto> studentskiPosloviCjenikSearchableComboBox;

    @FXML
    private Label studentskiPosaoCjenikErrorLabel;

    @FXML
    private NumberField brojOdradjenihSatiNumberField;

    @FXML
    private Label brojOdradjenihSatiErrorLabel;

    @FXML
    private NumberField cijenaPoSatuNumberField;

    @FXML
    private Label valutaLabel1;

    @FXML
    private Label cijenaPoSatuErrorLabel;

    @FXML
    private NumberField dosadZaradjeniIznosUOvojGodiniNumberField;

    @FXML
    private Label valutaLabel2;

    @FXML
    private Label dosadZaradjeniIznosUOvojGodiniErrorLabel;

    @FXML
    private CheckBox jeObracunatCheckBox;

    @Autowired
    private StudentskiUgovorService studentskiUgovorService;

    @Autowired
    private RadniOdnosService radniOdnosService;

    @Autowired
    private RadnoMjestoService radnoMjestoService;

    @Autowired
    private StudentskiPosaoCjenikService studentskiPosaoCjenikService;

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
        ControlUtils.setupSearchableComboBox(radniOdnosiSearchableComboBox, "Desila se greška prilikom dovaćanja radnih odnosa", text -> radniOdnosService.findAll(text, VrsteUgovora.STUDENTSKI_UGOVOR.getId(), "naziv"), "idRadniOdnos", "naziv");
        ControlUtils.setupSearchableComboBox(radnaMjestaSearchableComboBox, "Desila se greška prilikom dohvaćanja radnih mjesta.", text -> radnoMjestoService.findAll(text, "naziv"), "idRadnoMjesto", "naziv");
        ControlUtils.setupSearchableComboBox(studentskiPosloviCjenikSearchableComboBox, "Desila se greška prilikom dohvaćanja studentskih poslova cjenik.", text -> studentskiPosaoCjenikService.findAll(text, "naziv", "cijenaPoSatu"), "idStudentskiPosaoCjenik", "naziv+cijenaPoSatu+valuta.naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(radniOdnosErrorLabel, radnoMjestoErrorLabel, datumOdErrorLabel, datumDoErrorLabel, studentskiPosaoCjenikErrorLabel, brojOdradjenihSatiErrorLabel, cijenaPoSatuErrorLabel, dosadZaradjeniIznosUOvojGodiniErrorLabel);
    }

    @Override
    public void initData(StudentskiUgovorDto t) {
        zaposlenikDetailsDto = t.getZaposlenik();
        if (NumberUtils.isPositive(t.getIdUgovor())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi studentski ugovor zaposlenika");
            ControlUtils.setTextInLabel(idUgovorLabel, t, "idUgovor");
            ControlUtils.setSelectedComboBoxItem(radniOdnosiSearchableComboBox, t, "radniOdnos.idRadniOdnos");
            ControlUtils.setSelectedComboBoxItem(radnaMjestaSearchableComboBox, t, "radnoMjesto.idRadnoMjesto");
            ControlUtils.setValueInClearableDatePicker(datumOdClearableDatePicker, t, "datumOd");
            ControlUtils.setValueInClearableDatePicker(datumDoClearableDatePicker, t, "datumDo");
            ControlUtils.setSelectedComboBoxItem(studentskiPosloviCjenikSearchableComboBox, t, "studentskiPosaoCjenik.idStudentskiPosaoCjenik");
            ControlUtils.setTextInTextField(brojOdradjenihSatiNumberField, t, "brojOdradjenihSati");
            ControlUtils.setTextInTextField(cijenaPoSatuNumberField, t, "cijenaPoSatu");
            ControlUtils.setTextInTextField(dosadZaradjeniIznosUOvojGodiniNumberField, t, "dosadZaradjeniIznosUOvojGodini");
            ControlUtils.setValueInCheckBox(jeObracunatCheckBox, t, "jeObracunat");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj studentski ugovor zaposlenika");
        }
        setupValuta(t);
    }

    private void setupValuta(StudentskiUgovorDto t) {
        AppUtils.runWithTryCatch("Desila se greška prilikom dohvaćanja valuta.", () -> {
            valutaDto = NumberUtils.isPositive(t.getIdUgovor()) ? (ValutaDto) BeanUtils.getPropertyValue(t, "valuta") : valutaService.findByDrzaveJeDomovinaTrue();
            ControlUtils.setTextInLabel(valutaLabel1, valutaDto.getNaziv());
            ControlUtils.setTextInLabel(valutaLabel2, valutaDto.getNaziv());
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

            StudentskiUgovorDto studentskiUgovorDto = new StudentskiUgovorDto();
            studentskiUgovorDto.setIdUgovor(ControlUtils.getIntegerFromLabel(idUgovorLabel, 0));
            studentskiUgovorDto.setRadniOdnos(ControlUtils.getSelectedItemFromComboBox(radniOdnosiSearchableComboBox, id -> radniOdnosService.findById(id)));
            studentskiUgovorDto.setZaposlenik(zaposlenikDetailsDto);
            studentskiUgovorDto.setRadnoMjesto(ControlUtils.getSelectedItemFromComboBox(radnaMjestaSearchableComboBox, id -> radnoMjestoService.findById(id)));
            studentskiUgovorDto.setDatumOd(ControlUtils.getValueFromClearableDatePicker(datumOdClearableDatePicker));
            studentskiUgovorDto.setDatumDo(ControlUtils.getValueFromClearableDatePicker(datumDoClearableDatePicker));
            studentskiUgovorDto.setStudentskiPosaoCjenik(ControlUtils.getSelectedItemFromComboBox(studentskiPosloviCjenikSearchableComboBox, id -> studentskiPosaoCjenikService.findById(id)));
            studentskiUgovorDto.setBrojOdradjenihSati(ControlUtils.getDoubleFromTextField(brojOdradjenihSatiNumberField, null));
            studentskiUgovorDto.setCijenaPoSatu(ControlUtils.getDoubleFromTextField(cijenaPoSatuNumberField, null));
            studentskiUgovorDto.setDosadZaradjeniIznosUOvojGodini(ControlUtils.getDoubleFromTextField(dosadZaradjeniIznosUOvojGodiniNumberField, null));
            studentskiUgovorDto.setValuta(valutaDto);
            studentskiUgovorDto.setJeObracunat(ControlUtils.getValueFromCheckBox(jeObracunatCheckBox));

            Set<ConstraintViolation<StudentskiUgovorDto>> constraintViolations = validator.validate(studentskiUgovorDto);

            if (constraintViolations.isEmpty()) {
                studentskiUgovorService.save(studentskiUgovorDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Studentski ugovor je uspješno spremljen.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja studentskog ugovora zaposlenika.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<StudentskiUgovorDto>> constraintViolations) {
        FormUtils.showErrorMessage("radniOdnos", constraintViolations, radniOdnosErrorLabel);
        FormUtils.showErrorMessage("radnoMjesto", constraintViolations, radnoMjestoErrorLabel);
        FormUtils.showErrorMessage("datumOd", constraintViolations, datumOdErrorLabel);
        FormUtils.showErrorMessage("datumDo", constraintViolations, datumDoErrorLabel);
        FormUtils.showErrorMessage("studentskiPosaoCjenik", constraintViolations, studentskiPosaoCjenikErrorLabel);
        FormUtils.showErrorMessage("brojOdradjenihSati", constraintViolations, brojOdradjenihSatiErrorLabel);
        FormUtils.showErrorMessage("cijenaPoSatu", constraintViolations, cijenaPoSatuErrorLabel);
        FormUtils.showErrorMessage("dosadZaradjeniIznosUOvojGodini", constraintViolations, dosadZaradjeniIznosUOvojGodiniErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
