package com.bot.adminfront.model;

import javafx.scene.layout.HBox;

public class Tournoi {

    private String id;
    private String debut;
    private String fin;
    private String maximum;
    private String federation;
    private String categorie;

    // Colonne Actions (Enregistrer / Supprimer)
    private HBox actionButton;

    // Constructeur
    public Tournoi(String id,
                   String debut,
                   String fin,
                   String maximum,
                   String federation,
                   String categorie) {
        this.id = id;
        this.debut = debut;
        this.fin = fin;
        this.maximum = maximum;
        this.federation = federation;
        this.categorie = categorie;
    }

    // ===== Getters =====
    public String getId() {
        return id;
    }

    public String getDebut() {
        return debut;
    }

    public String getFin() {
        return fin;
    }

    public String getMaximum() {
        return maximum;
    }

    public String getFederation() {
        return federation;
    }

    public String getCategorie() {
        return categorie;
    }

    public HBox getActionButton() {
        return actionButton;
    }

    // ===== Setters =====
    public void setId(String id) {
        this.id = id;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public void setFederation(String federation) {
        this.federation = federation;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setActionButton(HBox actionButton) {
        this.actionButton = actionButton;
    }
}