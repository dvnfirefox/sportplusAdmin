package com.bot.adminfront.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainMenuController {

    // Middle content pane
    @FXML
    private AnchorPane contentPane;

    // Called automatically after FXML loads
    @FXML
    public void initialize() {

    }

    //suite de de methode qui permet a la page javafx de controller la page afficher
    @FXML
    public void creezUtilisateur(){
        loadContent("/com/bot/adminfront/creezUtilisateur.fxml");
    }
    @FXML
    public void supprimerUtilisateur(){loadContent("/com/bot/adminfront/supprimerUtilisateur.fxml");}

    @FXML
    public void creezOfficiel(){
        loadContent("/com/bot/adminfront/creezOfficiel.fxml");
    }
    @FXML
    public void supprimerOfficiel(){loadContent("/com/bot/adminfront/supprimerOfficiel.fxml");}
    @FXML
    public void creezTournois(){
        loadContent("/com/bot/adminfront/creezTournois.fxml");
    }
    @FXML
    public void rechercheTournois(){loadContent("/com/bot/adminfront/rechercheTournois.fxml");}


    //methode pour changer le contenue de la fenetre selon la page demander par l'utilisateur
    private void loadContent(String fxmlPath) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(pane);

            // Make the loaded pane fill the parent
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}