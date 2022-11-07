package com.peter.controller;

import com.peter.helperClasses.Utilities;
import com.peter.mainApplication.MainApplication;
import com.peter.model.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Created by KungPeter on 2016-03-24.
 */
public class LoginWindowController {

    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;


    private static MainApplication mainApplication;
    private static Stage primaryStage;

    public void initializeStart() {
//        progressIndicator.setVisible(false);
        loginButton.setDefaultButton(true);
    }

    public void setMainApplication(MainApplication mainApp) {
        mainApplication = mainApp;
    }


    public void handleLoginButtonClicked() {
        String userName = usernameTextField.getText();
        String password = passwordTextField.getText();
        System.out.println("login button pressed");

        if (mainApplication != null) {
            try {
                messageLabel.setText("Loggar in...");
                DatabaseHandler.validateUserAndPass(userName, password);
                mainApplication.loginOk();

            } catch (SQLException e) {
                messageLabel.setText("Felaktigt Användarnamn eller lösenord");
                Utilities.showAlert(Integer.toString(e.getErrorCode()), e.getMessage(), Alert.AlertType.ERROR);

            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlert("Ett fel uppstod!", "Kunde inte ladda Programmet.", Alert.AlertType.ERROR);
            }
        }
    }


}
