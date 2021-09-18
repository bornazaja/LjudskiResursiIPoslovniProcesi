/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.pretrazivanjeisortiranje;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.HtmlFiles;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControlUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ControllerInterface;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.ItemDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.NodeUtils;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageResult;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.Operator;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchCriteriaType;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperation;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.SearchOperationUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.MapUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Borna
 */
@Component
public class PretrazivanjeISortiranjeController implements Initializable, ControllerInterface<PretrazivanjeISortiranjeDto> {

    @FXML
    private AnchorPane root;

    @FXML
    private ComboBox<ItemDto> operatoriComboBox;

    @FXML
    private VBox filtriVBox;

    @FXML
    private Label trenutniBrojFilteraLabel;

    @FXML
    private ComboBox<ItemDto> stupciZaSortiranjeComboBox;

    @FXML
    private ComboBox<ItemDto> smjeroviSortiranjaComboBox;

    @FXML
    private Button dodajFilterButton;

    @FXML
    private Button obrisiSveButton;

    @FXML
    private WebView uputeWebView;

    private static final String VRSTE_KRITERIJA_COMBOBOX_ID = "vrsteKriterijaComboBox";
    private static final String STUPCI_ZA_PRETRAZIVANJE_COMBOBOX_ID = "stupciZaPretrazivanjeComboBox";
    private static final String OPERACIJE_ZA_PRETRAZIVANJE_COMBOBOX_ID = "operacijeZaPretrazivanjeComboBox";
    private static final String VRIJEDNOST_TEXT_FIELD_ID = "vrijednostTextField";
    private static final int MAX_NUMBER_OF_FILTERS = 10;

