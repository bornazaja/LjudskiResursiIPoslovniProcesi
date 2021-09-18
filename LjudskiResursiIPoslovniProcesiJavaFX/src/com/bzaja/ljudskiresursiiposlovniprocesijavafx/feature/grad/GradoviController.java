/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.grad;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.grad.GradService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.pdf.GenericBeanReportPdf;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.StringUtils;
import com.bzaja.myjavafxlibrary.control.PageableTableView;
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
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
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
        TableUtils.addColumns(gradoviPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        gradoviPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new GradDto()));
        gradoviPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        gradoviPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISoriranjeClick());
        gradoviPageableTableView.addMenuItem("Eksporitraj u PDF", (e) -> onEksportirajUPdfClick());
        gradoviPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        gradoviPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(gradPage, pageIndex, () -> refreshPageableTableView(true)));
        gradoviPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(gradPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(GradDto gradDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(gradDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(gradDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(gradDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Desila se greška prilikom dohvaćanja gradova", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), gradoviPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            gradPage = !enableSearchAndSort ? gradService.findAll(queryCriteriaDto.getPageable()) : gradService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(gradoviPageableTableView, gradPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(GradDto gradDto) {
        DodajUrediGradController dodajUrediGradController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_GRAD, gradDto);
        if (dodajUrediGradController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(GradDto gradDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji grada", gradDto, propertiesInfo);
    }

    private void onIzbrisiClick(GradDto gradDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj grad?", gradDto, propertiesInfo, "Grad je uspješno izbrisan.", "Desila se greška prilikom brisanja grada.", () -> {
            gradService.delete(gradDto.getIdGrad());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISoriranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("Grad", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Gradovi"), f -> () -> GenericBeanReportPdf.create("GRADOVI", gradService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
