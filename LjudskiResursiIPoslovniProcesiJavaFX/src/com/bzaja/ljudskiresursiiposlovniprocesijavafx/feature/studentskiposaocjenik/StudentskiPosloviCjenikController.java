/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.studentskiposaocjenik;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.AppUtils;
import com.bzaja.myjavafxlibrary.util.MenuItemListBuilder;
import com.bzaja.myjavafxlibrary.util.DialogUtils;
import com.bzaja.myjavafxlibrary.util.FileChooserUtils;
import com.bzaja.myjavafxlibrary.springframework.manager.StageManager;
import com.bzaja.myjavafxlibrary.util.StageResult;
import com.bzaja.myjavafxlibrary.springframework.util.TableUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenikService;
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
        TableUtils.addColumns(studentskiPosloviCjenikPageableTableView, propertiesInfo, x -> createMenuItems(x), LanguageFormat.HR);
        studentskiPosloviCjenikPageableTableView.addMenuItem("Dodaj", (e) -> onDodajUrediClick(new StudentskiPosaoCjenikDto()));
        studentskiPosloviCjenikPageableTableView.addMenuItem("Osvježi", (e) -> refreshPageableTableView(false));
        studentskiPosloviCjenikPageableTableView.addMenuItem("Napredna pretraga", (e) -> onNaprednaPretragaClick());
        studentskiPosloviCjenikPageableTableView.addMenuItem("Eksportiraj u PDF", (e) -> onEksportirajUPdfClick());
        studentskiPosloviCjenikPageableTableView.setOnItemsPerPageChange((e) -> refreshPageableTableView(true));
        studentskiPosloviCjenikPageableTableView.setOnPreviousClick((e) -> TableUtils.doPreviousPage(studentskiPosaoCjenikPage, pageIndex, () -> refreshPageableTableView(true)));
        studentskiPosloviCjenikPageableTableView.setOnNextClick((e) -> TableUtils.doNextPage(studentskiPosaoCjenikPage, pageIndex, () -> refreshPageableTableView(true)));
    }

    private List<MenuItem> createMenuItems(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto) {
        return new MenuItemListBuilder()
                .addMenuItem("Uredi", () -> onDodajUrediClick(studentskiPosaoCjenikDto))
                .addMenuItem("Detalji", () -> onDetaljiClick(studentskiPosaoCjenikDto))
                .addMenuItem("Izbriši", () -> onIzbrisiClick(studentskiPosaoCjenikDto))
                .build();
    }

    private void refreshPageableTableView(boolean enableSearchAndSort) {
        DialogUtils.showLoadingDialog(() -> {
            queryCriteriaDto.setPageable(PageRequest.of(pageIndex.get(), studentskiPosloviCjenikPageableTableView.getItemsPerPageComboBox().getValue(), queryCriteriaDto.getPageable().getSort()));
            studentskiPosaoCjenikPage = !enableSearchAndSort ? studentskiPosaoCjenikService.findAll(queryCriteriaDto.getPageable()) : studentskiPosaoCjenikService.findAll(queryCriteriaDto);
            Platform.runLater(() -> TableUtils.refresh(studentskiPosloviCjenikPageableTableView, studentskiPosaoCjenikPage, pageIndex.get()));
        }, "Deslia se greška prilikom dohvaćanja studentskih poslova cjenik.");
    }

    private void onDodajUrediClick(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto) {
        DodajUrediStudentskiPosaoCjenikController dodajUrediStudentskiPosaoCjenikController = stageManager.showSecondaryStage(FxmlView.DODAJ_UREDI_STUDENTSKI_POSAO_CJENIK, studentskiPosaoCjenikDto, AppUtils.getCssPathables());
        if (dodajUrediStudentskiPosaoCjenikController.getStageResult().equals(StageResult.OK)) {
            refreshPageableTableView(true);
        }
    }

    private void onDetaljiClick(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto) {
        DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Detalji studentskog posla cjenik", studentskiPosaoCjenikDto, propertiesInfo, LanguageFormat.HR);
    }

    private void onIzbrisiClick(StudentskiPosaoCjenikDto studentskiPosaoCjenikDto) {
        DialogUtils.showConfirmationDialog("Jeste li sigurni da želite izbrisati ovaj studentski posao cjenik?", studentskiPosaoCjenikDto, propertiesInfo, () -> {
            studentskiPosaoCjenikService.delete(studentskiPosaoCjenikDto.getIdStudentskiPosaoCjenik());
            refreshPageableTableView(true);
        }, "Studentski posao cjenik je uspješno izbrisan.", "Desila se greška prilikom brisanja studentskog posla cjenik.", LanguageFormat.HR);
    }

    private void onNaprednaPretragaClick() {
        stageManager.showAdvancedSearchStage(FxmlView.NAPREDNA_PRETRAGA, AppUtils.getCssPathables(), BeanClass.STUDENTSKI_POSAO_CJENIK, propertiesInfo, queryCriteriaDto, qc -> () -> {
            queryCriteriaDto = qc;
            refreshPageableTableView(true);
        });
    }

    private void onEksportirajUPdfClick() {
        FileChooserUtils.showSaveDialog(root,
                StringUtils.generateFileNameWithCurrentDateTime("Studentski_Poslovi_Cjenik", LocalDateTimePattern.HR),
                f -> () -> GenericBeanReportPdf.create("STUDENTSKI POSLOVI CJENIK", studentskiPosaoCjenikService.findAll(), propertiesInfo, f.getAbsolutePath(), LanguageFormat.HR),
                "*.pdf",
                f -> String.format("Popis studentskih poslova cjenik je uspješno eksportiran u PDF. Putanja: %s.", f.getAbsolutePath()),
                "Desila se greška prilikom eksportiranja popisa studentski poslova cjenik u PDF.");
    }
}
