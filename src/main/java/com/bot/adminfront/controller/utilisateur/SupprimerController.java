package com.bot.adminfront.controller.utilisateur;

import com.bot.adminfront.service.UtilisateurService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class SupprimerController{

    @FXML
    private VBox listContainer;

    @FXML
    private TextField searchField;

    // Reference to your service
    private UtilisateurService utilisateurService = new UtilisateurService();

    @FXML
    public void initialize() {
        // recherche vide pour la premiere recherche
        refreshList(utilisateurService.recherche(""));

        // ajouter un listener a la recherche
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            // async pour eviter de faire geler le programme pendant les recherche, car la recherche se fait a chaque lettre
            new Thread(() -> {
                List<String> result = utilisateurService.recherche(newText);
                Platform.runLater(() -> refreshList(result));
            }).start();
        });
    }

    //mets la liste de d'utilisateur a jour apres chaque changement a la recherche
    private void refreshList(List<String> users) {
        listContainer.getChildren().clear();

        for (String user : users) {
            HBox row = new HBox(10);
            Label nameLabel = new Label(user);
            Button deleteButton = new Button("Supprimer");

            deleteButton.setOnAction(e -> supprimerUtilisateur(user));

            row.getChildren().addAll(nameLabel, deleteButton);
            listContainer.getChildren().add(row);
        }
    }

    /**
     * Delete a user
     */
    @FXML
    public void supprimerUtilisateur(String username) {
        utilisateurService.supprimer(username);

        // reinitialise la liste de nom
        String keyword = searchField.getText().trim();
        new Thread(() -> {
            List<String> result = utilisateurService.recherche(keyword);
            Platform.runLater(() -> refreshList(result));
        }).start();
    }
}

