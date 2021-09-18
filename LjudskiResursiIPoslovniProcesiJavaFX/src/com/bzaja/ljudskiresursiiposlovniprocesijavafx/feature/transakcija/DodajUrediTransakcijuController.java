/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FormUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.kategorijatransakcije.KategorijaTransakcijeService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.valuta.ValutaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.vrstatransakcije.VrstaTransakcijeService;
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
public class DodajUrediTransakcijuController implements Initializable, ControllerInterface<TransakcijaDto> {

    @FXML
    private ScrollPane root;

    @FXML
    private Label naslovLabel;

    @FXML
    private Label idTransakcija;

    @FXML
    private SearchableComboBox<ItemDto> poslovniPartneriSearchableComboBox;

    @FXML
    private Label poslovniPartnerErrorMessage;

    @FXML
    private TextField opisTextField;

    @FXML
    private Label opisErrorMessage;

    @FXML
    private NumberField iznosNumberField;

    @FXML
    private Label iznosErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> valuteSearchableComboBox;

    @FXML
    private Label valutaErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> vrsteTransackijaSearchableComboBox;

    @FXML
    private Label vrstaTransakcijeErrorLabel;

    @FXML
    private SearchableComboBox<ItemDto> kategorijeTransakcijaSearchableComboBox;

    @FXML
    private Label kategorijaTransakcijeErrorLabel;

    @FXML
    private ClearableDatePicker datumTransakcijeClearableDatePicker;

    @FXML
    private Label datumTransakcijeErrorLabel;

    @Autowired
    private TransakcijaService transakcijaService;

    @Autowired
    private PoslovniPartnerService poslovniPartnerService;

    @Autowired
    private ValutaService valutaService;

    @Autowired
    private VrstaTransakcijeService vrstaTransakcijeService;

    @Autowired
    private KategorijaTransakcijeService kategorijaTransakcijeService;

    @Autowired
    private Validator validator;

    private StageResult stageResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        idTransakcija.setVisible(false);
        ControlUtils.setupSearchableComboBox(poslovniPartneriSearchableComboBox, "Desila se greška prilikom dohvaćanja poslovnih partnera.", text -> poslovniPartnerService.findAll(text, "naziv"), "idPoslovniPartner", "naziv");
        ControlUtils.setupSearchableComboBox(valuteSearchableComboBox, "Desila se greška prilikom dohvaćanja valuta.", text -> valutaService.findAll(text, "naziv"), "idValuta", "naziv+srednjiTecaj");
        ControlUtils.setupSearchableComboBox(vrsteTransackijaSearchableComboBox, "Desila se greška prilikom dohvaćanja vrsta transakcija.", text -> vrstaTransakcijeService.findAll(text, "naziv"), "idVrstaTransakcije", "naziv");
        ControlUtils.setupSearchableComboBox(kategorijeTransakcijaSearchableComboBox, "Desila se greška prilikom dohvačanja kategorija transakcija.", text -> kategorijaTransakcijeService.findAll(text, "naziv"), "idKategorijaTransakcije", "naziv");
        clearErrorLabels();
    }

    private void clearErrorLabels() {
        ControlUtils.clearLabelsText(poslovniPartnerErrorMessage, opisErrorMessage, iznosErrorLabel, valutaErrorLabel, vrstaTransakcijeErrorLabel, kategorijaTransakcijeErrorLabel, datumTransakcijeErrorLabel);
    }

    @Override
    public void initData(TransakcijaDto t) {
        if (NumberUtils.isPositive(t.getIdTransakcija())) {
            ControlUtils.setTextInLabel(naslovLabel, "Uredi transakciju");
            ControlUtils.setTextInLabel(idTransakcija, t, "idTransakcija");
            ControlUtils.setSelectedComboBoxItem(poslovniPartneriSearchableComboBox, t, "poslovniPartner.idPoslovniPartner");
            ControlUtils.setTextInTextField(opisTextField, t, "opis");
            ControlUtils.setTextInTextField(iznosNumberField, t, "iznos");
            ControlUtils.setSelectedComboBoxItem(valuteSearchableComboBox, t, "valuta.idValuta");
            ControlUtils.setSelectedComboBoxItem(vrsteTransackijaSearchableComboBox, t, "vrstaTransakcije.idVrstaTransakcije");
            ControlUtils.setSelectedComboBoxItem(kategorijeTransakcijaSearchableComboBox, t, "kategorijaTransakcije.idKategorijaTransakcije");
            ControlUtils.setValueInClearableDatePicker(datumTransakcijeClearableDatePicker, t, "datumTransakcije");
        } else {
            ControlUtils.setTextInLabel(naslovLabel, "Dodaj transakciju");
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
            ValutaDto valutaDto = ControlUtils.getSelectedItemFromComboBox(valuteSearchableComboBox, id -> valutaService.findById(id));
            Double srednjiTecaj = valutaDto != null ? valutaDto.getSrednjiTecaj() : null;

            TransakcijaDto transakcijaDto = new TransakcijaDto();
            transakcijaDto.setIdTransakcija(ControlUtils.getIntegerFromLabel(idTransakcija, 0));
            transakcijaDto.setPoslovniPartner(ControlUtils.getSelectedItemFromComboBox(poslovniPartneriSearchableComboBox, id -> poslovniPartnerService.findById(id)));
            transakcijaDto.setOpis(ControlUtils.getTextFromTextField(opisTextField));
            transakcijaDto.setIznos(ControlUtils.getDoubleFromTextField(iznosNumberField, null));
            transakcijaDto.setValuta(valutaDto);
            transakcijaDto.setSrednjiTecaj(srednjiTecaj);
            transakcijaDto.setVrstaTransakcije(ControlUtils.getSelectedItemFromComboBox(vrsteTransackijaSearchableComboBox, id -> vrstaTransakcijeService.findById(id)));
            transakcijaDto.setKategorijaTransakcije(ControlUtils.getSelectedItemFromComboBox(kategorijeTransakcijaSearchableComboBox, id -> kategorijaTransakcijeService.findById(id)));
            transakcijaDto.setDatumTransakcije(ControlUtils.getValueFromClearableDatePicker(datumTransakcijeClearableDatePicker));

            Set<ConstraintViolation<TransakcijaDto>> constraintViolations = validator.validate(transakcijaDto);

            if (constraintViolations.isEmpty()) {
                transakcijaService.save(transakcijaDto);
                stageResult = StageResult.OK;
                DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", "Transakcija je uspješno spremljena.");
                NodeUtils.closeCurrentStageByNode(root);
            } else {
                showErrorMessages(constraintViolations);
            }
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Desila se greška prilikom spremanja transakcije.");
        }
    }

    private void showErrorMessages(Set<ConstraintViolation<TransakcijaDto>> constraintViolations) {
        FormUtils.showErrorMessage("poslovniPartner", constraintViolations, poslovniPartnerErrorMessage);
        FormUtils.showErrorMessage("opis", constraintViolations, opisErrorMessage);
        FormUtils.showErrorMessage("iznos", constraintViolations, iznosErrorLabel);
        FormUtils.showErrorMessage("valuta", constraintViolations, valutaErrorLabel);
        FormUtils.showErrorMessage("vrstaTransakcije", constraintViolations, vrstaTransakcijeErrorLabel);
        FormUtils.showErrorMessage("kategorijaTransakcije", constraintViolations, kategorijaTransakcijeErrorLabel);
        FormUtils.showErrorMessage("datumTransakcije", constraintViolations, datumTransakcijeErrorLabel);
    }

    public StageResult getStageResult() {
        return stageResult;
    }
}
