package com.bot.adminfront.controller.tournois;

import com.bot.adminfront.service.FederationService;
import com.bot.adminfront.service.TournoisService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class CreezController {

    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private Spinner<Integer> maximum;
    @FXML
    private ComboBox<String> federation;
    @FXML
    private ComboBox<String> categorie;
    @FXML
    private Label status;

    private final TournoisService tournoisService = new TournoisService();
    private final FederationService federationService = new FederationService();

    @FXML
    public void initialize() {
        LocalDate today = LocalDate.now();

        dateDebut.setValue(today);
        dateFin.setValue(today);

        dateDebut.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });

        dateFin.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });

        maximum.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1)
        );

        categorie.getItems().addAll(
                "Moustique",
                "Atome",
                "Peewee",
                "Bantam",
                "Midget",
                "Junior"
        );
        categorie.setValue("Peewee");

        // Load federations from the backend
        loadFederations();
    }

    private void loadFederations() {
        try {
            federation.getItems().clear();
            federation.getItems().addAll(federationService.list());
        } catch (Exception e) {
            status.setText("Erreur de chargement des fédérations");
            e.printStackTrace();
        }
    }

    public void creez() {
        if (isComboBoxEmpty(federation)
                || dateDebut.getValue() == null
                || dateFin.getValue() == null
                || maximum.getValue() == null
                || isComboBoxEmpty(categorie)) {
            status.setText("Tous les champs sont obligatoires.");
            return;
        }

        if (dateFin.getValue().isBefore(dateDebut.getValue())) {
            status.setText("La date de fin ne peut pas être avant la date de début.");
            return;
        }

        boolean success = tournoisService.creez(
                dateDebut.getValue(),
                dateFin.getValue(),
                maximum.getValue(),
                categorie.getValue(),
                federation.getValue()  // Changed from getText() to getValue()
        );

        if (success) {
            // Clear form on success
            dateDebut.setValue(LocalDate.now());
            dateFin.setValue(LocalDate.now());
            maximum.getValueFactory().setValue(1);
            federation.setValue(null);
            categorie.setValue("Peewee");
            status.setText("Créé avec succès.");
        } else {
            status.setText("Erreur.");
        }
    }

    // Helper method to check if ComboBox is empty
    private boolean isComboBoxEmpty(ComboBox<String> comboBox) {
        return comboBox.getValue() == null || comboBox.getValue().trim().isEmpty();
    }

    // Keeping these methods in case you need them elsewhere
    private boolean isEmpty(DatePicker picker) {
        return picker == null || picker.getValue() == null;
    }

    private boolean isEmpty(Spinner<Integer> spinner) {
        return spinner == null ||
                spinner.getValue() == null ||
                spinner.getValue() <= 0;
    }
}