package com.medvora.desktopclient.controller;

import com.medvora.desktopclient.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AccessHelpController {

    @FXML
    private Button backToSignInButton;

    @FXML
    public void handleBackToSignIn(ActionEvent event) {
        Stage stage = (Stage) backToSignInButton.getScene().getWindow();
        SceneNavigator.navigate(stage, SceneNavigator.SIGN_IN_VIEW);
    }

    @FXML
    public void handleCheckConnection(ActionEvent event) {
        System.out.println("Checking connection to facility network service...");
        // Network status ping will be added when service contract is integrated
    }
}