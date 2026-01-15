package com.salledesport.dao;

import com.salledesport.model.Membre;
import com.salledesport.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDAO {

    // CREATE - Ajouter un membre
    public void ajouter(Membre membre) {
        String sql = "INSERT INTO Membre (nom, prenom, date_naissance, email, telephone, date_inscription, abonnement_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setDate(3, Date.valueOf(membre.getDateNaissance()));
            stmt.setString(4, membre.getEmail());
            stmt.setString(5, membre.getTelephone());
            stmt.setDate(6, Date.valueOf(membre.getDateInscription()));
            stmt.setInt(7, membre.getAbonnementId());

            stmt.executeUpdate();
            System.out.println("✅ Membre ajouté: " + membre.getNom() + " " + membre.getPrenom());

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout membre: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout du membre", e);
        }
    }

    // READ - Récupérer tous les membres
    public List<Membre> getAll() {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM Membre ORDER BY id_membre DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Membre m = new Membre();
                m.setId(rs.getInt("id_membre"));
                m.setNom(rs.getString("nom"));
                m.setPrenom(rs.getString("prenom"));

                Date dateNaissance = rs.getDate("date_naissance");
                if (dateNaissance != null) {
                    m.setDateNaissance(dateNaissance.toLocalDate());
                }

                m.setEmail(rs.getString("email"));
                m.setTelephone(rs.getString("telephone"));

                Date dateInscription = rs.getDate("date_inscription");
                if (dateInscription != null) {
                    m.setDateInscription(dateInscription.toLocalDate());
                }

                m.setAbonnementId(rs.getInt("abonnement_id"));

                membres.add(m);
            }

            System.out.println("✅ " + membres.size() + " membre(s) récupéré(s)");

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération membres: " + e.getMessage());
            e.printStackTrace();
        }

        return membres;
    }

    // READ - Récupérer un membre par ID
    public Membre getById(int id) {
        String sql = "SELECT * FROM Membre WHERE id_membre = ?";
        Membre m = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                m = new Membre();
                m.setId(rs.getInt("id_membre"));
                m.setNom(rs.getString("nom"));
                m.setPrenom(rs.getString("prenom"));

                Date dateNaissance = rs.getDate("date_naissance");
                if (dateNaissance != null) {
                    m.setDateNaissance(dateNaissance.toLocalDate());
                }

                m.setEmail(rs.getString("email"));
                m.setTelephone(rs.getString("telephone"));

                Date dateInscription = rs.getDate("date_inscription");
                if (dateInscription != null) {
                    m.setDateInscription(dateInscription.toLocalDate());
                }

                m.setAbonnementId(rs.getInt("abonnement_id"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération membre #" + id + ": " + e.getMessage());
            e.printStackTrace();
        }

        return m;
    }

    // UPDATE - Modifier un membre
    public void modifier(Membre membre) {
        String sql = "UPDATE Membre SET nom=?, prenom=?, date_naissance=?, email=?, " +
                "telephone=?, abonnement_id=? WHERE id_membre=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setDate(3, Date.valueOf(membre.getDateNaissance()));
            stmt.setString(4, membre.getEmail());
            stmt.setString(5, membre.getTelephone());
            stmt.setInt(6, membre.getAbonnementId());
            stmt.setInt(7, membre.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Membre #" + membre.getId() + " modifié");
            } else {
                System.out.println("⚠️ Aucun membre trouvé avec l'ID #" + membre.getId());
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur modification membre: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la modification du membre", e);
        }
    }

    // DELETE - Supprimer un membre
    public void supprimer(int id) {
        String sql = "DELETE FROM Membre WHERE id_membre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Membre #" + id + " supprimé");
            } else {
                System.out.println("⚠️ Aucun membre trouvé avec l'ID #" + id);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression membre: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du membre", e);
        }
    }

    // SEARCH - Rechercher des membres
    public List<Membre> rechercher(String critere) {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM Membre WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? ORDER BY id_membre DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + critere + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Membre m = new Membre();
                m.setId(rs.getInt("id_membre"));
                m.setNom(rs.getString("nom"));
                m.setPrenom(rs.getString("prenom"));

                Date dateNaissance = rs.getDate("date_naissance");
                if (dateNaissance != null) {
                    m.setDateNaissance(dateNaissance.toLocalDate());
                }

                m.setEmail(rs.getString("email"));
                m.setTelephone(rs.getString("telephone"));

                Date dateInscription = rs.getDate("date_inscription");
                if (dateInscription != null) {
                    m.setDateInscription(dateInscription.toLocalDate());
                }

                m.setAbonnementId(rs.getInt("abonnement_id"));

                membres.add(m);
            }

            System.out.println("✅ " + membres.size() + " résultat(s) pour: '" + critere + "'");

        } catch (SQLException e) {
            System.err.println("❌ Erreur recherche: " + e.getMessage());
            e.printStackTrace();
        }

        return membres;
    }
}