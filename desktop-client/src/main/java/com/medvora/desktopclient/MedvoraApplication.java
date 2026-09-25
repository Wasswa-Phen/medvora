package com.medvora.desktopclient;

import com.medvora.desktopclient.util.SceneNavigator;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class MedvoraApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Medvora");
        stage.setMinWidth(1024);
        stage.setMinHeight(768);

        // Set the window/favicon icon
        try {
            Image appIcon = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/com/medvora/desktopclient/images/app-icon.png")
            ));
            stage.getIcons().add(appIcon);
        } catch (Exception e) {
            System.err.println("Could not load application stage icon: " + e.getMessage());
        }

        SceneNavigator.navigate(stage, SceneNavigator.STARTUP_VIEW);
    }

    public static void main(String[] args) {
        launch(args);
    }
}