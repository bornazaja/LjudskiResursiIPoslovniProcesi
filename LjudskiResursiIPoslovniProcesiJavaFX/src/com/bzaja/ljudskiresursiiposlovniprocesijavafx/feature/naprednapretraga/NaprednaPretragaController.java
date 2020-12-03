/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.naprednapretraga;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.HtmlFiles;
import com.bzaja.myjavafxlibrary.springframework.advancedsearch.AdvancedSearchControllerInterface;
import com.bzaja.myjavafxlibrary.springframework.advancedsearch.AdvancedSearchCtrl;
import com.bzaja.myjavafxlibrary.springframework.advancedsearch.AdvancedSearchDto;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavalibrary.springframework.data.query.QueryCriteriaDto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Controller
public class NaprednaPretragaController implements Initializable, AdvancedSearchControllerInterface {

    @FXML
    private AdvancedSearchCtrl advancedSearchCtrl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void loadInstructionsHtmlFileInWebView() {
        advancedSearchCtrl.getAdvancedSearchManager().loadInstructionsHtmlFileInWebView(HtmlFiles.NAPREDNA_PRETRAGA);
    }

    @Override
    public QueryCriteriaDto getQueryCriteria() {
        return advancedSearchCtrl.getAdvancedSearchManager().getQueryCriteria();
    }

    @Override
    public StageResult getStageResult() {
        return advancedSearchCtrl.getAdvancedSearchManager().getStageResult();
    }

    @Override
    public void initData(AdvancedSearchDto t) {
        advancedSearchCtrl.getAdvancedSearchManager().initData(t);
    }

}
