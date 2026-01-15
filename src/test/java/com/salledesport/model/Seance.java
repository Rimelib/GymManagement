package com.salledesport.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Seance {
    private int id;
    private LocalDate date;
    private LocalTime heure;
    private String type;
    private int coachId;
    private int membreId;

    // Constructeur vide
    public Seance() {}

    // Constructeur complet
    public Seance(int id, LocalDate date, LocalTime heure, String type, int coachId, int membreId) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.type = type;
        this.coachId = coachId;
        this.membreId = membreId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public int getMembreId() {
        return membreId;
    }

    public void setMembreId(int membreId) {
        this.membreId = membreId;
    }

    @Override
    public String toString() {
        return "Séance " + type + " - " + date + " à " + heure;
    }
}
