package com.bot.adminfront.controller.tournois;

import com.bot.adminfront.model.Tournoi;
import com.bot.adminfront.service.TournoisService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

public class TournoisController {

    // ================= FXML =================

    @FXML private TableView<Tournoi> tableView;

    @FXML private TableColumn<Tournoi, String> idColumn;
    @FXML private TableColumn<Tournoi, String> debutColumn;
    @FXML private TableColumn<Tournoi, String> finColumn;
    @FXML private TableColumn<Tournoi, String> maximumColumn;
    @FXML private TableColumn<Tournoi, String> federationColumn;
    @FXML private TableColumn<Tournoi, String> categorieColumn;
    @FXML private TableColumn<Tournoi, HBox> actionColumn;

    @FXML private HBox searchContainer;
    @FXML private ChoiceBox<String> filterChoiceBox;

    // ================= STATE =================

    private final TextField searchField = new TextField();
    private final DatePicker datePicker1 = new DatePicker();
    private final DatePicker datePicker2 = new DatePicker();

    private final TournoisService tournoisService = new TournoisService();
    private final ObservableList<Tournoi> tournoisList = FXCollections.observableArrayList();

    // ================= INITIALIZE =================

    @FXML
    public void initialize() {

        tableView.setEditable(true);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        debutColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
        finColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));
        maximumColumn.setCellValueFactory(new PropertyValueFactory<>("maximum"));
        federationColumn.setCellValueFactory(new PropertyValueFactory<>("federation"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("actionButton"));

        debutColumn.setCellFactory(col -> new DatePickerTableCell(true));
        finColumn.setCellFactory(col -> new DatePickerTableCell(false));
        maximumColumn.setCellFactory(col -> new SpinnerTableCell(1, 500));
        federationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        categorieColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        federationColumn.setOnEditCommit(e ->
                e.getRowValue().setFederation(e.getNewValue()));
        categorieColumn.setOnEditCommit(e ->
                e.getRowValue().setCategorie(e.getNewValue()));

        filterChoiceBox.getItems().addAll(
                "debut", "fin", "avant", "apres", "entre", "federation", "categorie"
        );
        filterChoiceBox.setValue("");

        filterChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, n) -> switchSearchInput(n));

        searchField.textProperty().addListener((obs, o, n) ->
                searchAndRefresh(n, filterChoiceBox.getValue()));

        refreshTable(tournoisService.recherche("", ""));
    }

    // ================= SEARCH =================

    private void switchSearchInput(String mode) {
        searchContainer.getChildren().clear();
        if (mode == null) return;

        if (mode.matches("debut|fin|avant|apres")) {
            datePicker1.setValue(null);
            searchContainer.getChildren().add(datePicker1);
            datePicker1.valueProperty().addListener((o, ov, nv) ->
                    searchAndRefresh(nv != null ? nv.toString() : "", mode));
        }
        else if ("entre".equals(mode)) {
            datePicker1.setValue(null);
            datePicker2.setValue(null);
            searchContainer.getChildren().addAll(datePicker1, datePicker2);
            datePicker1.valueProperty().addListener((o, ov, nv) -> triggerBetween(mode));
            datePicker2.valueProperty().addListener((o, ov, nv) -> triggerBetween(mode));
        }
        else {
            searchField.clear();
            searchContainer.getChildren().add(searchField);
        }
    }

    private void triggerBetween(String mode) {
        if (datePicker1.getValue() != null && datePicker2.getValue() != null) {
            searchAndRefresh(
                    datePicker1.getValue() + "," + datePicker2.getValue(),
                    mode
            );
        }
    }

    private void searchAndRefresh(String keyword, String mode) {
        new Thread(() -> {
            List<Tournoi> result = tournoisService.recherche(keyword, mode);
            Platform.runLater(() -> refreshTable(result));
        }).start();
    }

    // ================= TABLE =================

    private void refreshTable(List<Tournoi> list) {
        tournoisList.clear();

        for (Tournoi t : list) {
            Button save = new Button("Enregistrer");
            Button delete = new Button("Supprimer");
            Button calendrier = new Button("Calendrier");

            save.setOnAction(e -> {
                if (tournoisService.modifier(t)) {
                    new Alert(Alert.AlertType.INFORMATION, "Tournoi modifié").showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Erreur modification").showAndWait();
                }
            });

            delete.setOnAction(e -> supprimer(t));

            calendrier.setOnAction(e -> calendrier(t));

            // Enable calendrier button only if we are within 7 days before debut or after
            LocalDate debut = LocalDate.parse(t.getDebut());
            LocalDate oneWeekBeforeDebut = debut.minusWeeks(1);
            LocalDate today = LocalDate.now();

            // Check if tournament has parties
            boolean hasParties = t.getParties() != null && !t.getParties().isEmpty();

            // Disable if debut is more than 7 days in the future OR if tournament already has parties
            calendrier.setDisable(today.isBefore(oneWeekBeforeDebut) || hasParties);

            // Disable save and delete when tournament has parties ← ADD THESE TWO LINES
            save.setDisable(hasParties);
            delete.setDisable(hasParties);

            t.setActionButton(new HBox(5, save, delete, calendrier));
        }

        tournoisList.addAll(list);
        tableView.setItems(tournoisList);
    }

    // ================= CRUD =================

    private void supprimer(Tournoi t) {
        LocalDate debut = LocalDate.parse(t.getDebut());
        if (debut.isAfter(LocalDate.now())) {
            tournoisService.supprimer(t.getId());
            searchAndRefresh("", filterChoiceBox.getValue());
        } else {
            new Alert(Alert.AlertType.WARNING,
                    "Impossible de supprimer un tournoi déjà commencé").showAndWait();
        }
    }

    private void calendrier(Tournoi t) {
        tournoisService.calendrier(t.getId());

    }

    // ================= CUSTOM CELLS =================

    public static class DatePickerTableCell extends TableCell<Tournoi, String> {

        private final DatePicker picker = new DatePicker();
        private final boolean isDebut;

        public DatePickerTableCell(boolean isDebut) {
            this.isDebut = isDebut;

            picker.valueProperty().addListener((obs, o, n) -> {
                if (n != null && getTableRow() != null && getTableRow().getItem() != null) {
                    Tournoi t = getTableRow().getItem();
                    if (isDebut) t.setDebut(n.toString());
                    else t.setFin(n.toString());
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || getTableRow().getItem() == null) {
                setGraphic(null);
            } else {
                picker.setValue(item != null ? LocalDate.parse(item) : null);
                setGraphic(picker);
            }
        }
    }

    public static class SpinnerTableCell extends TableCell<Tournoi, String> {

        private final Spinner<Integer> spinner;
        private final SpinnerValueFactory.IntegerSpinnerValueFactory factory;

        public SpinnerTableCell(int min, int max) {
            factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
            spinner = new Spinner<>();
            spinner.setValueFactory(factory);
            spinner.setEditable(true);

            spinner.valueProperty().addListener((obs, o, n) -> {
                if (n != null && getTableRow() != null && getTableRow().getItem() != null) {
                    getTableRow().getItem().setMaximum(String.valueOf(n));
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || getTableRow().getItem() == null) {
                setGraphic(null);
            } else {
                try {
                    factory.setValue(item != null ? Integer.parseInt(item) : factory.getMin());
                } catch (NumberFormatException e) {
                    factory.setValue(factory.getMin());
                }
                setGraphic(spinner);
            }
        }
    }
}