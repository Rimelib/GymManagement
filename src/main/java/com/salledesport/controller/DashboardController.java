package com.salledesport.controller;

import com.salledesport.dao.*;
import com.salledesport.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.salledesport.dao.*;
import com.salledesport.model.*;
import javafx.scene.control.*;

public class DashboardController {

    // ============ LABELS STATISTIQUES ============

    @FXML private Label dateLabel;
    @FXML private Label totalMembresLabel;
    @FXML private Label seancesAujourdhuiLabel;
    @FXML private Label revenusTotauxLabel;
    @FXML private Label totalCoachsLabel;
    @FXML private Label totalAbonnementsLabel;
    @FXML private Label totalPaiementsLabel;
    @FXML private Label lastUpdateLabel;

    // ============ TABLEVIEWS ============

    @FXML private TableView<Membre> membresRecentsTable;
    @FXML private TableColumn<Membre, String> membreNomColumn;
    @FXML private TableColumn<Membre, String> membrePrenomColumn;
    @FXML private TableColumn<Membre, String> membreEmailColumn;
    @FXML private TableColumn<Membre, LocalDate> membreDateColumn;

    @FXML private TableView<SeanceInfo> seancesDuJourTable;
    @FXML private TableColumn<SeanceInfo, String> seanceHeureColumn;
    @FXML private TableColumn<SeanceInfo, String> seanceTypeColumn;
    @FXML private TableColumn<SeanceInfo, String> seanceCoachColumn;
    @FXML private TableColumn<SeanceInfo, String> seanceMembreColumn;

    // ============ DAO ============

    private MembreDAO membreDAO;
    private AbonnementDAO abonnementDAO;
    private CoachDAO coachDAO;
    private SeanceDAO seanceDAO;
    private PaiementDAO paiementDAO;

    // ============ INITIALISATION ============

    @FXML
    public void initialize() {
        System.out.println("🔧 Initialisation DashboardController...");

        // Initialiser les DAO
        membreDAO = new MembreDAO();
        abonnementDAO = new AbonnementDAO();
        coachDAO = new CoachDAO();
        seanceDAO = new SeanceDAO();
        paiementDAO = new PaiementDAO();

        // Configurer les tables
        configurerTableMembresRecents();
        configurerTableSeancesDuJour();

        // Afficher la date
        afficherDate();

        // Charger toutes les statistiques
        chargerToutesLesStatistiques();

        System.out.println("✅ DashboardController initialisé");
    }

    // ============ CONFIGURATION TABLES ============

