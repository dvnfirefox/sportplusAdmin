package com.bot.adminfront.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainMenuController {

    // Middle content pane
    @FXML
    private AnchorPane contentPane;

    // Called automatically after FXML loads
    @FXML
    public void initialize() {

    }

    @FXML
    public void creezUtilisateur(){
        loadContent("/com/bot/adminfront/creezUtilisateur.fxml");
    }

    @FXML
    public void creezOfficiel(){
        loadContent("/com/bot/adminfront/creezOfficiel.fxml");
    }

    @FXML
    public void creezTournois(){
        loadContent("/com/bot/adminfront/creezTournois.fxml");
    }


    // Helper method to load an FXML into the middle pane
    private void loadContent(String fxmlPath) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(pane);

            // Make the loaded pane fill the parent
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}