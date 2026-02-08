package com.bot.adminfront.model;

import java.util.List;

public class Equipe {

    private Long id;
    private String nom;
    private String federation;
    private String categorie;
    private List<Joueur> joueurs;
    private List<Tournoi> tournois;

    // Constructors
    public Equipe() {
    }

    public Equipe(Long id, String nom, String federation, String categorie) {
        this.id = id;
        this.nom = nom;
        this.federation = federation;
        this.categorie = categorie;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getFederation() {
        return federation;
    }

    public String getCategorie() {
        return categorie;
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public List<Tournoi> getTournois() {
        return tournois;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setFederation(String federation) {
        this.federation = federation;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public void setTournois(List<Tournoi> tournois) {
        this.tournois = tournois;
    }
}