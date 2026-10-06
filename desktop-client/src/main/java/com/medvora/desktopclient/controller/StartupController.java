package com.medvora.desktopclient.controller;

import com.medvora.desktopclient.util.SceneNavigator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartupController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // Start the progress bar at 0
        progressBar.setProgress(0);

        // Animate the progress bar smoothly from 0 → 1 over ~2.5 seconds
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(1.0),
                        new KeyValue(progressBar.progressProperty(), 0.4)),
                new KeyFrame(Duration.seconds(1.8),
                        new KeyValue(progressBar.progressProperty(), 0.7)),
                new KeyFrame(Duration.seconds(2.5),
                        new KeyValue(progressBar.progressProperty(), 1.0))
        );

        timeline.setOnFinished(event -> {
            Stage stage = (Stage) progressBar.getScene().getWindow();
            SceneNavigator.navigate(stage, SceneNavigator.SIGN_IN_VIEW);
        });

        timeline.play();
    }
}