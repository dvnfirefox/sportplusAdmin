package com.bot.adminfront.controller.utilisateur;
import com.bot.adminfront.service.UtilisateurService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreezController {

    private final UtilisateurService utilisateurService = new UtilisateurService();
    @FXML
    public TextField nom;
    @FXML
    public TextField password;
    @FXML
    public Label statut;
    @FXML
    public CheckBox admin;

    @FXML
    public void creez(){
        //verification si les champs ne sont pas vide et lenvoie au utilisateur service pour traitement
        if(nom.getText().equals("") || password.getText().equals("")){
            statut.setText("Utilisateur ou mot de passe ne peut pas être vide");
        }else{
            ObjectNode result = utilisateurService.creez(nom.getText(), password.getText(), admin.isSelected());
            statut.setText(result.get("message").toString());
            //si le traiment est bon les champs seront vider et le status seront changer pour le message approprier
            if(result.get("status").asBoolean()){
                nom.clear();
                password.clear();
            }
        }

    }
}
