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
            // ✅ Démarrer avec la page de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root); // ✅ Augmenté de 600 à 680
            primaryStage.setTitle("🏋️ Gym Management - Connexion");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
            primaryStage.show();

            System.out.println("✅ Application lancée - Page de login");

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