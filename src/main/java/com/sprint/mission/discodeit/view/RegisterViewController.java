package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.DiscodeitApplication;
import com.sprint.mission.discodeit.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterViewController {

    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nicknameField;
    @FXML private TextField phoneNumField;
    @FXML private Label errorLabel;

    private UserService userService;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        this.userService = DiscodeitApplication.getAppConfig().getUserService();
    }

    @FXML
    private void handleRegister() throws IOException {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String nickname = nicknameField.getText();
        String phoneNum = phoneNumField.getText();

        if (email.isBlank() || username.isBlank() || password.isBlank() || nickname.isBlank()) {
            errorLabel.setText("Email, Username, Password, Nickname are required.");
            return;
        }

        try {
            userService.createUser(username, password, email, nickname, phoneNum.isBlank() ? null : phoneNum);
            DiscodeitApplication.showLoginView(this.stage);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin() throws IOException {
        DiscodeitApplication.showLoginView(this.stage);
    }
}