package com.bot.adminfront.model;

public class Partie {

    private long id;
    private String tournoi;
    private String date;
    private String equipeLocal;
    private String pointLocal;
    private String equipeVisiteur;
    private String pointVisiteur;

    // ================= CONSTRUCTORS =================

    public Partie() {
    }

    public Partie(long id, String tournoi, String date, String equipeLocal,
                  String pointLocal, String equipeVisiteur, String pointVisiteur) {
        this.id = id;
        this.tournoi = tournoi;
        this.date = date;
        this.equipeLocal = equipeLocal;
        this.pointLocal = pointLocal;
        this.equipeVisiteur = equipeVisiteur;
        this.pointVisiteur = pointVisiteur;
    }

    // ================= GETTERS & SETTERS =================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTournoi() {
        return tournoi;
    }

    public void setTournoi(String tournoi) {
        this.tournoi = tournoi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEquipeLocal() {
        return equipeLocal;
    }

    public void setEquipeLocal(String equipeLocal) {
        this.equipeLocal = equipeLocal;
    }

    public String getPointLocal() {
        return pointLocal;
    }

    public void setPointLocal(String pointLocal) {
        this.pointLocal = pointLocal;
    }

    public String getEquipeVisiteur() {
        return equipeVisiteur;
    }

    public void setEquipeVisiteur(String equipeVisiteur) {
        this.equipeVisiteur = equipeVisiteur;
    }

    public String getPointVisiteur() {
        return pointVisiteur;
    }

    public void setPointVisiteur(String pointVisiteur) {
        this.pointVisiteur = pointVisiteur;
    }

    @Override
    public String toString() {
        return "Partie{" +
                "id='" + id + '\'' +
                ", tournoi='" + tournoi + '\'' +
                ", date='" + date + '\'' +
                ", equipeLocal='" + equipeLocal + '\'' +
                ", pointLocal='" + pointLocal + '\'' +
                ", equipeVisiteur='" + equipeVisiteur + '\'' +
                ", pointVisiteur='" + pointVisiteur + '\'' +
                '}';
    }
}
