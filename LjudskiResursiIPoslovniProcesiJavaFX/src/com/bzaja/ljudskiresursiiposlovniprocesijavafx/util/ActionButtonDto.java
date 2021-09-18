package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ActionButtonDto<T> {

    private ContextMenu contextMenu;
    private Button button;
    private MouseEvent mouseEvent;
    private T t;
}
