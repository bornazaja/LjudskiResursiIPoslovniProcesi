package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoDto;
import java.util.List;
import java.util.Optional;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public final class DialogUtils {

    private DialogUtils() {

    }

    public static void showDialog(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static <T> void showDialog(Alert.AlertType alertType, String headerText, T bean, List<PropertyInfoDto> propertiesInfo) {
        showDialog(alertType, headerText, getBeanToString(bean, propertiesInfo));
    }

    public static void showConfirmationDialog(String headerText, String contentText, String successMessage, String errorMessage, Runnable runnable) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        ButtonType buttonTypeDa = new ButtonType("Da", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeNe = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeDa, buttonTypeNe);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeDa) {
            try {
                runnable.run();
                showDialog(Alert.AlertType.INFORMATION, "Info", successMessage);
            } catch (Exception e) {
                showDialog(Alert.AlertType.ERROR, "Greška", errorMessage);
            } finally {
                closeAlert(alert);
            }
        }
    }

    public static <T> void showConfirmationDialog(String headerText, T bean, List<PropertyInfoDto> propertiesInfo, String successMessage, String errorMeesage, Runnable runnable) {
        showConfirmationDialog(headerText, getBeanToString(bean, propertiesInfo), successMessage, errorMeesage, runnable);
    }

    public static void showLoadingDialog(String errorMessage, Runnable runnable) {
        Label loadingLabel = new Label("Molimo pričekajte...");

        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        progressBar.setPrefWidth(150);

        VBox bodyContent = new VBox(loadingLabel, progressBar);
        bodyContent.setAlignment(Pos.CENTER);
        bodyContent.setSpacing(10);

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initStyle(StageStyle.UTILITY);
        alert.getDialogPane().getScene().getWindow().setOnCloseRequest(e -> e.consume());
        alert.getDialogPane().setContent(bodyContent);
        alert.show();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    runnable.run();
                    Thread.sleep(500);
                } catch (Exception e) {
                    cancel();
                }

                return null;
            }

            @Override
            protected void succeeded() {
                closeAlert(alert);
            }

            @Override
            protected void cancelled() {
                closeAlert(alert);
                showDialog(Alert.AlertType.ERROR, "Greška", errorMessage);
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private static void closeAlert(Alert alert) {
        alert.setResult(ButtonType.CLOSE);
        alert.close();
    }

    private static <T> String getBeanToString(T bean, List<PropertyInfoDto> propertiesInfo) {
        StringBuilder stringBuilder = new StringBuilder();

        propertiesInfo.forEach((propertyInfo) -> {
            stringBuilder.append(propertyInfo.getDisplayName()).append(": ").append(BeanUtils.getFormattedPropertyValue(bean, propertyInfo.getPropertyName())).append(System.lineSeparator());
        });

        return stringBuilder.toString();
    }
}
