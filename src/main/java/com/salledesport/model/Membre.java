package com.salledesport.model;

import java.time.LocalDate;

public class Membre {
    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String email;
    private String telephone;
    private LocalDate dateInscription;
    private int abonnementId;

    // Constructeur vide
    public Membre() {}

    // Constructeur complet
    public Membre(int id, String nom, String prenom, LocalDate dateNaissance,
                  String email, String telephone, LocalDate dateInscription, int abonnementId) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.telephone = telephone;
        this.dateInscription = dateInscription;
        this.abonnementId = abonnementId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public int getAbonnementId() {
        return abonnementId;
    }

    public void setAbonnementId(int abonnementId) {
        this.abonnementId = abonnementId;
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}