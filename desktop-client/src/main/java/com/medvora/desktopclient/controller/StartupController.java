package com.medvora.desktopclient.controller;

import com.medvora.desktopclient.util.SceneNavigator;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartupController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        // Simulates loading workspace environment before displaying Sign In
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        pause.setOnFinished(event -> {
            Stage stage = (Stage) progressBar.getScene().getWindow();
            SceneNavigator.navigate(stage, SceneNavigator.SIGN_IN_VIEW);
        });
        pause.play();
    }
}