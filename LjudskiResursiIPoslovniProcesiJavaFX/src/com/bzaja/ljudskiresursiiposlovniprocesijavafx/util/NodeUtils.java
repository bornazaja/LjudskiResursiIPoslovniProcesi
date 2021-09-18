package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public final class NodeUtils {

    private NodeUtils() {

    }

    public static void closeCurrentStageByNode(Node node) {
        getStageFromNode(node).close();
    }

    public static Stage getStageFromNode(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    public static TabPane getTabPaneFromInsideNode(Node node) {
        TabPane tabPane = null;

        for (Node n = node.getParent(); n != null && tabPane == null; n = n.getParent()) {
            if (n instanceof TabPane) {
                tabPane = (TabPane) n;
            }
        }

        return tabPane;
    }
}
