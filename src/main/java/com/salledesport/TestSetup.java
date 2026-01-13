package com.salledesport;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestSetup extends Application {

    @Override
    public void start(Stage stage) {
        Label titleLabel = new Label("✅ Test Configuration");
        Label javafxLabel = new Label("JavaFX: OK");

        Button testDbButton = new Button("Tester MySQL");
        Label dbLabel = new Label("MySQL: Non testé");

        testDbButton.setOnAction(e -> {
            try {
                // Tester connexion MySQL
                String url = "jdbc:mysql://localhost:3306/salle_sport";
                String user = "root";
                String password = "";

                Connection conn = DriverManager.getConnection(url, user, password);
                dbLabel.setText("✅ MySQL: Connexion OK !");
                conn.close();

            } catch (Exception ex) {
                dbLabel.setText("❌ MySQL: " + ex.getMessage());
            }
        });

        VBox root = new VBox(20);
        root.getChildren().addAll(titleLabel, javafxLabel, testDbButton, dbLabel);
        root.setStyle("-fx-padding: 30; -fx-alignment: center;");

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Test Configuration");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}