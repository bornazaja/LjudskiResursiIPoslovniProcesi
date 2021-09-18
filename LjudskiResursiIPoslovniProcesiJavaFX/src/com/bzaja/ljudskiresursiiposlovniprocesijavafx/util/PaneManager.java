package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PaneManager {

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    public <T> void switchPanes(Pane currentPane, FxmlView fxmlView, T t) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlView.getPath()));
            loader.setControllerFactory(configurableApplicationContext::getBean);

            Parent parent = loader.load();

            if (t != null) {
                ControllerInterface<T> controller = loader.getController();
                controller.initData(t);
            }
            
            AnchorPane.setBottomAnchor(parent, 0.0);
            AnchorPane.setLeftAnchor(parent, 0.0);
            AnchorPane.setRightAnchor(parent, 0.0);
            AnchorPane.setTopAnchor(parent, 0.0);

            currentPane.getChildren().setAll(parent);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public void switchPanes(Pane currentPane, FxmlView fxmlDatoteka) {
        switchPanes(currentPane, fxmlDatoteka, null);
    }
}
