package com.salledesport.dao;

import com.salledesport.model.Coach;
import com.salledesport.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachDAO {

    // CREATE
    public void ajouter(Coach coach) {
        String sql = "INSERT INTO Coach (nom, prenom, specialite, telephone, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, coach.getNom());
            stmt.setString(2, coach.getPrenom());
            stmt.setString(3, coach.getSpecialite());
            stmt.setString(4, coach.getTelephone());
            stmt.setString(5, coach.getEmail());

            stmt.executeUpdate();
            System.out.println("✅ Coach ajouté: " + coach.toString());

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout coach: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout du coach", e);
        }
    }

    // READ ALL
    public List<Coach> getAll() {
        List<Coach> coachs = new ArrayList<>();
        String sql = "SELECT * FROM Coach ORDER BY id_coach DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Coach c = new Coach();
                c.setId(rs.getInt("id_coach"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setSpecialite(rs.getString("specialite"));
                c.setTelephone(rs.getString("telephone"));
                c.setEmail(rs.getString("email"));
                coachs.add(c);
            }

            System.out.println("✅ " + coachs.size() + " coach(s) récupéré(s)");

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération coachs: " + e.getMessage());
            e.printStackTrace();
        }

        return coachs;
    }

    // READ BY ID
    public Coach getById(int id) {
        String sql = "SELECT * FROM Coach WHERE id_coach = ?";
        Coach c = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                c = new Coach();
                c.setId(rs.getInt("id_coach"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setSpecialite(rs.getString("specialite"));
                c.setTelephone(rs.getString("telephone"));
                c.setEmail(rs.getString("email"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération coach #" + id + ": " + e.getMessage());
            e.printStackTrace();
        }

        return c;
    }

    // UPDATE
    public void modifier(Coach coach) {
        String sql = "UPDATE Coach SET nom=?, prenom=?, specialite=?, telephone=?, email=? WHERE id_coach=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, coach.getNom());
            stmt.setString(2, coach.getPrenom());
            stmt.setString(3, coach.getSpecialite());
            stmt.setString(4, coach.getTelephone());
            stmt.setString(5, coach.getEmail());
            stmt.setInt(6, coach.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Coach #" + coach.getId() + " modifié");
            } else {
                System.out.println("⚠️ Aucun coach trouvé avec l'ID #" + coach.getId());
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur modification coach: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la modification du coach", e);
        }
    }

    // DELETE
    public void supprimer(int id) {
        String sql = "DELETE FROM Coach WHERE id_coach = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Coach #" + id + " supprimé");
            } else {
                System.out.println("⚠️ Aucun coach trouvé avec l'ID #" + id);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression coach: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du coach", e);
        }
    }

    // SEARCH
    public List<Coach> rechercherParSpecialite(String specialite) {
        List<Coach> coachs = new ArrayList<>();
        String sql = "SELECT * FROM Coach WHERE specialite LIKE ? ORDER BY id_coach DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + specialite + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Coach c = new Coach();
                c.setId(rs.getInt("id_coach"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setSpecialite(rs.getString("specialite"));
                c.setTelephone(rs.getString("telephone"));
                c.setEmail(rs.getString("email"));
                coachs.add(c);
            }

            System.out.println("✅ " + coachs.size() + " résultat(s) pour spécialité: '" + specialite + "'");

        } catch (SQLException e) {
            System.err.println("❌ Erreur recherche: " + e.getMessage());
            e.printStackTrace();
        }

        return coachs;
    }

    public List<Coach> rechercher(String critere) {
        List<Coach> coachs = new ArrayList<>();
        String sql = "SELECT * FROM Coach WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? ORDER BY id_coach DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + critere + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Coach c = new Coach();
                c.setId(rs.getInt("id_coach"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setSpecialite(rs.getString("specialite"));
                c.setTelephone(rs.getString("telephone"));
                c.setEmail(rs.getString("email"));
                coachs.add(c);
            }

            System.out.println("✅ " + coachs.size() + " résultat(s) pour: '" + critere + "'");

        } catch (SQLException e) {
            System.err.println("❌ Erreur recherche: " + e.getMessage());
            e.printStackTrace();
        }

        return coachs;
    }
}