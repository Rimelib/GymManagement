package com.salledesport.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour gérer les connexions à la base de données MySQL.
 * Cette classe centralise toutes les informations de connexion.
 *
 * Tous les DAO utilisent cette classe pour se connecter à MySQL.
 */
public class DatabaseConnection {

    // =============== CONFIGURATION BASE DE DONNÉES ===============

    // URL de connexion à MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/salle_sport";

    // Nom d'utilisateur MySQL (par défaut "root" pour XAMPP)
    private static final String USER = "root";

    // Mot de passe MySQL (vide par défaut pour XAMPP)
    private static final String PASSWORD = "";

    // Driver JDBC MySQL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";


    // =============== MÉTHODES ===============

    /**
     * Obtenir une connexion à la base de données.
     *
     * @return Connection - Connexion active à MySQL
     * @throws RuntimeException si la connexion échoue
     */
    public static Connection getConnection() {
        try {
            // 1. Charger le driver MySQL
            Class.forName(DRIVER);

            // 2. Établir la connexion
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 3. Retourner la connexion
            return conn;

        } catch (ClassNotFoundException e) {
            // Erreur : Driver MySQL introuvable
            System.err.println("❌ ERREUR: Driver MySQL introuvable !");
            System.err.println("Vérifiez que mysql-connector-j est dans pom.xml");
            throw new RuntimeException("Driver MySQL introuvable", e);

        } catch (SQLException e) {
            // Erreur : Impossible de se connecter
            System.err.println("❌ ERREUR: Impossible de se connecter à MySQL !");
            System.err.println("Vérifiez que:");
            System.err.println("  - XAMPP MySQL est démarré");
            System.err.println("  - La base 'salle_sport' existe");
            System.err.println("  - User/Password sont corrects");
            throw new RuntimeException("Connexion MySQL échouée", e);
        }
    }

    /**
     * Fermer proprement une connexion.
     *
     * @param conn - Connexion à fermer
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("🔒 Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("⚠️ Erreur lors de la fermeture: " + e.getMessage());
            }
        }
    }

    /**
     * Tester la connexion (méthode utilitaire).
     *
     * @return true si connexion OK, false sinon
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            boolean isValid = conn != null && !conn.isClosed();
            closeConnection(conn);
            return isValid;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("🔍 Test de connexion...");
        System.out.println("URL: " + URL);
        System.out.println("USER: " + USER);

        try {
            Connection conn = getConnection();
            System.out.println("✅ Connexion réussie !");

            // Tester la table administrateur
            String sql = "SELECT * FROM administrateur";
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(sql);

            System.out.println("\n📋 Administrateurs dans la BD :");
            while (rs.next()) {
                System.out.println("  - ID: " + rs.getInt("id_admin") +
                        " | Username: " + rs.getString("username") +
                        " | Nom: " + rs.getString("nom") + " " + rs.getString("prenom"));
            }

            conn.close();

        } catch (Exception e) {
            System.err.println("❌ ERREUR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}