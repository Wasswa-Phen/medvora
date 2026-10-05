package com.medvora.desktopclient.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {
    public static final String STARTUP_VIEW = "/com/medvora/desktopclient/view/startup-view.fxml";
    public static final String SIGN_IN_VIEW = "/com/medvora/desktopclient/view/sign-in-view.fxml";
    public static final String ACCESS_HELP_VIEW = "/com/medvora/desktopclient/view/access-help-view.fxml";

    public static void navigate(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();

            if (stage.getScene() == null) {
                // Detect the usable screen area (excludes taskbar, menu bar, etc.)
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

                // Target 1280x800 but clamp to 90% of screen if the screen is smaller
                double sceneWidth = Math.min(1280, screenBounds.getWidth() * 0.90);
                double sceneHeight = Math.min(800, screenBounds.getHeight() * 0.90);

                stage.setScene(new Scene(root, sceneWidth, sceneHeight));

                // Centre the window on screen
                stage.setX((screenBounds.getWidth() - sceneWidth) / 2 + screenBounds.getMinX());
                stage.setY((screenBounds.getHeight() - sceneHeight) / 2 + screenBounds.getMinY());
            } else {
                stage.getScene().setRoot(root);
            }
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}