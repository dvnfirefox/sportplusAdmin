package com.bot.adminfront.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenuController {

    // Middle content pane
    @FXML
    private AnchorPane contentPane;

    // Left-side navigation buttons
    @FXML
    private Button utilisateur;
    @FXML
    private Button officiel;
    @FXML
    private Button tournois;
    @FXML
    private Button partie;
    @FXML
    private Button stat;

    // Called automatically after FXML loads
    @FXML
    public void initialize() {
        loadContent("/com/bot/adminfront/Utilisateur.fxml");

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