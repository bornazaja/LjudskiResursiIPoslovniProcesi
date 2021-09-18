/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.transakcija;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijaService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.transakcija.TransakcijeZadaniPeriodPdfReport;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.GenericBeanReportPdf;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.StringUtils;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
public class EksportirajTransakcijeUPdfController implements Initializable, ControllerInterface<List<PropertyInfoDto>> {

    @FXML
    private AnchorPane root;

    @FXML
    private RadioButton popisSvihTransakcijaRadioButton;

    @FXML
    private ToggleGroup vrsteExportiranjaUPdfToggleGroup;

    @FXML
    private RadioButton izvjestajTransakcijaUZadanomPerioduRadioButton;

    @FXML
    private DatePicker datumOdDatePicker;

    @FXML
    private DatePicker datumDoDatePicker;

    @Autowired
    private TransakcijaService transakcijaService;

    @Autowired
    private TransakcijeZadaniPeriodPdfReport transakcijeZadaniPeriodPdfReport;

    private List<PropertyInfoDto> propertiesInfo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initData(List<PropertyInfoDto> t) {
        propertiesInfo = t;
    }

    @FXML
    private void onEksportirajfClick(ActionEvent event) {
        if (popisSvihTransakcijaRadioButton.isSelected()) {
            FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Transakcije"), f -> () -> GenericBeanReportPdf.create("TRANSAKCIJE", transakcijaService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
        }

        if (izvjestajTransakcijaUZadanomPerioduRadioButton.isSelected()) {
            LocalDate datumOd = ControlUtils.getValueFromDatePicker(datumOdDatePicker);
            LocalDate datumDo = ControlUtils.getValueFromDatePicker(datumDoDatePicker);

            if (datumOd != null && datumDo != null) {
                FileChooserUtils.showSaveDialog(root, String.format("Transakcije_U_Periodu_Od_%s_Do_%s", datumOd.toString(), datumDo.toString()), f -> () -> transakcijeZadaniPeriodPdfReport.create(datumOd, datumDo, f.getAbsolutePath()), "*.pdf");
            } else {
                DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", "Datum od i datum do su obavezni.");
            }
        }
    }

    @FXML
    private void onIzvjestajTransakcijaUZadanomPerioduClick(ActionEvent event) {
        datumOdDatePicker.setDisable(false);
        datumDoDatePicker.setDisable(false);
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onPopisSvihTransakcijaClick(ActionEvent event) {
        datumOdDatePicker.setDisable(true);
        datumDoDatePicker.setDisable(true);
    }
}
