package com.bot.adminfront.controller.officiel;

import com.bot.adminfront.service.FederationService;
import com.bot.adminfront.service.OfficielService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreezController {

    @FXML
    public TextField nom;
    @FXML
    public TextField phone;
    @FXML
    public TextField courriel;
    @FXML
    public TextField adresse;
    @FXML
    public ComboBox<String> federation;
    @FXML
    public TextField role;
    @FXML
    public Label status;

    private final OfficielService officielService = new OfficielService();
    private final FederationService federationService = new FederationService();

    @FXML
    public void initialize() {
        // Load federations when the form is initialized
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
        // Verification si tous les champs sont remplis
        if (isEmpty(nom) ||
                isEmpty(phone) ||
                isEmpty(courriel) ||
                isEmpty(adresse) ||
                isComboBoxEmpty(federation) ||
                isEmpty(role)) {
            // Changer le statut du formulaire si tout ou un des champs est vide
            status.setText("Tous les champs sont obligatoires.");
            return;
        }

        // Creation d'un officiel
        if (officielService.creez(nom.getText(), phone.getText(), courriel.getText(),
                adresse.getText(), federation.getValue(), role.getText())) {
            nom.clear();
            phone.clear();
            courriel.clear();
            adresse.clear();
            federation.setValue(null);
            role.clear();
            status.setText("Creation reussi");
        } else {
            status.setText("creation non effectuer");
        }
    }

    // Methode pour simplifier la verification des champs vides
    private boolean isEmpty(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    // Methode pour verifier si le ComboBox est vide
    private boolean isComboBoxEmpty(ComboBox<String> comboBox) {
        return comboBox.getValue() == null || comboBox.getValue().trim().isEmpty();
    }
}