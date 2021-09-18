package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.MapUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberFormatPatterns;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import com.bzaja.myjavafxlibrary.control.ClearableDatePicker;
import com.bzaja.myjavafxlibrary.control.DashboardTile;
import com.bzaja.myjavafxlibrary.control.SearchableComboBox;
import com.bzaja.myjavafxlibrary.control.SearchableListSelectionView;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public final class ControlUtils {

    private static final String DEFAULT_COMBOBOX_KEY = "0";

    private ControlUtils() {

    }

    public static String getTextFromTextField(TextField textField) {
        return textField.getText() != null ? textField.getText() : "";
    }

    public static Integer getIntegerFromTextField(TextField textField, Integer defaultValue) {
        return NumberUtils.getDefaultInt(textField.getText(), defaultValue);
    }

    public static Double getDoubleFromTextField(TextField textField, Double defaultValue) {
        return NumberUtils.getDefaultDouble(textField.getText(), defaultValue);
    }

    public static Integer getIntegerFromLabel(Label label, Integer deafultValue) {
        return NumberUtils.getDefaultInt(label.getText(), deafultValue);
    }

    public static Double getDoubleFromLabel(Label label, Double defaultValue) {
        return NumberUtils.getDefaultDouble(label.getText(), defaultValue);
    }

    public static <T> T getSelectedItemFromComboBox(ComboBox<ItemDto> comboBox, Function<Integer, T> function) {
        return function.apply(NumberUtils.getDefaultInt(comboBox.getValue().getKey(), 0));
    }

    public static Integer getSelectedItemKeyFromComboBoxToInteger(ComboBox<ItemDto> comboBox) {
        return NumberUtils.getDefaultInt(comboBox.getValue().getKey(), 0);
    }

    public static String getSelectedItemKeyFromComboBoxToString(ComboBox<ItemDto> comboBox) {
        return comboBox.getValue().getKey();
    }

    public static Boolean getValueFromCheckBox(CheckBox checkBox) {
        return checkBox.isSelected();
    }

    public static LocalDate getValueFromDatePicker(DatePicker datePicker) {
        return datePicker.getValue();
    }

    public static LocalDate getValueFromClearableDatePicker(ClearableDatePicker clearableDatePicker) {
        return clearableDatePicker.getDatePicker().getValue();
    }

    public static List<Integer> getKeysFromListViewAsIntegers(ListView<ItemDto> listView) {
        return listView.getItems().stream().map(x -> Integer.parseInt(x.getKey())).collect(Collectors.toList());
    }

    public static <T> void setTextInTextField(TextField textField, T t) {
        textField.setText(t != null ? t.toString() : "");
    }

    public static <T> void setTextInTextField(TextField textField, T t, String propertyName) {
        textField.setText(BeanUtils.getPropertyValue(t, propertyName, "").toString());
    }

    public static <T> void setSelectedComboBoxItem(ComboBox<ItemDto> comboBox, T t, String propertyName) {
        comboBox.getSelectionModel().select(new ItemDto(BeanUtils.getPropertyValue(t, propertyName, DEFAULT_COMBOBOX_KEY).toString()));
    }

    public static void setSelectedComboBoxItem(ComboBox<ItemDto> comboBox, String key) {
        comboBox.getSelectionModel().select(new ItemDto(key));
    }

    private static <T> void setupSearchableComboBox(SearchableComboBox<ItemDto> searchableComboBox, String errorMessage, Function<String, List<T>> function, String keyProperty, String valueProperty, int index, boolean useChooseOption) {
        try {
            searchableComboBox.setEditable(false);

            fillComboBox(searchableComboBox, function.apply(searchableComboBox.getSearchTextField().getText()), keyProperty, valueProperty, index, useChooseOption);

            TextField textField = searchableComboBox.getSearchTextField();
            textField.setPromptText("Traži...");
            textField.setOnKeyReleased((e) -> {
                try {
                    if (e.getCode() != KeyCode.UP || e.getCode() != KeyCode.DOWN || e.getCode() != KeyCode.LEFT || e.getCode() != KeyCode.RIGHT || e.getCode() != KeyCode.ENTER) {
                        List<T> list = function.apply(textField.getText());
                        fillComboBox(searchableComboBox, list, keyProperty, valueProperty, index, useChooseOption);
                    }
                } catch (Exception ex) {
                    DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", errorMessage);
                }
            });
        } catch (Exception e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR, "Greška", errorMessage);
        }
    }

    public static <T> void setupSearchableComboBox(SearchableComboBox<ItemDto> searchableComboBox, String errorMessage, Function<String, List<T>> function, String keyProperty, String valueProperty) {
        setupSearchableComboBox(searchableComboBox, errorMessage, function, keyProperty, valueProperty, 0, true);
    }

    public static <T> void setupSearchableListSelectionView(SearchableListSelectionView<ItemDto> searchableListSelectionView, String errorMessage, Function<String, List<T>> function, String keyProperty, String valueProperty) {
        setupSearchableComboBox(searchableListSelectionView.getSearchableComboBox(), errorMessage, function, keyProperty, valueProperty, -1, false);
    }

    public static <T> void fillComboBox(ComboBox<ItemDto> comboBox, List<T> list, String keyProperty, String valueProperty, int index, boolean useChooseOption) {
        List<ItemDto> comboBoxItems = list.stream().map(x -> new ItemDto(BeanUtils.getPropertyValue(x, keyProperty).toString(), BeanUtils.getFormattedPropertyValue(x, valueProperty))).collect(Collectors.toList());
        comboBox.getItems().clear();

        if (useChooseOption) {
            comboBox.getItems().add(createChooseOptionForItemDto());
        }

        comboBox.getItems().addAll(comboBoxItems);

        if (index >= 0) {
            comboBox.getSelectionModel().select(index);
        }
    }

    public static void fillComboBox(ComboBox<ItemDto> comboBox, List<String> keys, List<String> values, int index, boolean useChooseOption) {
        Map<String, String> map = MapUtils.zipToMap(keys, values);
        List<ItemDto> comboBoxItems = map.entrySet().stream().map(x -> new ItemDto(x.getKey(), x.getValue())).collect(Collectors.toList());
        comboBox.getItems().clear();

        if (useChooseOption) {
            comboBox.getItems().add(createChooseOptionForItemDto());
        }

        comboBox.getItems().addAll(comboBoxItems);
        comboBox.getSelectionModel().select(index);
    }

    public static void fillComboBox(ComboBox<ItemDto> comboBox, Map<String, String> map, int index, boolean useChooseOption) {
        List<ItemDto> comboBoxItems = map.entrySet().stream().map(x -> new ItemDto(x.getKey(), x.getValue())).collect(Collectors.toList());
        comboBox.getItems().clear();

        if (useChooseOption) {
            comboBox.getItems().add(createChooseOptionForItemDto());
        }

        comboBox.getItems().addAll(comboBoxItems);
        comboBox.getSelectionModel().select(index);
    }

    public static <T> void fillComboBox(ComboBox<T> comboBox, List<T> list, int index) {
        comboBox.setItems(FXCollections.observableList(list));
        comboBox.getSelectionModel().select(index);
    }

    public static void fillComboBoxWithOnlyAChooseOption(ComboBox<ItemDto> comboBox) {
        comboBox.getItems().clear();
        comboBox.getItems().add(createChooseOptionForItemDto());
        comboBox.getSelectionModel().select(0);
    }

    private static ItemDto createChooseOptionForItemDto() {
        return new ItemDto(DEFAULT_COMBOBOX_KEY, "--- ODABERITE---");
    }

    public static <T> void setValueInCheckBox(CheckBox checkBox, T t, String propertyName) {
        checkBox.setSelected((Boolean) BeanUtils.getPropertyValue(t, propertyName, false));
    }

    public static <T> void setValueInDatePicker(DatePicker datePicker, T t, String propertyName) {
        datePicker.setValue((LocalDate) BeanUtils.getPropertyValue(t, propertyName, null));
    }

    public static void setValueInDatePicker(DatePicker datePicker, LocalDate localDate) {
        datePicker.setValue(localDate);
    }

    public static <T> void setValueInClearableDatePicker(ClearableDatePicker clearableDatePicker, T t, String propertyName) {
        clearableDatePicker.getDatePicker().setValue((LocalDate) BeanUtils.getPropertyValue(t, propertyName, null));
    }

    public static void setValueInClearableDatePicker(ClearableDatePicker clearableDatePicker, LocalDate localDate) {
        clearableDatePicker.getDatePicker().setValue(localDate);
    }

    public static <T> void setTextInLabel(Label label, T t, String propertyName) {
        label.setText(BeanUtils.getPropertyValue(t, propertyName, "").toString());
    }

    public static <T> void setTextInLabel(Label label, T t) {
        label.setText(t != null ? t.toString() : "");
    }

    public static void setTextInLabel(Label label, Double value) {
        label.setText(NumberUtils.format(value, NumberFormatPatterns.DECIMAL_HR));
    }

    public static void clearLabelsText(Label... labels) {
        for (Label label : labels) {
            label.setText("");
        }
    }

    public static void setValueInDashboardTile(DashboardTile materialDesginDashboardTile, Double value) {
        materialDesginDashboardTile.setValue(NumberUtils.format(value, NumberFormatPatterns.DECIMAL_HR));
    }

    public static <T> void setValueInDashboardTile(DashboardTile materialDesginDashboardTile, T value) {
        materialDesginDashboardTile.setValue(value.toString());
    }

    public static boolean hasComboBoxSelectedDefaultValue(ComboBox<ItemDto> comboBox) {
        return comboBox.getValue().getKey().equals(DEFAULT_COMBOBOX_KEY);
    }

    public static boolean hasTextFieldValue(TextField textField) {
        return textField.getText() != null || textField.getText().isEmpty();
    }
}
