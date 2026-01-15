package com.salledesport.dao;

import com.salledesport.model.Abonnement;
import com.salledesport.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAO {

    // CREATE
    public void ajouter(Abonnement abonnement) {
        String sql = "INSERT INTO Abonnement (type, prix, duree, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, abonnement.getType());
            stmt.setDouble(2, abonnement.getPrix());
            stmt.setInt(3, abonnement.getDuree());
            stmt.setString(4, abonnement.getDescription());

            stmt.executeUpdate();
            System.out.println("✅ Abonnement ajouté: " + abonnement.getType());

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout abonnement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout de l'abonnement", e);
        }
    }

    // READ ALL
    public List<Abonnement> getAll() {
        List<Abonnement> abonnements = new ArrayList<>();
        String sql = "SELECT * FROM Abonnement ORDER BY id_abonnement DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Abonnement a = new Abonnement();
                a.setId(rs.getInt("id_abonnement"));
                a.setType(rs.getString("type"));
                a.setPrix(rs.getDouble("prix"));
                a.setDuree(rs.getInt("duree"));
                a.setDescription(rs.getString("description"));
                abonnements.add(a);
            }

            System.out.println("✅ " + abonnements.size() + " abonnement(s) récupéré(s)");

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération abonnements: " + e.getMessage());
            e.printStackTrace();
        }

        return abonnements;
    }

    // READ BY ID
    public Abonnement getById(int id) {
        String sql = "SELECT * FROM Abonnement WHERE id_abonnement = ?";
        Abonnement a = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                a = new Abonnement();
                a.setId(rs.getInt("id_abonnement"));
                a.setType(rs.getString("type"));
                a.setPrix(rs.getDouble("prix"));
                a.setDuree(rs.getInt("duree"));
                a.setDescription(rs.getString("description"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération abonnement #" + id + ": " + e.getMessage());
            e.printStackTrace();
        }

        return a;
    }

    // UPDATE
    public void modifier(Abonnement abonnement) {
        String sql = "UPDATE Abonnement SET type=?, prix=?, duree=?, description=? WHERE id_abonnement=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, abonnement.getType());
            stmt.setDouble(2, abonnement.getPrix());
            stmt.setInt(3, abonnement.getDuree());
            stmt.setString(4, abonnement.getDescription());
            stmt.setInt(5, abonnement.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Abonnement #" + abonnement.getId() + " modifié");
            } else {
                System.out.println("⚠️ Aucun abonnement trouvé avec l'ID #" + abonnement.getId());
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur modification abonnement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la modification de l'abonnement", e);
        }
    }

    // DELETE
    public void supprimer(int id) {
        String sql = "DELETE FROM Abonnement WHERE id_abonnement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Abonnement #" + id + " supprimé");
            } else {
                System.out.println("⚠️ Aucun abonnement trouvé avec l'ID #" + id);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression abonnement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression de l'abonnement", e);
        }
    }
}