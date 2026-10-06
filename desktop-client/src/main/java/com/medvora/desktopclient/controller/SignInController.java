package com.medvora.desktopclient.controller;

import com.medvora.desktopclient.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignInController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button togglePasswordBtn;

    @FXML
    private Label eyeIconLabel;

    @FXML
    private Hyperlink accessHelpLink;

    private boolean passwordVisible = false;

    @FXML
    public void initialize() {
        // Keep the plain text field's text in sync with the password field
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    @FXML
    public void handleTogglePassword(ActionEvent event) {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            // Show plain text, hide password field
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);
            passwordTextField.requestFocus();
            passwordTextField.positionCaret(passwordTextField.getText().length());
            eyeIconLabel.setText("🔒");
        } else {
            // Show password field, hide plain text
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());
            eyeIconLabel.setText("👁");
        }
    }

    @FXML
    public void handleSignIn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        System.out.println("Attempting sign-in for: " + username);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        SceneNavigator.navigate(stage, SceneNavigator.DASHBOARD_VIEW);
    }

    @FXML
    public void handleAccessHelp(ActionEvent event) {
        Stage stage = (Stage) accessHelpLink.getScene().getWindow();
        SceneNavigator.navigate(stage, SceneNavigator.ACCESS_HELP_VIEW);
    }
}