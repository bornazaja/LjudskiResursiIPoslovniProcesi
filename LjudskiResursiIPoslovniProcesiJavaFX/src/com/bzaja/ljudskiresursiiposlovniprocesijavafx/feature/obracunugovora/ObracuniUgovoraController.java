/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.obracunugovora;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FXMLManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
public class ObracuniUgovoraController implements Initializable {

    @FXML
    private Tab obracuniUgovoraORaduTab;

    @FXML
    private Tab obracuniUgovoraODjeluTab;

    @FXML
    private Tab obracuniStudentskihUgovoraTab;

    @Autowired
    private FXMLManager fXMLManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obracuniUgovoraORaduTab.setOnSelectionChanged((e) -> obracuniUgovoraORaduTab.setContent(fXMLManager.load(FxmlView.OBRACUNI_UGOVORA_O_RADU)));
        obracuniUgovoraODjeluTab.setOnSelectionChanged((e) -> obracuniUgovoraODjeluTab.setContent(fXMLManager.load(FxmlView.OBRACUNI_UGOVORA_O_DJELU)));
        obracuniStudentskihUgovoraTab.setOnSelectionChanged((e) -> obracuniStudentskihUgovoraTab.setContent(fXMLManager.load(FxmlView.OBRACUNI_STUDENTSKIH_UGOVORA)));
        obracuniUgovoraORaduTab.setContent(fXMLManager.load(FxmlView.OBRACUNI_UGOVORA_O_RADU));
    }

}
