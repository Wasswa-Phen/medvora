package com.medvora.desktopclient.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
                // Standard desktop dimensions: 1280x800 per guide specifications
                stage.setScene(new Scene(root, 1280, 800));
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