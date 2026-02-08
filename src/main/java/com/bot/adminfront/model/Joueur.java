package com.bot.adminfront.model;

public class Joueur {

    private Long id;
    private String nom;
    private int numero;
    private Equipe equipe;

    // Constructors
    public Joueur() {
    }

    public Joueur(Long id, String nom, int numero) {
        this.id = id;
        this.nom = nom;
        this.numero = numero;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getNumero() {
        return numero;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }
}