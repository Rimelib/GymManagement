package com.salledesport.dao;

import com.salledesport.model.Paiement;
import com.salledesport.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAO {

    // CREATE - Ajouter un paiement
    public void ajouter(Paiement paiement) {
        String sql = "INSERT INTO Paiement (montant, date, membre_id, abonnement_id, methode_paiement) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, paiement.getMontant());
            stmt.setDate(2, Date.valueOf(paiement.getDate()));
            stmt.setInt(3, paiement.getMembreId());
            stmt.setInt(4, paiement.getAbonnementId());
            stmt.setString(5, paiement.getMethodePaiement());

            stmt.executeUpdate();
            System.out.println("✅ Paiement ajouté: " + paiement.getMontant() + " DH");

        } catch (SQLException e) {
            System.err.println("❌ Erreur ajout paiement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout du paiement", e);
        }
    }

    // READ - Récupérer tous les paiements
    public List<Paiement> getAll() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM Paiement ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Paiement p = new Paiement();
                p.setId(rs.getInt("id_paiement"));
                p.setMontant(rs.getBigDecimal("montant"));

                Date date = rs.getDate("date");
                if (date != null) {
                    p.setDate(date.toLocalDate());
                }

                p.setMembreId(rs.getInt("membre_id"));
                p.setAbonnementId(rs.getInt("abonnement_id"));
                p.setMethodePaiement(rs.getString("methode_paiement"));

                paiements.add(p);
            }

            System.out.println("✅ " + paiements.size() + " paiement(s) récupéré(s)");

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération paiements: " + e.getMessage());
            e.printStackTrace();
        }

        return paiements;
    }

    // READ - Récupérer un paiement par ID
    public Paiement getById(int id) {
        String sql = "SELECT * FROM Paiement WHERE id_paiement = ?";
        Paiement p = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Paiement();
                p.setId(rs.getInt("id_paiement"));
                p.setMontant(rs.getBigDecimal("montant"));

                Date date = rs.getDate("date");
                if (date != null) {
                    p.setDate(date.toLocalDate());
                }

                p.setMembreId(rs.getInt("membre_id"));
                p.setAbonnementId(rs.getInt("abonnement_id"));
                p.setMethodePaiement(rs.getString("methode_paiement"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération paiement #" + id + ": " + e.getMessage());
            e.printStackTrace();
        }

        return p;
    }

    // UPDATE - Modifier un paiement
    public void modifier(Paiement paiement) {
        String sql = "UPDATE Paiement SET montant=?, date=?, membre_id=?, abonnement_id=?, methode_paiement=? " +
                "WHERE id_paiement=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, paiement.getMontant());
            stmt.setDate(2, Date.valueOf(paiement.getDate()));
            stmt.setInt(3, paiement.getMembreId());
            stmt.setInt(4, paiement.getAbonnementId());
            stmt.setString(5, paiement.getMethodePaiement());
            stmt.setInt(6, paiement.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Paiement #" + paiement.getId() + " modifié");
            } else {
                System.out.println("⚠️ Aucun paiement trouvé avec l'ID #" + paiement.getId());
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur modification paiement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la modification du paiement", e);
        }
    }

    // DELETE - Supprimer un paiement
    public void supprimer(int id) {
        String sql = "DELETE FROM Paiement WHERE id_paiement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Paiement #" + id + " supprimé");
            } else {
                System.out.println("⚠️ Aucun paiement trouvé avec l'ID #" + id);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur suppression paiement: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression du paiement", e);
        }
    }

    // SEARCH - Rechercher des paiements
    public List<Paiement> rechercher(String critere) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM Paiement WHERE methode_paiement LIKE ? OR date LIKE ? ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + critere + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Paiement p = new Paiement();
                p.setId(rs.getInt("id_paiement"));
                p.setMontant(rs.getBigDecimal("montant"));

                Date date = rs.getDate("date");
                if (date != null) {
                    p.setDate(date.toLocalDate());
                }

                p.setMembreId(rs.getInt("membre_id"));
                p.setAbonnementId(rs.getInt("abonnement_id"));
                p.setMethodePaiement(rs.getString("methode_paiement"));

                paiements.add(p);
            }

            System.out.println("✅ " + paiements.size() + " résultat(s) pour: '" + critere + "'");

        } catch (SQLException e) {
            System.err.println("❌ Erreur recherche: " + e.getMessage());
            e.printStackTrace();
        }

        return paiements;
    }

    // Filtrer les paiements par membre
    public List<Paiement> filtrerParMembre(int membreId) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM Paiement WHERE membre_id = ? ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, membreId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Paiement p = new Paiement();
                p.setId(rs.getInt("id_paiement"));
                p.setMontant(rs.getBigDecimal("montant"));

                Date date = rs.getDate("date");
                if (date != null) {
                    p.setDate(date.toLocalDate());
                }

                p.setMembreId(rs.getInt("membre_id"));
                p.setAbonnementId(rs.getInt("abonnement_id"));
                p.setMethodePaiement(rs.getString("methode_paiement"));

                paiements.add(p);
            }

            System.out.println("✅ " + paiements.size() + " paiement(s) trouvé(s) pour le membre #" + membreId);

        } catch (SQLException e) {
            System.err.println("❌ Erreur filtre membre: " + e.getMessage());
            e.printStackTrace();
        }

        return paiements;
    }

    // Calculer le total des revenus
    public BigDecimal getTotalRevenus() {
        String sql = "SELECT SUM(montant) as total FROM Paiement";
        BigDecimal total = BigDecimal.ZERO;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                BigDecimal result = rs.getBigDecimal("total");
                if (result != null) {
                    total = result;
                }
            }

            System.out.println("✅ Revenus totaux: " + total + " DH");

        } catch (SQLException e) {
            System.err.println("❌ Erreur calcul revenus: " + e.getMessage());
            e.printStackTrace();
        }

        return total;
    }
}