package com.bot.adminfront.controller;

import com.bot.adminfront.model.Partie;
import com.bot.adminfront.service.PartiesService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

public class PartiesController {

    // ================= FXML =================

    @FXML private TableView<Partie> tableView;

    @FXML private TableColumn<Partie, Long> idColumn;
    @FXML private TableColumn<Partie, String> tournoiColumn;
    @FXML private TableColumn<Partie, String> dateColumn;
    @FXML private TableColumn<Partie, String> equipeLocalColumn;
    @FXML private TableColumn<Partie, String> pointLocalColumn;
    @FXML private TableColumn<Partie, String> equipeVisiteurColumn;
    @FXML private TableColumn<Partie, String> pointVisiteurColumn;

    @FXML private HBox searchContainer;
    @FXML private ChoiceBox<String> filterChoiceBox;
    @FXML private Button saveAllButton;

    // ================= STATE =================

// ================= STATE =================

    private final TextField searchField = new TextField();
    private final DatePicker datePicker1 = new DatePicker();

    private final PartiesService partiesService = new PartiesService();
    private final ObservableList<Partie> partiesList = FXCollections.observableArrayList();

// ================= INITIALIZE =================

    @FXML
    public void initialize() {

        tableView.setEditable(true);

        // Set up cell value factories - using Partie properties (all Strings)
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tournoiColumn.setCellValueFactory(new PropertyValueFactory<>("tournoi"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        equipeLocalColumn.setCellValueFactory(new PropertyValueFactory<>("equipeLocal"));
        pointLocalColumn.setCellValueFactory(new PropertyValueFactory<>("pointLocal"));
        equipeVisiteurColumn.setCellValueFactory(new PropertyValueFactory<>("equipeVisiteur"));
        pointVisiteurColumn.setCellValueFactory(new PropertyValueFactory<>("pointVisiteur"));

        // Set cell factories for editable columns
        pointLocalColumn.setCellFactory(col -> new SpinnerTableCell(0, 100, true));
        pointVisiteurColumn.setCellFactory(col -> new SpinnerTableCell(0, 100, false));

        // Setup filter choice box
        filterChoiceBox.getItems().addAll(
                "tournoi", "date", "avant", "apres", "equipeLocal", "equipeVisiteur"
        );
        filterChoiceBox.setValue("");

        filterChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> switchSearchInput(n));

        searchField.textProperty().addListener((obs, o, n) ->
                searchAndRefresh(n, filterChoiceBox.getValue()));

        // ADD THIS: Set up date picker listener once
        datePicker1.valueProperty().addListener((o, ov, nv) ->
                searchAndRefresh(nv != null ? nv.toString() : "", filterChoiceBox.getValue()));

        // Set up save button
        saveAllButton.setOnAction(e -> saveAllChanges());

        refreshTable(partiesService.recherche("", ""));
    }

// ================= SEARCH =================

    private void switchSearchInput(String mode) {
        searchContainer.getChildren().clear();
        if (mode == null) return;

        if (mode.matches("date|avant|apres")) {
            datePicker1.setValue(LocalDate.now());
            searchContainer.getChildren().add(datePicker1);

            LocalDate currentDate = datePicker1.getValue();
            if (currentDate != null) {
                searchAndRefresh(currentDate.toString(), mode);
            }
        } else {
            searchField.clear();
            searchContainer.getChildren().add(searchField);
        }
    }

    private void searchAndRefresh(String keyword, String mode) {
        new Thread(() -> {
            List<Partie> result = partiesService.recherche(keyword, mode);
            Platform.runLater(() -> refreshTable(result));
        }).start();
    }

    // ================= TABLE =================

    private void refreshTable(List<Partie> list) {
        partiesList.clear();
        partiesList.addAll(list);
        tableView.setItems(partiesList);
    }

    // ================= SAVE =================

    private void saveAllChanges() {
        boolean allSuccess = true;
        int savedCount = 0;

        for (Partie p : partiesList) {
            // Only save parties that can be edited (date is today or past)
            LocalDate partieDate = LocalDate.parse(p.getDate());
            if (!partieDate.isAfter(LocalDate.now())) {
                if (partiesService.modifier(p)) {
                    savedCount++;
                } else {
                    allSuccess = false;
                }
            }
        }

        if (allSuccess && savedCount > 0) {
            new Alert(Alert.AlertType.INFORMATION,
                    savedCount + " partie modifiée avec succès").showAndWait();
        } else if (savedCount == 0) {
            new Alert(Alert.AlertType.INFORMATION,
                    "Aucune modification à enregistrer").showAndWait();
        } else {
            new Alert(Alert.AlertType.WARNING,
                    "Certaines modifications n'ont pas pu être enregistrées").showAndWait();
        }
    }

    // ================= CUSTOM CELLS =================

    public static class SpinnerTableCell extends TableCell<Partie, String> {

        private final Spinner<Integer> spinner;
        private final SpinnerValueFactory.IntegerSpinnerValueFactory factory;
        private final boolean isLocal;

        public SpinnerTableCell(int min, int max, boolean isLocal) {
            this.isLocal = isLocal;
            factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
            spinner = new Spinner<>();
            spinner.setValueFactory(factory);
            spinner.setEditable(true);

            spinner.valueProperty().addListener((obs, o, n) -> {
                if (n != null && getTableRow() != null && getTableRow().getItem() != null) {
                    Partie p = getTableRow().getItem();
                    if (isLocal) {
                        p.setPointLocal(String.valueOf(n));
                    } else {
                        p.setPointVisiteur(String.valueOf(n));
                    }
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                setGraphic(null);
            } else {
                Partie partie = getTableRow().getItem();
                LocalDate today = LocalDate.now();
                LocalDate partieDate = LocalDate.parse(partie.getDate());

                // Only enable editing if date is today or in the past
                boolean canEdit = !partieDate.isAfter(today);
                spinner.setDisable(!canEdit);

                try {
                    factory.setValue(item != null ? Integer.parseInt(item) : 0);
                } catch (NumberFormatException e) {
                    factory.setValue(0);
                }
                setGraphic(spinner);
            }
        }
    }
}