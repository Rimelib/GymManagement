package com.salledesport.model;

import java.time.LocalDate;

public class Administrateur {
    private int id;
    private String username;
    private String password;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateCreation;

    // Constructeur vide
    public Administrateur() {}

    // Constructeur complet
    public Administrateur(int id, String username, String password, String nom,
                          String prenom, String email, LocalDate dateCreation) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + username + ")";
    }
}
