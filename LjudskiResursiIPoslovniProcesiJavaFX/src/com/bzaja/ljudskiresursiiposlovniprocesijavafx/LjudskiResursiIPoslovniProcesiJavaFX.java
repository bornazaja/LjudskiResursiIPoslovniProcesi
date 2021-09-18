/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesijavafx;

import com.bzaja.ljudskiresursiiposlovniprocesijavafx.resource.FxmlView;
import com.bzaja.ljudskiresursiiposlovniprocesijavafx.util.StageManager;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author Borna
 */
@SpringBootApplication(scanBasePackages = {"com.bzaja.ljudskiresursiiposlovniprocesilibrary.config", "com.bzaja.ljudskiresursiiposlovniprocesijavafx"})
public class LjudskiResursiIPoslovniProcesiJavaFX extends Application {

    @Autowired
    private StageManager stageManager;

    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void init() throws Exception {
        configurableApplicationContext = SpringApplication.run(LjudskiResursiIPoslovniProcesiJavaFX.class);
        configurableApplicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stageManager.showPrimaryStage(primaryStage, FxmlView.PRIJAVA);
    }

    @Override
    public void stop() throws Exception {
        configurableApplicationContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
