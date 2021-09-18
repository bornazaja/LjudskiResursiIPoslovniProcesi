package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import java.util.function.Function;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ActionButtonTableCell<T> extends TableCell<T, Button> {

    private final ContextMenu contextMenu;
    private final Button actionButton;

    private ActionButtonTableCell(Function<ActionButtonDto<T>, ActionButtonDto<T>> function) {
        this.contextMenu = new ContextMenu();
        this.actionButton = new Button("Akcije");
        this.actionButton.setOnMouseClicked((e) -> {
            function.apply(new ActionButtonDto<>(contextMenu, actionButton, e, getCurrentItem()));
        });
    }

    public T getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    public static <T> Callback<TableColumn<T, Button>, TableCell<T, Button>> forTableColumn(Function<ActionButtonDto<T>, ActionButtonDto<T>> function) {
        return param -> new ActionButtonTableCell<>(function);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(actionButton);
        }
    }
}
