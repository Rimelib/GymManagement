package com.salledesport.controller;

import com.salledesport.model.Administrateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Controller principal pour la navigation de l'application
 * Gère le chargement dynamique des différentes vues (Membres, Coachs, etc.)
 *
 * @author Rim EL IBRAHIMI & Aya AKHALOUI
 */
public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Label adminInfoLabel; // Optionnel : pour afficher le nom de l'admin connecté

    // ============ VARIABLES ============

    private Administrateur administrateurConnecte;

    // ============ INITIALISATION ============

    @FXML
    public void initialize() {
        System.out.println("🔧 Initialisation MainController...");

        // Charger la page d'accueil par défaut (Dashboard ou Membres)
        chargerDashboard();

        System.out.println("✅ MainController initialisé");
    }

    /**
     * Méthode pour recevoir les informations de l'administrateur connecté
     * Appelée depuis LoginController après authentification réussie
     *
     * @param admin - L'administrateur qui vient de se connecter
     */
    public void setAdministrateur(Administrateur admin) {
        this.administrateurConnecte = admin;

        // Afficher le nom de l'admin dans l'interface (si le label existe)
        if (adminInfoLabel != null) {
            adminInfoLabel.setText("👤 " + admin.getPrenom() + " " + admin.getNom());
        }

        System.out.println("👤 Administrateur connecté : " + admin.getPrenom() + " " + admin.getNom());
    }

    // ============ NAVIGATION ============

    @FXML
    private void chargerDashboard() {
        chargerVue("/fxml/dashboard.fxml", "Dashboard");
    }

    @FXML
    private void chargerMembres() {
        chargerVue("/fxml/membre.fxml", "Membres");
    }

    @FXML
    private void chargerAbonnements() {
        chargerVue("/fxml/abonnement.fxml", "Abonnements");
    }

    @FXML
    private void chargerCoachs() {
        chargerVue("/fxml/coach.fxml", "Coachs");
    }

    @FXML
    private void chargerSeances() {
        chargerVue("/fxml/seance.fxml", "Séances");
    }

    @FXML
    private void chargerPaiements() {
        chargerVue("/fxml/paiement.fxml", "Paiements");
    }

    // ============ MÉTHODE UTILITAIRE ============

    private void chargerVue(String cheminFXML, String nomVue) {
        try {
            System.out.println("📄 Chargement de la vue: " + nomVue);

            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(cheminFXML));
            Parent vue = loader.load();

            // Remplacer le contenu
            contentArea.getChildren().clear();
            contentArea.getChildren().add(vue);

            System.out.println("✅ Vue " + nomVue + " chargée avec succès");

        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement de " + nomVue + ": " + e.getMessage());
            e.printStackTrace();

            // Afficher une alerte à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de chargement");
            alert.setHeaderText("Impossible de charger " + nomVue);
            alert.setContentText("Fichier: " + cheminFXML + "\nErreur: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // ============ ACTIONS MENU ============

    @FXML
    private void handleQuitter() {
        System.out.println("👋 Fermeture de l'application...");
        System.exit(0);
    }

    @FXML
    private void handleAPropos() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Gestion Salle de Sport");
        alert.setContentText(
                "Version: 1.0\n" +
                        "Développé par: Rim EL IBRAHIMI & Aya AKHALOUI\n" +
                        "Date: Janvier 2026\n\n" +
                        "Application JavaFX + MySQL pour la gestion d'une salle de sport."
        );
        alert.showAndWait();
    }
}