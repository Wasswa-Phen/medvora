package com.medvora.desktopclient;

import com.medvora.desktopclient.util.SceneNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class MedvoraApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Medvora");
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        SceneNavigator.navigate(stage, SceneNavigator.STARTUP_VIEW);
    }

    public static void main(String[] args) {
        launch(args);
    }
}