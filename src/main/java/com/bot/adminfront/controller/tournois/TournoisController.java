package com.bot.adminfront.controller.tournois;

import com.bot.adminfront.model.Tournoi;
import com.bot.adminfront.service.TournoisService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

public class TournoisController {

    @FXML
    private TableView<Tournoi> tableView;

    @FXML
    private TableColumn<Tournoi, String> idColumn;
    @FXML
    private TableColumn<Tournoi, String> debutColumn;
    @FXML
    private TableColumn<Tournoi, String> finColumn;
    @FXML
    private TableColumn<Tournoi, String> maximumColumn;
    @FXML
    private TableColumn<Tournoi, String> federationColumn;
    @FXML
    private TableColumn<Tournoi, String> categorieColumn;
    @FXML
    private TableColumn<Tournoi, Button> actionColumn;

    @FXML
    private HBox searchContainer;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    private TextField searchField = new TextField();
    private DatePicker datePicker1 = new DatePicker();
    private DatePicker datePicker2 = new DatePicker(); // used for "entre"

    private TournoisService tournoisService = new TournoisService();
    private ObservableList<Tournoi> tournoisList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup TableView columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        debutColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
        finColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));
        maximumColumn.setCellValueFactory(new PropertyValueFactory<>("maximum"));
        federationColumn.setCellValueFactory(new PropertyValueFactory<>("federation"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("actionButton"));

        // Setup ChoiceBox
        filterChoiceBox.getItems().addAll("debut", "fin", "avant", "apres", "entre", "federation", "categorie");
        filterChoiceBox.setValue("");

        // Initially show TextField
        searchField.setPromptText("Rechercher un tournoi...");
        searchContainer.getChildren().setAll(searchField);

        // TextField listener
        searchField.textProperty().addListener((obs, oldText, newText) ->
                searchAndRefresh(newText, filterChoiceBox.getValue()));

        // ChoiceBox listener
        filterChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldMode, newMode) ->
                switchSearchInput(newMode));

        // Load initial data
        refreshTable(tournoisService.recherche("", filterChoiceBox.getValue()));
    }

    // Switch input type dynamically
    private void switchSearchInput(String mode) {
        searchContainer.getChildren().clear();

        if (mode.equals("debut") || mode.equals("fin") || mode.equals("avant") || mode.equals("apres")) {
            // Single DatePicker
            datePicker1.setValue(null);
            datePicker1.setPromptText("Choisir une date...");
            searchContainer.getChildren().add(datePicker1);

            datePicker1.valueProperty().addListener((obs, oldVal, newVal) -> {
                String keyword = newVal != null ? newVal.toString() : "";
                searchAndRefresh(keyword, mode);
            });

        } else if (mode.equals("entre")) {
            // Two DatePickers
            datePicker1.setValue(null);
            datePicker2.setValue(null);
            datePicker1.setPromptText("Date début");
            datePicker2.setPromptText("Date fin");
            searchContainer.getChildren().addAll(datePicker1, datePicker2);

            datePicker1.valueProperty().addListener((obs, oldVal, newVal) -> triggerEntreSearch(mode));
            datePicker2.valueProperty().addListener((obs, oldVal, newVal) -> triggerEntreSearch(mode));

        } else {
            // Default TextField
            searchField.clear();
            searchContainer.getChildren().add(searchField);
        }
    }

    private void triggerEntreSearch(String mode) {
        if (datePicker1.getValue() != null && datePicker2.getValue() != null) {
            String keyword = datePicker1.getValue() + "," + datePicker2.getValue(); // "startDate,endDate"
            searchAndRefresh(keyword, mode);
        }
    }

    // Perform search in background
    private void searchAndRefresh(String keyword, String mode) {
        new Thread(() -> {
            List<Tournoi> result = tournoisService.recherche(keyword, mode);
            Platform.runLater(() -> refreshTable(result));
        }).start();
    }

    // Refresh TableView
    private void refreshTable(List<Tournoi> list) {
        tournoisList.clear();
        for (Tournoi t : list) {
            Button deleteBtn = new Button("Supprimer");
            deleteBtn.setOnAction(e -> supprimer(t.getId(), getCurrentKeyword(), filterChoiceBox.getValue()));
            t.setActionButton(deleteBtn);
        }
        tournoisList.addAll(list);
        tableView.setItems(tournoisList);
    }

    private void supprimer(String id, String keyword, String mode) {
        // Find the tournament in the current list
        Tournoi tournoi = tournoisList.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (tournoi == null) return; // safety check

        LocalDate dateDebut = LocalDate.parse(tournoi.getDebut());
        LocalDate today = LocalDate.now();

        if (dateDebut.isAfter(today)) { // only delete if start date is tomorrow or later
            tournoisService.supprimer(id);
            searchAndRefresh(keyword, mode);
        } else {
            // Show alert if deletion not allowed
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Suppression interdite");
            alert.setHeaderText(null);
            alert.setContentText("Vous ne pouvez supprimer un tournoi dont le début est aujourd'hui ou déjà passé.");
            alert.showAndWait();
        }
    }

    // Get the current keyword depending on input type
    private String getCurrentKeyword() {
        String mode = filterChoiceBox.getValue();
        if (mode.equals("debut") || mode.equals("fin") || mode.equals("avant") || mode.equals("apres")) {
            return datePicker1.getValue() != null ? datePicker1.getValue().toString() : "";
        } else if (mode.equals("entre")) {
            if (datePicker1.getValue() != null && datePicker2.getValue() != null) {
                return datePicker1.getValue() + "," + datePicker2.getValue();
            } else {
                return "";
            }
        } else {
            return searchField.getText().trim();
        }
    }
}
