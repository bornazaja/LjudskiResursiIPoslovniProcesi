/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.grad;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanClass;
import com.bzaja.myjavalibrary.pdf.GenericBeanReportPdf;
import com.bzaja.myjavalibrary.springframework.data.query.Operator;
import com.bzaja.myjavalibrary.springframework.data.query.QueryCriteriaDto;
import com.bzaja.myjavalibrary.util.PropertyInfoDto;
import com.bzaja.myjavalibrary.util.PropertyInfoUtils;
import com.bzaja.myjavalibrary.util.StringUtils;
import com.bzaja.myjavafxlibrary.control.PageableTableView;
import com.bzaja.myjavalibrary.util.LanguageFormat;
import com.bzaja.myjavalibrary.util.LocalDateTimePattern;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Controller
public class GradoviController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<GradDto> gradoviPageableTableView;

    @Autowired
    private GradService gradService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<GradDto> gradPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Grad", "Naziv", "Prirez", "Država");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idGrad", "naziv", "prirez", "drzava.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), gradoviPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idGrad"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(gradoviPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        gradoviPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new GradDto()));
        gradoviPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        gradoviPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        gradoviPageableTableView.addMenuItem("Eksporitraj u PDF", (e) -> onEksportirajUPdfClick());
        gradoviPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        gradoviPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(gradPage, pageIndex, () -> refreshPageableTableView(true)));
        gradoviPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(gradPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(GradDto gradDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(gradDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(gradDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(gradDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), gradoviPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            gradPage = !enableSearchAndSort ? gradService.findAll(queryCriteriaDto.getPageable()) : gradService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(gradoviPageableTableView, gradPage, pageIndex.get()));
        }, "Desila se greška prilikom dohvaćanja gradova.");
    }

    private void onDodajUrediClick(GradDto gradDto) {
        DodajUrediGradController dodajUrediGradController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_GRAD, gradDto, AppUtils.getCssPathables());
        if (dodajUrediGradController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(GradDto gradDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji grada", gradDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(GradDto gradDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj grad?", gradDto, propertiesInfo, () -> {
            gradService.delete(gradDto.getIdGrad());
            refreshPageableTableView(true);
        }, "Grad je uspješno izbrisan.", "Desila se greška prilikom brisanja grada.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.GRAD, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Gradovi", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("GRADOVI", gradService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis gradova je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa gradova u PDF.");
    }
}
