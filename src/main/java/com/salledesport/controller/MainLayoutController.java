package com.salledesport.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

public class MainLayoutController {

    @FXML private StackPane contentArea;

    // Boutons de navigation
    @FXML private Button btnDashboard;
    @FXML private Button btnMembres;
    @FXML private Button btnCoachs;
    @FXML private Button btnSeances;
    @FXML private Button btnAbonnements;
    @FXML private Button btnPaiements;

    @FXML
    public void initialize() {
        System.out.println("🔧 Initialisation MainLayoutController...");
        // Charger le Dashboard par défaut
        chargerPage("/view/DashboardContent.fxml");
    }

    // ============ NAVIGATION ============

    @FXML
    private void handleNavigateDashboard() {
        chargerPage("/view/DashboardContent.fxml");
        setActiveButton(btnDashboard);
    }

    @FXML
    private void handleNavigateMembres() {
        chargerPage("/view/membre.fxml");
        setActiveButton(btnMembres);
    }

    @FXML
    private void handleNavigateCoachs() {
        chargerPage("/view/coach.fxml");
        setActiveButton(btnCoachs);
    }

    @FXML
    private void handleNavigateSeances() {
        chargerPage("/view/seance.fxml");
        setActiveButton(btnSeances);
    }

    @FXML
    private void handleNavigateAbonnements() {
        chargerPage("/view/abonnement.fxml");
        setActiveButton(btnAbonnements);
    }

    @FXML
    private void handleNavigatePaiements() {
        chargerPage("/view/paiement.fxml");
        setActiveButton(btnPaiements);
    }

    @FXML
    private void handleDeconnexion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Voulez-vous vraiment vous déconnecter ?");
        alert.setContentText("Vous serez redirigé vers la page de connexion.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                // Rediriger vers la page de login si elle existe
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/salledesport/view/Login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) contentArea.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println("❌ Erreur lors de la déconnexion: " + e.getMessage());
                e.printStackTrace();
                // Si pas de page login, juste fermer l'application
                System.exit(0);
            }
        }
    }

    // ============ GESTION DU CONTENU ============

    private void chargerPage(String fxmlPath) {
        try {
            System.out.println("📄 Chargement de la page: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();

            // Remplacer le contenu
            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);

            System.out.println("✅ Page chargée avec succès");

        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement de la page: " + fxmlPath);
            e.printStackTrace();

            // Afficher une erreur à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de chargement");
            alert.setHeaderText("Impossible de charger la page");
            alert.setContentText("Fichier: " + fxmlPath + "\nErreur: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // ============ GESTION DES BOUTONS ACTIFS ============

    private void setActiveButton(Button activeBtn) {
        // Retirer la classe active de tous les boutons
        btnDashboard.getStyleClass().remove("menu-btn-active");
        btnMembres.getStyleClass().remove("menu-btn-active");
        btnCoachs.getStyleClass().remove("menu-btn-active");
        btnSeances.getStyleClass().remove("menu-btn-active");
        btnAbonnements.getStyleClass().remove("menu-btn-active");
        btnPaiements.getStyleClass().remove("menu-btn-active");

        // Ajouter la classe active au bouton cliqué
        if (!activeBtn.getStyleClass().contains("menu-btn-active")) {
            activeBtn.getStyleClass().add("menu-btn-active");
        }
    }
}