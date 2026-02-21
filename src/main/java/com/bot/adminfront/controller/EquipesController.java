package com.bot.adminfront.controller;

import com.bot.adminfront.model.Equipe;
import com.bot.adminfront.service.EquipesService;
import com.bot.adminfront.service.FederationService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;

public class EquipesController {

    // ================= FXML =================

    @FXML private TableView<Equipe> tableView;

    @FXML private TableColumn<Equipe, Long> idColumn;
    @FXML private TableColumn<Equipe, String> nomColumn;
    @FXML private TableColumn<Equipe, String> federationColumn;
    @FXML private TableColumn<Equipe, String> categorieColumn;
    @FXML private TableColumn<Equipe, Void> actionColumn;

    @FXML private HBox searchContainer;
    @FXML private ChoiceBox<String> filterChoiceBox;

    // ================= STATE =================

    private final TextField searchField = new TextField();
    private final ComboBox<String> federationComboBox = new ComboBox<>();
    private final ComboBox<String> categorieComboBox = new ComboBox<>();

    private final EquipesService equipesService = new EquipesService();
    private final FederationService federationService = new FederationService();
    private final ObservableList<Equipe> equipesList = FXCollections.observableArrayList();

    // Hardcoded categories
    private static final List<String> CATEGORIES = List.of(
            "Moustique",
            "Atome",
            "Peewee",
            "Bantam",
            "Midget",
            "Junior"
    );

    // ================= INITIALIZE =================

    @FXML
    public void initialize() {

        // Set up cell value factories
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        federationColumn.setCellValueFactory(new PropertyValueFactory<>("federation"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        // Set up action column with delete button
        actionColumn.setCellFactory(col -> new DeleteButtonCell());

        // Setup filter choice box
        filterChoiceBox.getItems().addAll(
                "nom", "federation", "categorie"
        );
        filterChoiceBox.setValue("");

        filterChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> switchSearchInput(n));

        searchField.textProperty().addListener((obs, o, n) ->
                searchAndRefresh(n, filterChoiceBox.getValue()));

        // Initialize with all equipes
        refreshTable(equipesService.recherche("", ""));
    }

    // ================= SEARCH =================

    private void switchSearchInput(String mode) {
        searchContainer.getChildren().clear();
        if (mode == null) return;

        if ("federation".equals(mode)) {
            federationComboBox.getItems().clear();
            federationComboBox.getItems().add(""); // Add empty option for "all"
            federationComboBox.getItems().addAll(federationService.list());
            federationComboBox.setValue("");
            searchContainer.getChildren().add(federationComboBox);

            federationComboBox.valueProperty().addListener((obs, o, n) ->
                    searchAndRefresh(n != null ? n : "", mode));
        }
        else if ("categorie".equals(mode)) {
            categorieComboBox.getItems().clear();
            categorieComboBox.getItems().add(""); // Add empty option for "all"
            categorieComboBox.getItems().addAll(CATEGORIES);
            categorieComboBox.setValue("");
            searchContainer.getChildren().add(categorieComboBox);

            categorieComboBox.valueProperty().addListener((obs, o, n) ->
                    searchAndRefresh(n != null ? n : "", mode));
        }
        else {
            searchField.clear();
            searchContainer.getChildren().add(searchField);
        }
    }

    private void searchAndRefresh(String keyword, String mode) {
        new Thread(() -> {
            List<Equipe> result = equipesService.recherche(keyword, mode);
            Platform.runLater(() -> refreshTable(result));
        }).start();
    }

    // ================= TABLE =================

    private void refreshTable(List<Equipe> list) {
        equipesList.clear();
        equipesList.addAll(list);
        tableView.setItems(equipesList);
    }

    // ================= DELETE =================

    private void deleteEquipe(Equipe equipe) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer l'équipe");
        confirmation.setContentText("Voulez-vous vraiment supprimer l'équipe \"" + equipe.getNom() + "\" ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = equipesService.supprimer(equipe.getId());
                if (success) {
                    searchAndRefresh(searchField.getText(), filterChoiceBox.getValue());
                    new Alert(Alert.AlertType.INFORMATION, "Équipe supprimée avec succès").showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression").showAndWait();
                }
            }
        });
    }

    // ================= CUSTOM CELL =================

    private class DeleteButtonCell extends TableCell<Equipe, Void> {
        private final Button deleteButton = new Button("Supprimer");

        public DeleteButtonCell() {
            deleteButton.setOnAction(e -> {
                Equipe equipe = getTableView().getItems().get(getIndex());
                deleteEquipe(equipe);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                setGraphic(null);
            } else {
                setGraphic(deleteButton);
            }
        }
    }
}