    private void configurerTableMembresRecents() {
        membreNomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        membrePrenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        membreEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        membreDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));

        // Formater la date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        membreDateColumn.setCellFactory(column -> new TableCell<Membre, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText((empty || date == null) ? null : formatter.format(date));
            }
        });
    }

    private void configurerTableSeancesDuJour() {
        seanceHeureColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));
        seanceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        seanceCoachColumn.setCellValueFactory(new PropertyValueFactory<>("coachNom"));
        seanceMembreColumn.setCellValueFactory(new PropertyValueFactory<>("membreNom"));
    }

    // ============ CHARGEMENT STATISTIQUES ============

    private void chargerToutesLesStatistiques() {
        try {
            chargerStatistiquesMembres();
            chargerStatistiquesSeances();
            chargerStatistiquesRevenus();
            chargerStatistiquesCoachs();
            chargerStatistiquesAbonnements();
            chargerStatistiquesPaiements();
            chargerMembresRecents();
            chargerSeancesDuJour();

            // Mettre à jour l'heure
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            lastUpdateLabel.setText("Dernière mise à jour : " + LocalDateTime.now().format(formatter));

            System.out.println("✅ Toutes les statistiques chargées");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement des statistiques: " + e.getMessage());
            e.printStackTrace();
            afficherErreur("Erreur", "Impossible de charger certaines statistiques: " + e.getMessage());
        }
    }

    private void chargerStatistiquesMembres() {
        try {
            List<Membre> membres = membreDAO.getAll();
            totalMembresLabel.setText(String.valueOf(membres.size()));
        } catch (Exception e) {
            totalMembresLabel.setText("--");
            System.err.println("Erreur chargement membres: " + e.getMessage());
        }
    }

    private void chargerStatistiquesSeances() {
        try {
            List<Seance> seancesAujourdhui = seanceDAO.getSeancesAujourdhui();
            seancesAujourdhuiLabel.setText(String.valueOf(seancesAujourdhui.size()));
        } catch (Exception e) {
            seancesAujourdhuiLabel.setText("--");
            System.err.println("Erreur chargement séances: " + e.getMessage());
        }
    }

    private void chargerStatistiquesRevenus() {
        try {
            // ✅ CORRIGÉ : utilise getTotalRevenus() et BigDecimal
            BigDecimal revenus = paiementDAO.getTotalRevenus();
            revenusTotauxLabel.setText(String.format("%.2f DH", revenus.doubleValue()));
        } catch (Exception e) {
            revenusTotauxLabel.setText("-- DH");
            System.err.println("Erreur chargement revenus: " + e.getMessage());
        }
    }

    private void chargerStatistiquesCoachs() {
        try {
            List<Coach> coachs = coachDAO.getAll();
            totalCoachsLabel.setText(String.valueOf(coachs.size()));
        } catch (Exception e) {
            totalCoachsLabel.setText("--");
            System.err.println("Erreur chargement coachs: " + e.getMessage());
        }
    }

    private void chargerStatistiquesAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementDAO.getAll();
            totalAbonnementsLabel.setText(String.valueOf(abonnements.size()));
        } catch (Exception e) {
            totalAbonnementsLabel.setText("--");
            System.err.println("Erreur chargement abonnements: " + e.getMessage());
        }
    }

    private void chargerStatistiquesPaiements() {
        try {
            List<Paiement> paiements = paiementDAO.getAll();
            totalPaiementsLabel.setText(String.valueOf(paiements.size()));
        } catch (Exception e) {
            totalPaiementsLabel.setText("--");
            System.err.println("Erreur chargement paiements: " + e.getMessage());
        }
    }

    private void chargerMembresRecents() {
        try {
            List<Membre> tousMembres = membreDAO.getAll();

            // Trier par date d'inscription (les plus récents en premier)
            List<Membre> membresRecents = tousMembres.stream()
                    .sorted((m1, m2) -> {
                        if (m1.getDateInscription() == null) return 1;
                        if (m2.getDateInscription() == null) return -1;
                        return m2.getDateInscription().compareTo(m1.getDateInscription());
                    })
                    .limit(5) // Les 5 plus récents
                    .collect(Collectors.toList());

            ObservableList<Membre> data = FXCollections.observableArrayList(membresRecents);
            membresRecentsTable.setItems(data);

        } catch (Exception e) {
            System.err.println("Erreur chargement membres récents: " + e.getMessage());
        }
    }

    private void chargerSeancesDuJour() {
        try {
            List<Seance> seances = seanceDAO.getSeancesAujourdhui();

            ObservableList<SeanceInfo> seancesInfo = FXCollections.observableArrayList();

            for (Seance seance : seances) {
                SeanceInfo info = new SeanceInfo();

                // Formater l'heure
                if (seance.getHeure() != null) {
                    info.setHeure(seance.getHeure().format(DateTimeFormatter.ofPattern("HH:mm")));
                } else {
                    info.setHeure("--:--");
                }

                info.setType(seance.getType() != null ? seance.getType() : "Non défini");

                // Récupérer le nom du coach
                try {
                    Coach coach = coachDAO.getById(seance.getCoachId());
                    info.setCoachNom(coach != null ? coach.getNom() + " " + coach.getPrenom() : "N/A");
                } catch (Exception e) {
                    info.setCoachNom("N/A");
                }

                // Récupérer le nom du membre
                try {
                    Membre membre = membreDAO.getById(seance.getMembreId());
                    info.setMembreNom(membre != null ? membre.getNom() + " " + membre.getPrenom() : "N/A");
                } catch (Exception e) {
                    info.setMembreNom("N/A");
                }

                seancesInfo.add(info);
            }

            // Trier par heure
            seancesInfo.sort((s1, s2) -> s1.getHeure().compareTo(s2.getHeure()));

            seancesDuJourTable.setItems(seancesInfo);

        } catch (Exception e) {
            System.err.println("Erreur chargement séances du jour: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============ AFFICHAGE DATE ============

    private void afficherDate() {
        LocalDate aujourdhui = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", java.util.Locale.FRENCH);
        dateLabel.setText(aujourdhui.format(formatter));
    }

    // ============ ACTIONS ============

    @FXML
    private void handleActualiser() {
        System.out.println("🔄 Actualisation des données...");
        chargerToutesLesStatistiques();
        afficherSucces("Actualisation", "Les données ont été actualisées avec succès !");
    }

    // ============ MESSAGES ============

    private void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ============ CLASSE INTERNE ============

    /**
     * Classe pour afficher les informations complètes d'une séance
     */
    public static class SeanceInfo {
        private String heure;
        private String type;
        private String coachNom;
        private String membreNom;

        public SeanceInfo() {}

        public String getHeure() { return heure; }
        public void setHeure(String heure) { this.heure = heure; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getCoachNom() { return coachNom; }
        public void setCoachNom(String coachNom) { this.coachNom = coachNom; }

        public String getMembreNom() { return membreNom; }
        public void setMembreNom(String membreNom) { this.membreNom = membreNom; }
    }
}