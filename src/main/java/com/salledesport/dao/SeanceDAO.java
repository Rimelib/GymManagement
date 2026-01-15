package com.salledesport.dao;

import com.salledesport.model.Seance;
import com.salledesport.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO {

    // CREATE - Ajouter une séance
    public void ajouter(Seance seance) {
        String sql = "INSERT INTO Seance (date, heure, type, coach_id, membre_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(seance.getDate()));
            stmt.setTime(2, Time.valueOf(seance.getHeure()));
            stmt.setString(3, seance.getType());
            stmt.setInt(4, seance.getCoachId());
            stmt.setInt(5, seance.getMembreId());

            stmt.executeUpdate();
            System.out.println("✅ Séance ajoutée: " + seance.getType() + " - " + seance.getDate());

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout séance: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout de la séance", e);
        }
    }

    // READ - Récupérer toutes les séances
    public List<Seance> getAll() {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM Seance ORDER BY date DESC, heure DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Seance s = new Seance();
                s.setId(rs.getInt("id_seance"));

                Date date = rs.getDate("date");
                if (date != null) {
                    s.setDate(date.toLocalDate());
                }

                Time heure = rs.getTime("heure");
                if (heure != null) {
                    s.setHeure(heure.toLocalTime());
                }

                s.setType(rs.getString("type"));
                s.setCoachId(rs.getInt("coach_id"));
                s.setMembreId(rs.getInt("membre_id"));

                seances.add(s);
            }

            System.out.println("✅ " + seances.size() + " séance(s) récupérée(s)");

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération séances: " + e.getMessage());
            e.printStackTrace();
        }

        return seances;
    }

    // READ - Récupérer une séance par ID
    public Seance getById(int id) {
        String sql = "SELECT * FROM Seance WHERE id_seance = ?";
        Seance s = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                s = new Seance();
                s.setId(rs.getInt("id_seance"));

                Date date = rs.getDate("date");
                if (date != null) {
                    s.setDate(date.toLocalDate());
                }

                Time heure = rs.getTime("heure");
                if (heure != null) {
                    s.setHeure(heure.toLocalTime());
                }

                s.setType(rs.getString("type"));
                s.setCoachId(rs.getInt("coach_id"));
                s.setMembreId(rs.getInt("membre_id"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération séance #" + id + ": " + e.getMessage());
            e.printStackTrace();
        }

        return s;
    }

    // UPDATE - Modifier une séance
    public void modifier(Seance seance) {
        String sql = "UPDATE Seance SET date=?, heure=?, type=?, coach_id=?, membre_id=? " +
                "WHERE id_seance=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(seance.getDate()));
            stmt.setTime(2, Time.valueOf(seance.getHeure()));
            stmt.setString(3, seance.getType());
            stmt.setInt(4, seance.getCoachId());
            stmt.setInt(5, seance.getMembreId());
            stmt.setInt(6, seance.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Séance #" + seance.getId() + " modifiée");
            } else {
                System.out.println("⚠️ Aucune séance trouvée avec l'ID #" + seance.getId());
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur modification séance: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la modification de la séance", e);
        }
    }

    // DELETE - Supprimer une séance
    public void supprimer(int id) {
        String sql = "DELETE FROM Seance WHERE id_seance = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Séance #" + id + " supprimée");
            } else {
                System.out.println("⚠️ Aucune séance trouvée avec l'ID #" + id);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression séance: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression de la séance", e);
        }
    }

    // SEARCH - Rechercher des séances par type ou date
    public List<Seance> rechercher(String critere) {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM Seance WHERE type LIKE ? OR date LIKE ? ORDER BY date DESC, heure DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + critere + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Seance s = new Seance();
                s.setId(rs.getInt("id_seance"));

                Date date = rs.getDate("date");
                if (date != null) {
                    s.setDate(date.toLocalDate());
                }

                Time heure = rs.getTime("heure");
                if (heure != null) {
                    s.setHeure(heure.toLocalTime());
                }

                s.setType(rs.getString("type"));
                s.setCoachId(rs.getInt("coach_id"));
                s.setMembreId(rs.getInt("membre_id"));

                seances.add(s);
            }

            System.out.println("✅ " + seances.size() + " résultat(s) pour: '" + critere + "'");

        } catch (SQLException e) {
            System.err.println("❌ Erreur recherche: " + e.getMessage());
            e.printStackTrace();
        }

        return seances;
    }

    // Filtrer les séances par coach
    public List<Seance> filtrerParCoach(int coachId) {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM Seance WHERE coach_id = ? ORDER BY date DESC, heure DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, coachId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Seance s = new Seance();
                s.setId(rs.getInt("id_seance"));

                Date date = rs.getDate("date");
                if (date != null) {
                    s.setDate(date.toLocalDate());
                }

                Time heure = rs.getTime("heure");
                if (heure != null) {
                    s.setHeure(heure.toLocalTime());
                }

                s.setType(rs.getString("type"));
                s.setCoachId(rs.getInt("coach_id"));
                s.setMembreId(rs.getInt("membre_id"));

                seances.add(s);
            }

            System.out.println("✅ " + seances.size() + " séance(s) trouvée(s) pour le coach #" + coachId);

        } catch (SQLException e) {
            System.err.println("❌ Erreur filtre coach: " + e.getMessage());
            e.printStackTrace();
        }

        return seances;
    }
}
