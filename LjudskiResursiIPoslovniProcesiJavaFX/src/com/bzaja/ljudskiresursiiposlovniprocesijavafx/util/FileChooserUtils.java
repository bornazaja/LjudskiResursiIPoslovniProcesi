package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class FileChooserUtils {

    public static void showSaveDialog(Pane root, String fileName, Function<File, Runnable> function, String extension) {
        List<FileChooser.ExtensionFilter> extensionFilters = new ArrayList<>();
        extensionFilters.add(new FileChooser.ExtensionFilter(extension.substring(2).toUpperCase(), extension));

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(extensionFilters);
        fileChooser.setInitialFileName(fileName);

        File file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if (file != null) {
            DialogUtils.showLoadingDialog("Desila se greška prilikom kreiranja datoteke.", () -> {
                function.apply(file).run();
                Platform.runLater(() -> DialogUtils.showDialog(Alert.AlertType.INFORMATION, "Info", String.format("Datoteka je uspješno kreirana. Putanja: %s.", file.getAbsolutePath())));
            });
        }
    }
}
