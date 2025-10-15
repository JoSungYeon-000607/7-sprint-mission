package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.DiscodeitApplication;
import com.sprint.mission.discodeit.auth.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private AuthService authService;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        this.authService = DiscodeitApplication.getAppConfig().getAuthService();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            errorLabel.setText("Username and password are required.");
            return;
        }

        authService.login(username, password).ifPresentOrElse(
                authUser -> {
                    try {
                        DiscodeitApplication.showMainView(this.stage, authUser);
                    } catch (IOException e) {
                        errorLabel.setText("Failed to load main application window.");
                        e.printStackTrace();
                    }
                },
                () -> errorLabel.setText("Invalid username or password.")
        );
    }

    @FXML
    private void handleRegisterLink() throws IOException {
        DiscodeitApplication.showRegisterView(this.stage);
    }
}