package com.salledesport.model;

public class Abonnement {
    private int id;
    private String type;
    private double prix;
    private int duree;
    private String description;

    public Abonnement() {}

    public Abonnement(int id, String type, double prix, int duree, String description) {
        this.id = id;
        this.type = type;
        this.prix = prix;
        this.duree = duree;
        this.description = description;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return type + " (" + duree + " mois) - " + prix + " MAD";
    }
}
