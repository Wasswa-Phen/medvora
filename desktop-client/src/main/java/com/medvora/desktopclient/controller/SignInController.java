package com.medvora.desktopclient.controller;

import com.medvora.desktopclient.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignInController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink accessHelpLink;

    @FXML
    public void handleSignIn(ActionEvent event) {
        String username = usernameField.getText();
        System.out.println("Attempting sign-in for: " + username);
        // Will connect to application-service API in future tasks
    }

    @FXML
    public void handleAccessHelp(ActionEvent event) {
        Stage stage = (Stage) accessHelpLink.getScene().getWindow();
        SceneNavigator.navigate(stage, SceneNavigator.ACCESS_HELP_VIEW);
    }
}