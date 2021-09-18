package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FXMLManager {

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    public <T> Parent load(FxmlView fxmlView, T t) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlView.getPath()));
            loader.setControllerFactory(configurableApplicationContext::getBean);

            Parent parent = loader.load();

            if (t != null) {
                ControllerInterface<T> dataController = loader.getController();
                dataController.initData(t);
            }

            return parent;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Parent load(FxmlView fxmlView) {
        return load(fxmlView, null);
    }
}
