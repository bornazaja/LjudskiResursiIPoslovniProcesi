/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.ugovor;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.zaposlenik.ZaposlenikSourceResultDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
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
public class UgovoriZaposlenikaController implements Initializable, ControllerInterface<ZaposlenikSourceResultDto> {

    @FXML
    private Tab ugovoriORaduTab;

    @FXML
    private Tab ugovoriODjeluTab;

    @FXML
    private Tab studentskiUgovoriTab;

    @Autowired
    private FXMLManager fXMLManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void initData(ZaposlenikSourceResultDto t) {
        ugovoriORaduTab.setOnSelectionChanged((e) -> ugovoriORaduTab.setContent(fXMLManager.load(FxmlView.UGOVORI_O_RADU_ZAPOSLENIKA, t)));
        ugovoriODjeluTab.setOnSelectionChanged((e) -> ugovoriODjeluTab.setContent(fXMLManager.load(FxmlView.UGOVORI_O_DJELU_ZAPOSLENIKA, t)));
        studentskiUgovoriTab.setOnSelectionChanged((e) -> studentskiUgovoriTab.setContent(fXMLManager.load(FxmlView.STUDENTSKI_UGOVORI_ZAPOSLENIKA, t)));
        ugovoriORaduTab.setContent(fXMLManager.load(FxmlView.UGOVORI_O_RADU_ZAPOSLENIKA, t));
    }

}
