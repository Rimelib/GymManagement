package com.salledesport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestMySQL {

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("TEST CONNEXION MYSQL");
        System.out.println("=================================\n");

        // Configuration connexion
        String url = "jdbc:mysql://localhost:3306/salle_sport";
        String user = "root";
        String password = ""; // Vide pour XAMPP par défaut

        Connection conn = null;

        try {
            // Étape 1 : Charger le driver MySQL
            System.out.println("📡 Chargement du driver MySQL...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver chargé avec succès !\n");

            // Étape 2 : Se connecter à MySQL
            System.out.println("🔌 Connexion à MySQL...");
            System.out.println("   URL: " + url);
            System.out.println("   User: " + user);
            System.out.println("   Password: " + (password.isEmpty() ? "(vide)" : "***"));

            conn = DriverManager.getConnection(url, user, password);

            // Étape 3 : Vérifier que la connexion fonctionne
            if (conn != null && !conn.isClosed()) {
                System.out.println("\n✅✅✅ CONNEXION RÉUSSIE ! ✅✅✅");
                System.out.println("✅ MySQL fonctionne parfaitement !");
                System.out.println("✅ Base de données 'salle_sport' accessible !");

                // Informations supplémentaires
                System.out.println("\n📊 Informations connexion:");
                System.out.println("   Database: " + conn.getCatalog());
                System.out.println("   Auto-commit: " + conn.getAutoCommit());

            }

        } catch (ClassNotFoundException e) {
            System.err.println("\n❌ ERREUR: Driver MySQL introuvable !");
            System.err.println("❌ Vérifiez que mysql-connector-j est dans pom.xml");
            System.err.println("❌ Détails: " + e.getMessage());

        } catch (SQLException e) {
            System.err.println("\n❌ ERREUR DE CONNEXION MySQL !");

            // Diagnostics détaillés
            if (e.getMessage().contains("Access denied")) {
                System.err.println("❌ Problème: Nom d'utilisateur ou mot de passe incorrect");
                System.err.println("💡 Solution: Vérifiez user='root' et password=''");

            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("❌ Problème: La base 'salle_sport' n'existe pas");
                System.err.println("💡 Solution: Créez la base dans phpMyAdmin");

            } else if (e.getMessage().contains("Communications link failure")) {
                System.err.println("❌ Problème: MySQL n'est pas démarré");
                System.err.println("💡 Solution: Lancez XAMPP et démarrez MySQL");

            } else {
                System.err.println("❌ Erreur: " + e.getMessage());
            }

            e.printStackTrace();

        } finally {
            // Fermer la connexion
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("\n🔒 Connexion fermée proprement.");
                } catch (SQLException e) {
                    System.err.println("⚠️ Erreur lors de la fermeture: " + e.getMessage());
                }
            }
        }

        System.out.println("\n=================================");
        System.out.println("FIN DU TEST");
        System.out.println("=================================");
    }
}