package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.query.QueryCriteriaDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.PropertyInfoDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.pretrazivanjeisortiranje.PretrazivanjeISortiranjeController;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.feature.pretrazivanjeisortiranje.PretrazivanjeISortiranjeDto;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.CssFiles;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StageManager {

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    public <T> void showPrimaryStage(Stage currentStage, FxmlView fxmlView, T t) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlView.getPath()));
            loader.setControllerFactory(configurableApplicationContext::getBean);

            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            scene.getStylesheets().add(CssFiles.DEFAULT.getPath());
            scene.getRoot().requestFocus();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            currentStage.close();

            if (t != null) {
                ControllerInterface<T> controller = loader.getController();
                controller.initData(t);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void showPrimaryStage(Stage currentStage, FxmlView fxmlDatoteka) {
        showPrimaryStage(currentStage, fxmlDatoteka, null);
    }

    public <TController, TDto> TController showSecondaryStage(FxmlView fxmlView, TDto tdto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlView.getPath()));
            loader.setControllerFactory(configurableApplicationContext::getBean);

            Parent parent = loader.load();

            TController tController = loader.getController();

            if (tdto != null) {
                ControllerInterface<TDto> dataController = (ControllerInterface<TDto>) tController;
                dataController.initData(tdto);
            }

            Scene scene = new Scene(parent);
            scene.getStylesheets().addAll(CssFiles.DEFAULT.getPath());
            scene.getRoot().requestFocus();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            return tController;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <TController> TController showSecondaryStage(FxmlView fxmlDatoteka) {
        return showSecondaryStage(fxmlDatoteka, null);
    }

    public void showPretrazivanjeISortiranjeStage(String className, List<PropertyInfoDto> propertiesInfo, QueryCriteriaDto queryCriteriaDto, Function<QueryCriteriaDto, Runnable> function) {
        PretrazivanjeISortiranjeDto pretrazivanjeISortiranjeDto = new PretrazivanjeISortiranjeDto(className, propertiesInfo, queryCriteriaDto);
        PretrazivanjeISortiranjeController pretrazivanjeISortiranjeController = showSecondaryStage(FxmlView.PRETRAZIVANJE_I_SORTIRANJE, pretrazivanjeISortiranjeDto);

        if (pretrazivanjeISortiranjeController.getStageResult().equals(StageResult.OK)) {
            function.apply(pretrazivanjeISortiranjeController.getQueryCriteria()).run();
        }
    }
}