    private StageResult stageResult;
    private PretrazivanjeISortiranjeDto pretrazivanjeISortiranjeDto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stageResult = StageResult.CANCEL;
        initUputeWebView();
        setupFiltriBoxChildrenListener();
    }

    private void initUputeWebView() {
        URL url = this.getClass().getResource(HtmlFiles.UPUTE_ZA_PRETRAZIVANJE_I_SORTIRANJE.getPath());
        uputeWebView.getEngine().load(url.toString());
    }

    private void setupFiltriBoxChildrenListener() {
        filtriVBox.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> change) {
                if (filtriVBox.getChildren().size() == MAX_NUMBER_OF_FILTERS) {
                    dodajFilterButton.setDisable(true);
                } else {
                    dodajFilterButton.setDisable(false);
                }

                if (filtriVBox.getChildren().isEmpty()) {
                    obrisiSveButton.setDisable(true);
                } else {
                    obrisiSveButton.setDisable(false);
                }
            }
        });
    }

    @Override
    public void initData(PretrazivanjeISortiranjeDto t) {
        pretrazivanjeISortiranjeDto = t;

        Sort.Order order = t.getQueryCriteria().getPageable().getSort().get().findFirst().orElse(null);

        initOperatoriComboBox(t.getQueryCriteria().getOperator());
        initStupciZaSortiranjaComboBox(order);
        initSmjeroviSortiranjaComboBox(order);
        initSearchFilters(t);
        refreshTrenutniBrojFiltaraLabel();
    }

    private void initOperatoriComboBox(Operator operator) {
        Map<String, String> map = MapUtils.toStringString(MapUtils.toEnumMap(Operator.values()));
        ControlUtils.fillComboBox(operatoriComboBox, map, 0, false);
        ControlUtils.setSelectedComboBoxItem(operatoriComboBox, operator.toString());
    }

    private void initStupciZaSortiranjaComboBox(Sort.Order order) {
        stupciZaSortiranjeComboBox.setPrefWidth(Integer.MAX_VALUE);
        ControlUtils.fillComboBox(stupciZaSortiranjeComboBox, pretrazivanjeISortiranjeDto.getPropertiesInfo(), "propertyName", "displayName", 0, false);
        ControlUtils.setSelectedComboBoxItem(stupciZaSortiranjeComboBox, order.getProperty());
    }

    private void initSmjeroviSortiranjaComboBox(Sort.Order order) {
        smjeroviSortiranjaComboBox.setPrefWidth(Integer.MAX_VALUE);
        List<String> keys = Arrays.asList(Sort.Direction.ASC.toString(), Sort.Direction.DESC.toString());
        List<String> values = Arrays.asList("Uzlazno", "Silazno");
        ControlUtils.fillComboBox(smjeroviSortiranjaComboBox, keys, values, 0, false);
        ControlUtils.setSelectedComboBoxItem(smjeroviSortiranjaComboBox, order.getDirection().toString());
    }

    private void initSearchFilters(PretrazivanjeISortiranjeDto t) {
        List<SearchCriteriaDto> searchCriterias = t.getQueryCriteria().getSearchCriterias();

        if (!searchCriterias.isEmpty()) {
            searchCriterias.forEach((searchCriteria) -> {
                filtriVBox.getChildren().add(createHBoxFilter(searchCriteria.getSearchCriteriaType(), searchCriteria.getColumn(), searchCriteria.getSearchOperation(), searchCriteria.getValue()));
            });
        } else {
            obrisiSveButton.setDisable(true);
        }
    }

    private void refreshTrenutniBrojFiltaraLabel() {
        int trenutniBrojFiltera = filtriVBox.getChildren().size();
        trenutniBrojFilteraLabel.setText(String.format("%d/%d", trenutniBrojFiltera, MAX_NUMBER_OF_FILTERS));
    }

    private HBox createHBoxFilter(SearchCriteriaType searchCriteriaType, String column, SearchOperation searchOperation, Object value) {
        HBox filterHBox = new HBox(20);
        filterHBox.setPadding(new Insets(20));
        filterHBox.setSpacing(10);

        ComboBox<ItemDto> vrsteKriterijaComboBox = createVrsteKriterijaComboBox(searchCriteriaType);

        ComboBox<ItemDto> stupciZaPretrazivanjeComboBox = createStupciZaPretraziavnjeComboBox(column);

        ComboBox<ItemDto> operacijeZaPretrazivanjeComboBox = createOperacijeZaPretrazivanjeComboBox(column, searchOperation);

        setOnStupciZaPretrazivanjeChanged(stupciZaPretrazivanjeComboBox, operacijeZaPretrazivanjeComboBox);

        TextField vrijednostZaPretrazivanjeTextField = createVrijednostTextField(value);

        Button removeButton = createRemoveButton(filterHBox);

        List<Node> nodes = Arrays.asList(vrsteKriterijaComboBox, stupciZaPretrazivanjeComboBox, operacijeZaPretrazivanjeComboBox, vrijednostZaPretrazivanjeTextField, removeButton);

        filterHBox.getChildren().addAll(nodes);

        return filterHBox;
    }

    private ComboBox<ItemDto> createVrsteKriterijaComboBox(SearchCriteriaType searchCriteriaType) {
        ComboBox<ItemDto> vrsteKriterijaComboBox = createComboBox(VRSTE_KRITERIJA_COMBOBOX_ID, "Vrsta kriterija");

        Map<String, String> map = MapUtils.toStringString(MapUtils.toEnumMap(SearchCriteriaType.values()));
        ControlUtils.fillComboBox(vrsteKriterijaComboBox, map, 0, true);

        if (searchCriteriaType != null) {
            vrsteKriterijaComboBox.getSelectionModel().select(new ItemDto(searchCriteriaType.toString()));
        }

        return vrsteKriterijaComboBox;
    }

    private ComboBox<ItemDto> createStupciZaPretraziavnjeComboBox(String column) {
        ComboBox<ItemDto> stupciZaPretrazivanjeComboBox = createComboBox(STUPCI_ZA_PRETRAZIVANJE_COMBOBOX_ID, "Stupac");
        ControlUtils.fillComboBox(stupciZaPretrazivanjeComboBox, pretrazivanjeISortiranjeDto.getPropertiesInfo(), "propertyName", "displayName", 0, true);

        if (column != null) {
            stupciZaPretrazivanjeComboBox.getSelectionModel().select(new ItemDto(column));
        }

        return stupciZaPretrazivanjeComboBox;
    }

    private ComboBox<ItemDto> createOperacijeZaPretrazivanjeComboBox(String column, SearchOperation searchOperation) {
        ComboBox<ItemDto> operacijeZaPretrazivanjeComboBox = createComboBox(OPERACIJE_ZA_PRETRAZIVANJE_COMBOBOX_ID, "Operacija");

        if (column != null && searchOperation != null) {
            Map<String, String> map = MapUtils.toStringString(MapUtils.toEnumMap(SearchOperationUtils.getSearchOperationsByClassAndPropertyName(pretrazivanjeISortiranjeDto.getClassName(), column)));
            ControlUtils.fillComboBox(operacijeZaPretrazivanjeComboBox, map, 0, true);
            operacijeZaPretrazivanjeComboBox.getSelectionModel().select(new ItemDto(searchOperation.toString()));
        } else {
            ControlUtils.fillComboBoxWithOnlyAChooseOption(operacijeZaPretrazivanjeComboBox);
        }

        return operacijeZaPretrazivanjeComboBox;
    }

    private void setOnStupciZaPretrazivanjeChanged(ComboBox<ItemDto> stupciZaPretrazivanjeComboBox, ComboBox<ItemDto> operacijeZaPretrazivanjeComboBox) {
        stupciZaPretrazivanjeComboBox.setOnAction((e) -> {
            if (!ControlUtils.hasComboBoxSelectedDefaultValue(stupciZaPretrazivanjeComboBox)) {
                Map<String, String> map = MapUtils.toStringString(MapUtils.toEnumMap(SearchOperationUtils.getSearchOperationsByClassAndPropertyName(pretrazivanjeISortiranjeDto.getClassName(), stupciZaPretrazivanjeComboBox.getValue().getKey())));
                ControlUtils.fillComboBox(operacijeZaPretrazivanjeComboBox, map, 0, true);
            } else {
                ControlUtils.fillComboBoxWithOnlyAChooseOption(operacijeZaPretrazivanjeComboBox);
            }
        });
    }

    private ComboBox<ItemDto> createComboBox(String id, String promptText) {
        ComboBox<ItemDto> comboBox = new ComboBox<>();
        comboBox.setId(id);
        comboBox.setPromptText(promptText);
        comboBox.setPrefWidth(Integer.MAX_VALUE);
        comboBox.setMaxWidth(Integer.MAX_VALUE);
        HBox.setHgrow(comboBox, Priority.ALWAYS);
        return comboBox;
    }

    private TextField createVrijednostTextField(Object value) {
        TextField vrijednostTextField = createTextField(VRIJEDNOST_TEXT_FIELD_ID);

        if (value != null) {
            vrijednostTextField.setText(value.toString());
        }

        return vrijednostTextField;
    }

    private TextField createTextField(String id) {
        TextField textField = new TextField();
        textField.setId(id);
        textField.setPrefWidth(Integer.MAX_VALUE);
        HBox.setHgrow(textField, Priority.ALWAYS);
        return textField;
    }

    private Button createRemoveButton(HBox hBox) {
        Button removeButton = new Button("X");
        removeButton.setOnAction((e) -> {
            filtriVBox.getChildren().remove(hBox);
            refreshTrenutniBrojFiltaraLabel();
        });
        return removeButton;
    }

    @FXML
    private void onDodajFilterClick(ActionEvent event) {
        filtriVBox.getChildren().add(createHBoxFilter(null, null, null, null));
        refreshTrenutniBrojFiltaraLabel();
    }

    @FXML
    private void onObrisiSveClick(ActionEvent actionEvent) {
        filtriVBox.getChildren().clear();
        refreshTrenutniBrojFiltaraLabel();
    }

    @FXML
    private void onOdustaniClick(ActionEvent event) {
        NodeUtils.closeCurrentStageByNode(root);
    }

    @FXML
    private void onPrimjeniClick(ActionEvent event) {
        List<SearchCriteriaDto> searchCriterias = new ArrayList<>();

        for (Node node : filtriVBox.getChildren()) {
            HBox hBox = (HBox) node;
            List<Node> hBoxNodes = hBox.getChildren();

            ComboBox<ItemDto> vrsteKriterijaComboBox = (ComboBox<ItemDto>) getDynamiclyCreatedControl(hBoxNodes, VRSTE_KRITERIJA_COMBOBOX_ID);
            SearchCriteriaType searchCriteriaType = !ControlUtils.hasComboBoxSelectedDefaultValue(vrsteKriterijaComboBox)
                    ? SearchCriteriaType.valueOf(ControlUtils.getSelectedItemKeyFromComboBoxToString(vrsteKriterijaComboBox))
                    : null;

            ComboBox<ItemDto> stupciZaPretrazivanjeComboBox = (ComboBox<ItemDto>) getDynamiclyCreatedControl(hBoxNodes, STUPCI_ZA_PRETRAZIVANJE_COMBOBOX_ID);
            String column = !ControlUtils.hasComboBoxSelectedDefaultValue(stupciZaPretrazivanjeComboBox)
                    ? ControlUtils.getSelectedItemKeyFromComboBoxToString(stupciZaPretrazivanjeComboBox)
                    : null;

            ComboBox<ItemDto> operacijeZaPretrazivanjeComboBox = (ComboBox<ItemDto>) getDynamiclyCreatedControl(hBoxNodes, OPERACIJE_ZA_PRETRAZIVANJE_COMBOBOX_ID);
            SearchOperation searchOperation = !ControlUtils.hasComboBoxSelectedDefaultValue(operacijeZaPretrazivanjeComboBox)
                    ? SearchOperation.valueOf(ControlUtils.getSelectedItemKeyFromComboBoxToString(operacijeZaPretrazivanjeComboBox))
                    : null;

            TextField vrijednostTextField = (TextField) getDynamiclyCreatedControl(hBoxNodes, VRIJEDNOST_TEXT_FIELD_ID);
            String value = ControlUtils.hasTextFieldValue(vrijednostTextField) ? vrijednostTextField.getText() : null;

            searchCriterias.add(new SearchCriteriaDto(searchCriteriaType, column, searchOperation, value));
        }

        Operator operator = Operator.valueOf(operatoriComboBox.getValue().getKey());
        Integer pageNumber = pretrazivanjeISortiranjeDto.getQueryCriteria().getPageable().getPageNumber();
        Integer pageSize = pretrazivanjeISortiranjeDto.getQueryCriteria().getPageable().getPageSize();
        Sort.Direction direction = Sort.Direction.valueOf(smjeroviSortiranjaComboBox.getValue().getKey());
        String property = stupciZaSortiranjeComboBox.getValue().getKey();

        pretrazivanjeISortiranjeDto.getQueryCriteria().setOperator(operator);
        pretrazivanjeISortiranjeDto.getQueryCriteria().setSearchCriterias(searchCriterias);
        pretrazivanjeISortiranjeDto.getQueryCriteria().setPageable(PageRequest.of(pageNumber, pageSize, direction, property));

        stageResult = StageResult.OK;
        NodeUtils.closeCurrentStageByNode(root);
    }

    private Node getDynamiclyCreatedControl(List<Node> nodes, String nodeId) {
        return nodes.stream().filter(x -> x.getId().equals(nodeId)).findFirst().orElse(null);
    }

    @FXML
    private void onResetirajClick(ActionEvent event) {
        for (Node node : filtriVBox.getChildren()) {
            HBox hBox = (HBox) node;
            for (Node hboxNode : hBox.getChildren()) {
                if (hboxNode instanceof ComboBox) {
                    ComboBox comboBox = (ComboBox) hboxNode;
                    comboBox.getSelectionModel().select(0);
                }

                if (hboxNode instanceof TextField) {
                    TextField textField = (TextField) hboxNode;
                    textField.setText("");
                }
            }
        }
        operatoriComboBox.getSelectionModel().select(0);
        stupciZaSortiranjeComboBox.getSelectionModel().select(0);
        smjeroviSortiranjaComboBox.getSelectionModel().select(0);
    }

    public StageResult getStageResult() {
        return stageResult;
    }

    public QueryCriteriaDto getQueryCriteria() {
        return pretrazivanjeISortiranjeDto.getQueryCriteria();
    }
}
