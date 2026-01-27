package com.bot.adminfront.controller.officiel;

import com.bot.adminfront.service.OfficielService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class SupprimerController {

    @FXML
    private VBox listContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    private OfficielService officielService = new OfficielService();

    @FXML
    public void initialize() {
        // Setup ChoiceBox with some modes (adjust as needed)
        filterChoiceBox.getItems().addAll("nom", "numerodetelephone", "courriel", "adresse", "adresse", "federation", "role");
        filterChoiceBox.setValue("nom"); // default

        // Initial load
        refreshList(officielService.recherche("", filterChoiceBox.getValue()));

        // Listener on searchField
        searchField.textProperty().addListener((obs, oldText, newText) -> searchAndRefresh(newText, filterChoiceBox.getValue()));

        // Listener on ChoiceBox
        filterChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldMode, newMode) -> {
            String keyword = searchField.getText().trim();
            searchAndRefresh(keyword, newMode);
        });
    }

    /**
     * Performs the search asynchronously and refreshes the list.
     */
    private void searchAndRefresh(String keyword, String mode) {
        new Thread(() -> {
            List<String> result = officielService.recherche(keyword, mode);
            Platform.runLater(() -> refreshList(result));
        }).start();
    }

    /**
     * Refreshes the VBox list of users.
     */
    private void refreshList(List<String> users) {
        listContainer.getChildren().clear();

        for (String user : users) {
            HBox row = new HBox(10);
            Label nameLabel = new Label(user);
            Button deleteButton = new Button("Supprimer");

            deleteButton.setOnAction(e -> supprimer(user,searchField.getText()));

            row.getChildren().addAll(nameLabel, deleteButton);
            listContainer.getChildren().add(row);
        }
    }

    /**
     * Delete a user
     */
    @FXML
    public void supprimer(String label, String mode) {
        String id = label.split("\\|")[0];

        officielService.supprimer(id);
        // reinitialise la liste de nom
        String keyword = searchField.getText().trim();
        new Thread(() -> {
            List<String> result = officielService.recherche(keyword, mode);
            Platform.runLater(() -> refreshList(result));
        }).start();
    }
}