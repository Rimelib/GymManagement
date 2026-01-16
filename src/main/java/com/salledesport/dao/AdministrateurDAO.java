package com.salledesport.dao;

import com.salledesport.model.Administrateur;
import com.salledesport.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministrateurDAO {

    /**
     * MÉTHODE PRINCIPALE : Authentifier un administrateur
     *
     * @param username - Nom d'utilisateur
     * @param password - Mot de passe
     * @return Administrateur si authentification réussie, null sinon
     */
    public Administrateur authentifier(String username, String password) {
        String sql = "SELECT * FROM administrateur WHERE username = ? AND password = ?";
        Administrateur admin = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // L'administrateur existe et le mot de passe est correct
                admin = new Administrateur();
                admin.setId(rs.getInt("id_admin"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setNom(rs.getString("nom"));
                admin.setPrenom(rs.getString("prenom"));
                admin.setEmail(rs.getString("email"));

                Date dateCreation = rs.getDate("date_creation");
                if (dateCreation != null) {
                    admin.setDateCreation(dateCreation.toLocalDate());
                }

                System.out.println("✅ Authentification réussie pour: " + username);
            } else {
                System.out.println("❌ Échec d'authentification pour: " + username);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'authentification: " + e.getMessage());
            e.printStackTrace();
        }

        return admin;
    }

    /**
     * Vérifier si un username existe déjà
     *
     * @param username - Nom d'utilisateur à vérifier
     * @return true si existe, false sinon
     */
    public boolean usernameExiste(String username) {
        String sql = "SELECT COUNT(*) as total FROM administrateur WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur vérification username: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Récupérer tous les administrateurs (pour gestion ultérieure)
     */
    public List<Administrateur> getAll() {
        List<Administrateur> admins = new ArrayList<>();
        String sql = "SELECT * FROM administrateur ORDER BY nom, prenom";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Administrateur admin = new Administrateur();
                admin.setId(rs.getInt("id_admin"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setNom(rs.getString("nom"));
                admin.setPrenom(rs.getString("prenom"));
                admin.setEmail(rs.getString("email"));

                Date dateCreation = rs.getDate("date_creation");
                if (dateCreation != null) {
                    admin.setDateCreation(dateCreation.toLocalDate());
                }

                admins.add(admin);
            }

            System.out.println("✅ " + admins.size() + " administrateur(s) récupéré(s)");

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération administrateurs: " + e.getMessage());
            e.printStackTrace();
        }

        return admins;
    }

    /**
     * Ajouter un nouvel administrateur (optionnel, pour gérer les admins)
     */
    public void ajouter(Administrateur admin) {
        String sql = "INSERT INTO administrateur (username, password, nom, prenom, email, date_creation) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getNom());
            stmt.setString(4, admin.getPrenom());
            stmt.setString(5, admin.getEmail());
            stmt.setDate(6, Date.valueOf(admin.getDateCreation()));

            stmt.executeUpdate();
            System.out.println("✅ Administrateur ajouté: " + admin.getUsername());

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout administrateur: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout de l'administrateur", e);
        }
    }

    /**
     * Modifier le mot de passe d'un administrateur
     */
    public void changerMotDePasse(int idAdmin, String nouveauPassword) {
        String sql = "UPDATE administrateur SET password = ? WHERE id_admin = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nouveauPassword);
            stmt.setInt(2, idAdmin);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Mot de passe changé pour l'admin #" + idAdmin);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur changement mot de passe: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du changement de mot de passe", e);
        }
    }
}