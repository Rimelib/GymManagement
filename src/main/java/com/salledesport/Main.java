package com.salledesport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le layout principal avec sidebar persistante
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainLayout.fxml"));
            Parent root = loader.load();

            // Créer la scène
            Scene scene = new Scene(root, 1400, 900);

            // Configurer la fenêtre principale
            primaryStage.setTitle("💪 Gym Management System");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(800);
            primaryStage.setMaximized(true);
            primaryStage.show();

            System.out.println("✅ Application lancée avec succès !");
            System.out.println("📌 Layout principal chargé avec sidebar persistante");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du lancement de l'application:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("🚀 Démarrage de Gym Management System...");
        launch(args);
    }
}