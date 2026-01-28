package com.bot.adminfront.model;

import javafx.scene.control.Button;

public class Tournoi {
    private String id;
    private String debut;
    private String fin;
    private String maximum;

    private String federation;
    private String categorie;
    private Button actionButton;

    public Tournoi(String id, String debut, String fin, String maximum, String federation, String categorie) {
        this.id = id;
        this.debut = debut;
        this.fin = fin;
        this.maximum = maximum;
        this.federation = federation;
        this.categorie = categorie;
    }

    // Getters & setters
    public String getId() { return id; }
    public String getDebut() { return debut; }
    public String getFin() { return fin; }
    public String getMaximum() { return maximum; }
    public String getFederation() { return federation; }
    public String getCategorie() { return categorie; }
    public Button getActionButton() { return actionButton; }

    public void setActionButton(Button actionButton) { this.actionButton = actionButton; }
}
