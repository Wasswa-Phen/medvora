package com.medvora.desktopclient.controller;

import com.medvora.desktopclient.util.SceneNavigator;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartupController {

    @FXML
    private Pane backgroundDecorationPane;

    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        setupBackgroundArcs();
        startStartupAnimation();
    }

    private void setupBackgroundArcs() {
        if (backgroundDecorationPane == null) {
            return;
        }

        // Clip the decoration pane to ensure no boundary overflow
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(backgroundDecorationPane.widthProperty());
        clip.heightProperty().bind(backgroundDecorationPane.heightProperty());
        backgroundDecorationPane.setClip(clip);

        // Top-right subtle circular arcs (matching the corner waves in the design)
        Circle trArc1 = new Circle();
        trArc1.setRadius(380);
        trArc1.centerXProperty().bind(backgroundDecorationPane.widthProperty());
        trArc1.setCenterY(0);
        trArc1.setFill(Color.TRANSPARENT);
        trArc1.setStroke(Color.rgb(56, 189, 248, 0.28));
        trArc1.setStrokeWidth(1.2);
        trArc1.setMouseTransparent(true);

        Circle trArc2 = new Circle();
        trArc2.setRadius(240);
        trArc2.centerXProperty().bind(backgroundDecorationPane.widthProperty());
        trArc2.setCenterY(0);
        trArc2.setFill(Color.TRANSPARENT);
        trArc2.setStroke(Color.rgb(56, 189, 248, 0.16));
        trArc2.setStrokeWidth(1.0);
        trArc2.setMouseTransparent(true);

        // Bottom-left subtle circular arcs (matching the corner waves in the design)
        Circle blArc1 = new Circle();
        blArc1.setRadius(360);
        blArc1.setCenterX(0);
        blArc1.centerYProperty().bind(backgroundDecorationPane.heightProperty());
        blArc1.setFill(Color.TRANSPARENT);
        blArc1.setStroke(Color.rgb(20, 184, 166, 0.28));
        blArc1.setStrokeWidth(1.2);
        blArc1.setMouseTransparent(true);

        Circle blArc2 = new Circle();
        blArc2.setRadius(220);
        blArc2.setCenterX(0);
        blArc2.centerYProperty().bind(backgroundDecorationPane.heightProperty());
        blArc2.setFill(Color.TRANSPARENT);
        blArc2.setStroke(Color.rgb(20, 184, 166, 0.16));
        blArc2.setStrokeWidth(1.0);
        blArc2.setMouseTransparent(true);

        backgroundDecorationPane.getChildren().addAll(trArc1, trArc2, blArc1, blArc2);
    }

    private void startStartupAnimation() {
        progressBar.setProgress(0.12);

        // Animate progress smoothly through workspace preparation stages
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(800),
                        new KeyValue(progressBar.progressProperty(), 0.38, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(1800),
                        new KeyValue(progressBar.progressProperty(), 0.78, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(2600),
                        new KeyValue(progressBar.progressProperty(), 1.0, Interpolator.EASE_BOTH))
        );

        timeline.setOnFinished(event -> {
            if (progressBar != null && progressBar.getScene() != null && progressBar.getScene().getWindow() instanceof Stage stage) {
                SceneNavigator.navigate(stage, SceneNavigator.SIGN_IN_VIEW);
            } else {
                System.err.println("StartupController: Unable to retrieve active Stage for scene navigation.");
            }
        });

        timeline.play();
    }
}