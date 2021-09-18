/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.studentskiposaocjenik;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ContextMenuItemListBuilder;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.DialogUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.FileChooserUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenikService;
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
public class StudentskiPosloviCjenikController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private PageableTableView<StudentskiPosaoCjenikDto> studentskiPosloviCjenikPageableTableView;

    @Autowired
    private StudentskiPosaoCjenikService studentskiPosaoCjenikService;

    @Autowired
    private StageManager stageManager;

    private SimpleIntegerProperty pageIndex;
    private List<PropertyInfoDto> propertiesInfo;
    private QueryCriteriaDto queryCriteriaDto;
    private Page<StudentskiPosaoCjenikDto> studentskiPosaoCjenikPage;

    private static final List<String> DISPLAY_NAMES = Arrays.asList("ID Studentski posao cjenik", "Naziv", "Cijena po satu", "Valuta");
    private static final List<String> PROPERTY_NAMES = Arrays.asList("idStudentskiPosaoCjenik", "naziv", "cijenaPoSatu", "valuta.naziv");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pageIndex = new SimpleIntegerProperty(0);
        propertiesInfo = PropertyInfoUtils.toPropertiesInfo(DISPLAY_NAMES, PROPERTY_NAMES);
        queryCriteriaDto = new QueryCriteriaDto(Operator.AND, new ArrayList<>(), PageRequest.of(pageIndex.get(), studentskiPosloviCjenikPageableTableView.getItemsPerPageComboBox().getValue(), Sort.Direction.ASC, "idStudentskiPosaoCjenik"));
        initPageableTableView();
        refreshPageableTableView(false);
    }

    private void initPageableTableView() {
        TableUtils.addColumns(studentskiPosloviCjenikPageableTableView, propertiesInfo, x -> TableUtils.triggerContextMenu(x, createContextMenuItems(x.getT())));
        studentskiPosloviCjenikPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new StudentskiPosaoCjenikDto()));
        studentskiPosloviCjenikPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        studentskiPosloviCjenikPageableTableView.addMenuItem("Pretraživanje i sortiranje", (e) -> onPretrazivanjeISortiranjeClick());
        studentskiPosloviCjenikPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        studentskiPosloviCjenikPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        studentskiPosloviCjenikPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(studentskiPosaoCjenikPage, pageIndex, () -> refreshPageableTableView(true)));
        studentskiPosloviCjenikPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(studentskiPosaoCjenikPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createContextMenuItems(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto) {
        return new ContextMenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(studentskiPosaoCjenikDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(studentskiPosaoCjenikDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(studentskiPosaoCjenikDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog("Deslia se greška prilikom dohvaćanja studentskih poslova cjenik.", () -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), studentskiPosloviCjenikPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            studentskiPosaoCjenikPage = !enableSearchAndSort ? studentskiPosaoCjenikService.findAll(queryCriteriaDto.getPageable()) : studentskiPosaoCjenikService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(studentskiPosloviCjenikPageableTableView, studentskiPosaoCjenikPage, pageIndex.get()));
        });
    }

    private void onDodajUrediClick(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto) {
        DodajUrediStudentskiPosaoCjenikController dodajUrediStudentskiPosaoCjenikController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_STUDENTSKI_POSAO_CJENIK, studentskiPosaoCjenikDto);
        if (dodajUrediStudentskiPosaoCjenikController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji studentskog posla cjenik", studentskiPosaoCjenikDto, propertiesInfo);
    }

    private void onIzbrisiClick(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj studentski posao cjenik?", studentskiPosaoCjenikDto, propertiesInfo, "Studentski posao cjenik je uspješno izbrisan.", "Desila se greška prilikom brisanja studentskog posla cjenik.", () -> {
            studentskiPosaoCjenikService.delete(studentskiPosaoCjenikDto.getIdStudentskiPosaoCjenik());
            refreshPageableTableView(true);
        });
    }

    private void onPretrazivanjeISortiranjeClick() {
        stageManager.showPretrazivanjeISortiranjeStage("StudentskiPosaoCjenik", propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root, StringUtils.generateFileNameWithCurrentDateTime("Studentski_Poslovi_Cjenik"), f -> () -> GenericBeanReportPdf.create("STUDENTSKI POSLOVI CJENIK", studentskiPosaoCjenikService.findAll(), propertiesInfo, f.getAbsolutePath()), "*.pdf");
    }
}
