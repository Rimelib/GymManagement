package com.salledesport.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Paiement {
    private int id;
    private BigDecimal montant;
    private LocalDate date;
    private int membreId;
    private int abonnementId;
    private String methodePaiement;

    // Constructeur vide
    public Paiement() {}

    // Constructeur complet
    public Paiement(int id, BigDecimal montant, LocalDate date, int membreId,
                    int abonnementId, String methodePaiement) {
        this.id = id;
        this.montant = montant;
        this.date = date;
        this.membreId = membreId;
        this.abonnementId = abonnementId;
        this.methodePaiement = methodePaiement;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getMembreId() {
        return membreId;
    }

    public void setMembreId(int membreId) {
        this.membreId = membreId;
    }

    public int getAbonnementId() {
        return abonnementId;
    }

    public void setAbonnementId(int abonnementId) {
        this.abonnementId = abonnementId;
    }

    public String getMethodePaiement() {
        return methodePaiement;
    }

    public void setMethodePaiement(String methodePaiement) {
        this.methodePaiement = methodePaiement;
    }

    @Override
    public String toString() {
        return "Paiement " + montant + " DH - " + date;
    }
}