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
                //si la connection est reconnue cela chargeras le menu principale
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/bot/adminfront/MainMenu.fxml"));
                Scene mainMenuScene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) nom.getScene().getWindow();
                stage.setScene(mainMenuScene);
                stage.setTitle("Main Menu"); // optional: set title
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password");
            alert.showAndWait();
        }
    }
}