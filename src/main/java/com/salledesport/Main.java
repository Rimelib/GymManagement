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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 450, 600);

            primaryStage.setTitle("🏋️ Gym Management - Connexion");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
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