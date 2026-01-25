package com.bot.adminfront.controller;

import com.bot.adminfront.service.LoggingService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoggingController {
    private final LoggingService loggingService = new LoggingService();
    @FXML
    private TextField nom;
    @FXML
    private TextField password;

    @FXML
    protected void loggingButtonClick() {
        if (loggingService.logging(nom.getText(), password.getText())) {
            try {
                // Load the main menu FXML
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/bot/adminfront/MainMenu.fxml"));
                Scene mainMenuScene = new Scene(fxmlLoader.load());

                // Get the current stage from any node in your login scene
                Stage stage = (Stage) nom.getScene().getWindow();

                // Set the new scene (main menu) on the stage
                stage.setScene(mainMenuScene);
                stage.setTitle("Main Menu"); // optional: set title
                stage.show();

            } catch (IOException e) {
                e.printStackTrace(); // handle exception properly in real apps
            }
        } else {
            // Optionally show an alert if login failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }
    }
}