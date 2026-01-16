package com.salledesport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Classe principale de l'application Gestion Salle de Sport
 *
 * Point d'entrée de l'application JavaFX
 * L'application démarre avec la page de login (authentification admin)
 *
 * @author Rim EL IBRAHIMI & Aya AKHALOUI
 * @version 1.0
 * @date Janvier 2026
 */
public class Main extends Application {

    /**
     * Méthode principale qui lance l'interface graphique
     *
     * @param primaryStage - Fenêtre principale de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("════════════════════════════════════════════════════════");
            System.out.println("🚀 DÉMARRAGE DE L'APPLICATION SALLE DE SPORT");
            System.out.println("════════════════════════════════════════════════════════");
            System.out.println("📅 Date: " + java.time.LocalDateTime.now());
            System.out.println("👥 Développeurs: Rim EL IBRAHIMI & Aya AKHALOUI");
            System.out.println("════════════════════════════════════════════════════════");

            // ========================================
            // CHARGEMENT DE LA PAGE DE LOGIN
            // ========================================
            System.out.println("\n📂 Chargement de la page de connexion...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            System.out.println("✅ Page de connexion chargée avec succès");

            // ========================================
            // CRÉATION DE LA SCÈNE
            // ========================================
            Scene scene = new Scene(root, 900, 600);

            // ========================================
            // CONFIGURATION DE LA FENÊTRE
            // ========================================
            primaryStage.setTitle("🔐 Connexion - Gestion Salle de Sport");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);  // Empêcher le redimensionnement du login
            primaryStage.centerOnScreen();     // Centrer la fenêtre à l'écran

            // Optionnel : Ajouter une icône à la fenêtre
            // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));

            // Empêcher la fermeture directe (demander confirmation)
            primaryStage.setOnCloseRequest(event -> {
                System.out.println("\n════════════════════════════════════════════════════════");
                System.out.println("👋 FERMETURE DE L'APPLICATION");
                System.out.println("════════════════════════════════════════════════════════");
            });

            // Afficher la fenêtre
            primaryStage.show();

            System.out.println("\n════════════════════════════════════════════════════════");
            System.out.println("✅ APPLICATION DÉMARRÉE AVEC SUCCÈS !");
            System.out.println("🔐 En attente de connexion administrateur...");
            System.out.println("════════════════════════════════════════════════════════\n");

        } catch (Exception e) {
            System.err.println("\n════════════════════════════════════════════════════════");
            System.err.println("❌ ERREUR CRITIQUE AU DÉMARRAGE");
            System.err.println("════════════════════════════════════════════════════════");
            System.err.println("Message d'erreur : " + e.getMessage());
            System.err.println("\nStack trace :");
            e.printStackTrace();
            System.err.println("════════════════════════════════════════════════════════\n");

            // Afficher une alerte à l'utilisateur
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle("Erreur de démarrage");
            alert.setHeaderText("Impossible de démarrer l'application");
            alert.setContentText(
                    "Une erreur critique s'est produite :\n\n" +
                            e.getMessage() +
                            "\n\nVérifiez que :\n" +
                            "- Le fichier login.fxml existe dans resources/fxml/\n" +
                            "- XAMPP MySQL est démarré\n" +
                            "- La base de données 'salle_sport' existe"
            );
            alert.showAndWait();

            // Fermer l'application en cas d'erreur critique
            System.exit(1);
        }
    }

    /**
     * Point d'entrée Java standard
     * Lance l'application JavaFX
     *
     * @param args - Arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                                                        ║");
        System.out.println("║        🏋️  GESTION SALLE DE SPORT 🏋️                  ║");
        System.out.println("║                                                        ║");
        System.out.println("║        Application JavaFX + MySQL                     ║");
        System.out.println("║        Version 1.0 - Janvier 2026                     ║");
        System.out.println("║                                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        // Lancer l'application JavaFX
        launch(args);
    }
}