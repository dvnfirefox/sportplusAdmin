package com.bot.adminfront.controller.tournois;

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
    private TextField federation;
    @FXML
    private ComboBox<String> categorie;
    @FXML
    private Label status;

    private final TournoisService tournoisService = new TournoisService();

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
        categorie.setValue("Peewee (11-12 ans)");
    }


    public void creez() {
        if (federation.getText().isEmpty()
                || dateDebut.getValue() == null
                || dateFin.getValue() == null
                || maximum.getValue() == null) {  // Spinner can't be null, but just in case
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
                maximum.getValue(),   // <-- Spinner value
                categorie.getValue(),
                federation.getText()
        );

        status.setText(success ? "Créé avec succès." : "Erreur.");
    }



    private boolean isEmpty(TextField field) {
        System.out.println(field.getText());
        return field == null
                || field.getText() == null
                || field.getText().trim().isEmpty();
    }
    private boolean isEmpty(DatePicker picker) {
        return picker == null || picker.getValue() == null;
    }
    private boolean isEmpty(Spinner<Integer> spinner) {
        System.out.println(spinner.getValue());
        return spinner == null ||
                spinner.getValue() == null ||
                spinner.getValue() <= 0;
    }
}
