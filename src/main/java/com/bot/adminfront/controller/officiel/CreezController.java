package com.bot.adminfront.controller.officiel;

import com.bot.adminfront.service.OfficielService;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
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
    public TextField federation;
    @FXML
    public TextField role;
    @FXML
    public Label status;

    private final OfficielService officielService = new OfficielService();

    public void creez() {
        //verification si tout les champs sont remplie
        if (isEmpty(nom) ||
                isEmpty(phone) ||
                isEmpty(courriel) ||
                isEmpty(adresse) ||
                isEmpty(federation) ||
                isEmpty(role)) {
            //changer le statut de du formulaire si tout ou un des champs est vide
            status.setText("Tous les champs sont obligatoires.");
        }
        //creation dun officiel
        if (officielService.creez(nom.getText(), phone.getText(),courriel.getText(),adresse.getText(),federation.getText(),role.getText())) {
            nom.clear();
            phone.clear();
            courriel.clear();
            adresse.clear();
            federation.clear();
            role.clear();
            status.setText("Creation reussi");
        }else{
            status.setText("creation non effectuer");
        }
    }

    //methode pour simplifier la verification des champs vide
    private boolean isEmpty(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }




}
