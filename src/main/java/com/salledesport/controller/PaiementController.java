package com.salledesport.controller;

import com.salledesport.dao.AbonnementDAO;
import com.salledesport.dao.MembreDAO;
import com.salledesport.dao.PaiementDAO;
import com.salledesport.model.Abonnement;
import com.salledesport.model.Membre;
import com.salledesport.model.Paiement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PaiementController {

    // ============ COMPOSANTS FXML ============

    @FXML private TableView<Paiement> paiementTable;
    @FXML private TableColumn<Paiement, Integer> idColumn;
    @FXML private TableColumn<Paiement, BigDecimal> montantColumn;
    @FXML private TableColumn<Paiement, LocalDate> dateColumn;
    @FXML private TableColumn<Paiement, String> membreColumn;
    @FXML private TableColumn<Paiement, String> abonnementColumn;
    @FXML private TableColumn<Paiement, String> methodeColumn;

    @FXML private TextField rechercheField;
    @FXML private Label totalPaiementsLabel;
    @FXML private Label totalRevenusLabel;
    @FXML private Label selectionLabel;

    @FXML private VBox formulaireBox;
    @FXML private Label formulaireTitreLabel;
    @FXML private TextField montantField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Membre> membreComboBox;
    @FXML private ComboBox<Abonnement> abonnementComboBox;
    @FXML private ComboBox<String> methodeComboBox;

    // ============ VARIABLES ============

    private PaiementDAO paiementDAO;
    private MembreDAO membreDAO;
    private AbonnementDAO abonnementDAO;
    private ObservableList<Paiement> paiementsData;
    private Paiement paiementEnCours;
    private boolean modeModification = false;

    // ============ INITIALISATION ============

    @FXML
    public void initialize() {
        System.out.println("🔧 Initialisation PaiementController...");

        paiementDAO = new PaiementDAO();
        membreDAO = new MembreDAO();
        abonnementDAO = new AbonnementDAO();
        paiementsData = FXCollections.observableArrayList();

        configurerTableView();
        chargerPaiements();
        chargerMembres();
        chargerAbonnements();
        chargerMethodesPaiement();
        configurerListeners();
        calculerRevenus();

        System.out.println("✅ PaiementController initialisé");
    }

    private void configurerTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        methodeColumn.setCellValueFactory(new PropertyValueFactory<>("methodePaiement"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateColumn.setCellFactory(column -> new TableCell<Paiement, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText((empty || date == null) ? null : formatter.format(date));
            }
        });

        montantColumn.setCellFactory(column -> new TableCell<Paiement, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal montant, boolean empty) {
                super.updateItem(montant, empty);
                setText((empty || montant == null) ? null : montant + " DH");
            }
        });

        membreColumn.setCellValueFactory(cellData -> {
            int membreId = cellData.getValue().getMembreId();
            Membre membre = membreDAO.getById(membreId);
            return new SimpleStringProperty(membre != null ? membre.getNom() + " " + membre.getPrenom() : "N/A");
        });

        abonnementColumn.setCellValueFactory(cellData -> {
            int abonnementId = cellData.getValue().getAbonnementId();
            Abonnement abo = abonnementDAO.getById(abonnementId);
            return new SimpleStringProperty(abo != null ? abo.getType() : "N/A");
        });
    }

    private void configurerListeners() {
        paiementTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectionLabel.setText("Sélectionné: " + newSelection.getMontant() + " DH - " + newSelection.getDate());
                    } else {
                        selectionLabel.setText("Aucune sélection");
                    }
                }
        );

        membreComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !modeModification) {
                Membre membre = newVal;
                if (membre.getAbonnementId() > 0) {
                    Abonnement abo = abonnementDAO.getById(membre.getAbonnementId());
                    if (abo != null) {
                        abonnementComboBox.setValue(abo);
                        montantField.setText(String.valueOf(abo.getPrix()));
                    }
                }
            }
        });
    }

    // ============ CHARGEMENT DONNÉES ============

    private void chargerPaiements() {
        try {
            List<Paiement> paiements = paiementDAO.getAll();
            paiementsData.clear();
            paiementsData.addAll(paiements);
            paiementTable.setItems(paiementsData);
            totalPaiementsLabel.setText("Total: " + paiements.size() + " paiement(s)");
            System.out.println("✅ " + paiements.size() + " paiement(s) chargé(s)");
        } catch (Exception e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les paiements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void chargerMembres() {
        try {
            List<Membre> membres = membreDAO.getAll();
            ObservableList<Membre> membreData = FXCollections.observableArrayList(membres);
            membreComboBox.setItems(membreData);
            System.out.println("✅ " + membres.size() + " membre(s) chargé(s)");
        } catch (Exception e) {
            afficherErreur("Erreur", "Impossible de charger les membres: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void chargerAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementDAO.getAll();
            ObservableList<Abonnement> aboData = FXCollections.observableArrayList(abonnements);
            abonnementComboBox.setItems(aboData);
            System.out.println("✅ " + abonnements.size() + " abonnement(s) chargé(s)");
        } catch (Exception e) {
            afficherErreur("Erreur", "Impossible de charger les abonnements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void chargerMethodesPaiement() {
        ObservableList<String> methodes = FXCollections.observableArrayList(
                "Espèces", "Carte Bancaire", "Virement", "Chèque", "PayPal"
        );
        methodeComboBox.setItems(methodes);
    }

    private void calculerRevenus() {
        try {
            BigDecimal total = paiementDAO.getTotalRevenus();
            totalRevenusLabel.setText("Revenus totaux: " + total + " DH");
            System.out.println("💰 Revenus totaux: " + total + " DH");
        } catch (Exception e) {
            System.err.println("❌ Erreur calcul revenus: " + e.getMessage());
        }
    }

    // ============ ACTIONS BOUTONS ============

    @FXML
    private void handleNouveauPaiement() {
        modeModification = false;
        paiementEnCours = null;
        formulaireTitreLabel.setText("➕ Nouveau Paiement");
        viderFormulaire();
        afficherFormulaire(true);
        System.out.println("📝 Formulaire nouveau paiement affiché");
    }

    @FXML
    private void handleModifier() {
        Paiement selection = paiementTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un paiement à modifier.");
            return;
        }

        modeModification = true;
        paiementEnCours = selection;
        formulaireTitreLabel.setText("✏️ Modifier Paiement #" + selection.getId());
        remplirFormulaire(selection);
        afficherFormulaire(true);
        System.out.println("✏️ Modification paiement #" + selection.getId());
    }

    @FXML
    private void handleSupprimer() {
        Paiement selection = paiementTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un paiement à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le paiement de " + selection.getMontant() + " DH ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> resultat = confirmation.showAndWait();

        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            try {
                paiementDAO.supprimer(selection.getId());
                chargerPaiements();
                calculerRevenus();
                afficherSucces("Paiement supprimé", "Le paiement a été supprimé avec succès.");
                System.out.println("🗑️ Paiement #" + selection.getId() + " supprimé");
            } catch (Exception e) {
                afficherErreur("Erreur de suppression", "Impossible de supprimer le paiement: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEnregistrer() {
        if (!validerFormulaire()) {
            return;
        }

        try {
            BigDecimal montant = new BigDecimal(montantField.getText().trim());

            if (modeModification) {
                paiementEnCours.setMontant(montant);
                paiementEnCours.setDate(datePicker.getValue());
                paiementEnCours.setMembreId(membreComboBox.getValue().getId());
                paiementEnCours.setAbonnementId(abonnementComboBox.getValue().getId());
                paiementEnCours.setMethodePaiement(methodeComboBox.getValue());
                paiementDAO.modifier(paiementEnCours);
                afficherSucces("Paiement modifié", "Les modifications ont été enregistrées.");
                System.out.println("✅ Paiement #" + paiementEnCours.getId() + " modifié");
            } else {
                Paiement nouveau = new Paiement();
                nouveau.setMontant(montant);
                nouveau.setDate(datePicker.getValue());
                nouveau.setMembreId(membreComboBox.getValue().getId());
                nouveau.setAbonnementId(abonnementComboBox.getValue().getId());
                nouveau.setMethodePaiement(methodeComboBox.getValue());

                paiementDAO.ajouter(nouveau);
                afficherSucces("Paiement ajouté", "Le nouveau paiement a été ajouté avec succès.");
                System.out.println("✅ Nouveau paiement ajouté");
            }

            chargerPaiements();
            calculerRevenus();
            afficherFormulaire(false);
            viderFormulaire();

        } catch (Exception e) {
            afficherErreur("Erreur d'enregistrement", "Impossible d'enregistrer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnnuler() {
        afficherFormulaire(false);
        viderFormulaire();
        System.out.println("❌ Formulaire annulé");
    }

    @FXML
    private void handleRechercher() {
        String critere = rechercheField.getText().trim();

        if (critere.isEmpty()) {
            afficherAvertissement("Recherche vide", "Veuillez entrer un critère de recherche.");
            return;
        }

        try {
            List<Paiement> resultats = paiementDAO.rechercher(critere);
            paiementsData.clear();
            paiementsData.addAll(resultats);
            paiementTable.setItems(paiementsData);
            totalPaiementsLabel.setText("Résultats: " + resultats.size() + " paiement(s)");
            System.out.println("🔍 Recherche: " + resultats.size() + " résultat(s)");
        } catch (Exception e) {
            afficherErreur("Erreur de recherche", "Erreur lors de la recherche: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReinitialiser() {
        rechercheField.clear();
        chargerPaiements();
        System.out.println("🔄 Liste réinitialisée");
    }

    // ============ GESTION FORMULAIRE ============

    private void afficherFormulaire(boolean visible) {
        formulaireBox.setVisible(visible);
        formulaireBox.setManaged(visible);
    }

    private void viderFormulaire() {
        montantField.clear();
        datePicker.setValue(LocalDate.now());
        membreComboBox.setValue(null);
        abonnementComboBox.setValue(null);
        methodeComboBox.setValue(null);
        paiementEnCours = null;
    }

    private void remplirFormulaire(Paiement paiement) {
        montantField.setText(paiement.getMontant().toString());
        datePicker.setValue(paiement.getDate());
        methodeComboBox.setValue(paiement.getMethodePaiement());

        for (Membre membre : membreComboBox.getItems()) {
            if (membre.getId() == paiement.getMembreId()) {
                membreComboBox.setValue(membre);
                break;
            }
        }

        for (Abonnement abo : abonnementComboBox.getItems()) {
            if (abo.getId() == paiement.getAbonnementId()) {
                abonnementComboBox.setValue(abo);
                break;
            }
        }
    }

    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        String montantStr = montantField.getText().trim();
        if (montantStr.isEmpty()) {
            erreurs.append("• Le montant est obligatoire\n");
        } else {
            try {
                BigDecimal montant = new BigDecimal(montantStr);
                if (montant.compareTo(BigDecimal.ZERO) <= 0) {
                    erreurs.append("• Le montant doit être supérieur à 0\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("• Le montant doit être un nombre valide\n");
            }
        }

        if (datePicker.getValue() == null) {
            erreurs.append("• La date est obligatoire\n");
        }

        if (membreComboBox.getValue() == null) {
            erreurs.append("• Veuillez sélectionner un membre\n");
        }

        if (abonnementComboBox.getValue() == null) {
            erreurs.append("• Veuillez sélectionner un abonnement\n");
        }

        if (methodeComboBox.getValue() == null) {
            erreurs.append("• Veuillez sélectionner une méthode de paiement\n");
        }

        if (erreurs.length() > 0) {
            afficherAvertissement("Formulaire incomplet", erreurs.toString());
            return false;
        }

        return true;
    }

    // ============ MESSAGES UTILISATEUR ============

    private void afficherSucces(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherAvertissement(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
}