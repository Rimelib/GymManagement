package com.salledesport.controller;

import com.salledesport.dao.AdministrateurDAO;
import com.salledesport.model.Administrateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    // ============ COMPOSANTS FXML ============

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button connexionButton;
    @FXML private Label messageLabel;
    @FXML private CheckBox afficherPasswordCheckBox;

    // ============ VARIABLES ============

    private AdministrateurDAO adminDAO;
    private TextField passwordVisible; // Pour afficher le mot de passe

    // ============ INITIALISATION ============

    @FXML
    public void initialize() {
        System.out.println("🔧 Initialisation LoginController...");
        adminDAO = new AdministrateurDAO();

        // Configurer l'affichage du mot de passe
        configurerAffichagePassword();

        // Permettre la connexion avec la touche Entrée
        passwordField.setOnAction(event -> handleConnexion());

        System.out.println("✅ LoginController initialisé");
    }

    // ============ GESTION CONNEXION ============

    @FXML
    private void handleConnexion() {
        // 1. Récupérer les valeurs saisies
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // 2. Vider le message d'erreur précédent
        messageLabel.setText("");

        // 3. Validation de base
        if (username.isEmpty() || password.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs");
            return;
        }

        // 4. Désactiver le bouton pendant la vérification
        connexionButton.setDisable(true);
        messageLabel.setText("Vérification en cours...");
        messageLabel.setStyle("-fx-text-fill: #007bff;");

        try {
            // 5. Authentifier avec la base de données
            Administrateur admin = adminDAO.authentifier(username, password);

            if (admin != null) {
                // ✅ AUTHENTIFICATION RÉUSSIE
                System.out.println("✅ Connexion réussie: " + admin.getNom() + " " + admin.getPrenom());

                // Afficher message de succès
                afficherSucces("Connexion réussie ! Bienvenue " + admin.getPrenom());

                // Attendre un peu pour que l'utilisateur voie le message
                Thread.sleep(1000);

                // Ouvrir l'application principale
                ouvrirApplicationPrincipale(admin);

            } else {
                // ❌ AUTHENTIFICATION ÉCHOUÉE
                afficherErreur("Nom d'utilisateur ou mot de passe incorrect");

                // Vider le mot de passe pour sécurité
                passwordField.clear();
                usernameField.requestFocus();
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la connexion: " + e.getMessage());
            e.printStackTrace();
            afficherErreur("Erreur de connexion à la base de données");
        } finally {
            // 6. Réactiver le bouton
            connexionButton.setDisable(false);
        }
    }

    @FXML
    private void handleQuitter() {
        System.out.println("👋 Fermeture de l'application...");
        System.exit(0);
    }

    // ============ NAVIGATION ============

    private void ouvrirApplicationPrincipale(Administrateur admin) {
        try {
            System.out.println("📂 Ouverture de l'application principale...");

            // Charger le fichier FXML principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();

            // Récupérer le controller principal et lui passer les infos de l'admin
            MainController mainController = loader.getController();
            mainController.setAdministrateur(admin);

            // Créer la nouvelle scène
            Scene scene = new Scene(root, 1200, 800);

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) connexionButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("🏋️ Gestion Salle de Sport - " + admin.getPrenom() + " " + admin.getNom());
            stage.setResizable(true);  // Permettre le redimensionnement de l'app principale
            stage.setMaximized(true);  // Ouvrir en plein écran
            stage.show();

            System.out.println("✅ Application principale ouverte");
            System.out.println("👤 Connecté en tant que : " + admin.getPrenom() + " " + admin.getNom());

        } catch (IOException e) {
            System.err.println("❌ Erreur ouverture application principale: " + e.getMessage());
            e.printStackTrace();
            afficherErreur("Impossible d'ouvrir l'application principale");
        }
    }

    // ============ AFFICHAGE MOT DE PASSE ============

    private void configurerAffichagePassword() {
        // Cette fonctionnalité permet de voir le mot de passe en clair
        // Pour l'implémenter complètement, il faudrait ajouter un TextField dans le FXML
        // Pour simplifier, on la laisse pour plus tard

        if (afficherPasswordCheckBox != null) {
            afficherPasswordCheckBox.setOnAction(event -> {
                // Logique pour afficher/masquer le mot de passe
                // À implémenter si besoin
            });
        }
    }

    // ============ MESSAGES UTILISATEUR ============

    private void afficherSucces(String message) {
        messageLabel.setText("✅ " + message);
        messageLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
    }

    private void afficherErreur(String message) {
        messageLabel.setText("❌ " + message);
        messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");

        // Animation : faire trembler les champs (optionnel)
        usernameField.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2;");
        passwordField.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2;");

        // Retirer la bordure rouge après 2 secondes
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> {
                    usernameField.setStyle("");
                    passwordField.setStyle("");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ============ ACTIONS SUPPLÉMENTAIRES ============

    @FXML
    private void handleMotDePasseOublie() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mot de passe oublié");
        alert.setHeaderText("Contactez l'administrateur système");
        alert.setContentText(
                "Pour réinitialiser votre mot de passe, veuillez contacter :\n\n" +
                        "Email : admin@salledesport.ma\n" +
                        "Téléphone : 0612345678"
        );
        alert.showAndWait();
    }
}