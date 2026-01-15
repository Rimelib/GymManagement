package com.salledesport.controller;

import com.salledesport.dao.AbonnementDAO;
import com.salledesport.dao.MembreDAO;
import com.salledesport.model.Abonnement;
import com.salledesport.model.Membre;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MembreController {

    // ============ COMPOSANTS FXML ============

    @FXML private TableView<Membre> membreTable;
    @FXML private TableColumn<Membre, Integer> idColumn;
    @FXML private TableColumn<Membre, String> nomColumn;
    @FXML private TableColumn<Membre, String> prenomColumn;
    @FXML private TableColumn<Membre, String> emailColumn;
    @FXML private TableColumn<Membre, String> telephoneColumn;
    @FXML private TableColumn<Membre, LocalDate> dateNaissanceColumn;
    @FXML private TableColumn<Membre, LocalDate> dateInscriptionColumn;
    @FXML private TableColumn<Membre, String> abonnementColumn;

    @FXML private TextField rechercheField;
    @FXML private Label totalMembresLabel;
    @FXML private Label selectionLabel;

    @FXML private VBox formulaireBox;
    @FXML private Label formulaireTitreLabel;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private ComboBox<Abonnement> abonnementComboBox;

    // ============ VARIABLES ============

    private MembreDAO membreDAO;
    private AbonnementDAO abonnementDAO;
    private ObservableList<Membre> membresData;
    private Membre membreEnCours;
    private boolean modeModification = false;

    // ============ INITIALISATION ============

    @FXML
    public void initialize() {
        System.out.println("🔧 Initialisation MembreController...");

        membreDAO = new MembreDAO();
        abonnementDAO = new AbonnementDAO();
        membresData = FXCollections.observableArrayList();

        configurerTableView();
        chargerMembres();
        chargerAbonnements();
        configurerListeners();

        System.out.println("✅ MembreController initialisé");
    }

    private void configurerTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        dateNaissanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        dateInscriptionColumn.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dateNaissanceColumn.setCellFactory(column -> new TableCell<Membre, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText((empty || date == null) ? null : formatter.format(date));
            }
        });

        dateInscriptionColumn.setCellFactory(column -> new TableCell<Membre, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText((empty || date == null) ? null : formatter.format(date));
            }
        });

        abonnementColumn.setCellValueFactory(cellData -> {
            int abonnementId = cellData.getValue().getAbonnementId();
            Abonnement abo = abonnementDAO.getById(abonnementId);
            return new SimpleStringProperty(abo != null ? abo.getType() : "N/A");
        });
    }

    private void configurerListeners() {
        membreTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectionLabel.setText("Sélectionné: " + newSelection.getNom() + " " + newSelection.getPrenom());
                    } else {
                        selectionLabel.setText("Aucune sélection");
                    }
                }
        );
    }

    // ============ CHARGEMENT DONNÉES ============

    private void chargerMembres() {
        try {
            List<Membre> membres = membreDAO.getAll();
            membresData.clear();
            membresData.addAll(membres);
            membreTable.setItems(membresData);
            totalMembresLabel.setText("Total: " + membres.size() + " membre(s)");
            System.out.println("✅ " + membres.size() + " membre(s) chargé(s)");
        } catch (Exception e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les membres: " + e.getMessage());
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

    // ============ ACTIONS BOUTONS ============

    @FXML
    private void handleNouveauMembre() {
        modeModification = false;
        membreEnCours = null;
        formulaireTitreLabel.setText("➕ Nouveau Membre");
        viderFormulaire();
        afficherFormulaire(true);
        System.out.println("📝 Formulaire nouveau membre affiché");
    }

    @FXML
    private void handleModifier() {
        Membre selection = membreTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un membre à modifier.");
            return;
        }

        modeModification = true;
        membreEnCours = selection;
        formulaireTitreLabel.setText("✏️ Modifier Membre #" + selection.getId());
        remplirFormulaire(selection);
        afficherFormulaire(true);
        System.out.println("✏️ Modification membre #" + selection.getId());
    }

    @FXML
    private void handleSupprimer() {
        Membre selection = membreTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un membre à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le membre : " + selection.getNom() + " " + selection.getPrenom() + " ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> resultat = confirmation.showAndWait();

        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            try {
                membreDAO.supprimer(selection.getId());
                chargerMembres();
                afficherSucces("Membre supprimé", "Le membre a été supprimé avec succès.");
                System.out.println("🗑️ Membre #" + selection.getId() + " supprimé");
            } catch (Exception e) {
                afficherErreur("Erreur de suppression", "Impossible de supprimer le membre: " + e.getMessage());
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
            if (modeModification) {
                membreEnCours.setNom(nomField.getText().trim());
                membreEnCours.setPrenom(prenomField.getText().trim());
                membreEnCours.setEmail(emailField.getText().trim());
                membreEnCours.setTelephone(telephoneField.getText().trim());
                membreEnCours.setDateNaissance(dateNaissancePicker.getValue());
                membreEnCours.setAbonnementId(abonnementComboBox.getValue().getId());
                membreDAO.modifier(membreEnCours);
                afficherSucces("Membre modifié", "Les modifications ont été enregistrées.");
                System.out.println("✅ Membre #" + membreEnCours.getId() + " modifié");
            } else {
                Membre nouveau = new Membre();
                nouveau.setNom(nomField.getText().trim());
                nouveau.setPrenom(prenomField.getText().trim());
                nouveau.setEmail(emailField.getText().trim());
                nouveau.setTelephone(telephoneField.getText().trim());
                nouveau.setDateNaissance(dateNaissancePicker.getValue());
                nouveau.setDateInscription(LocalDate.now());
                nouveau.setAbonnementId(abonnementComboBox.getValue().getId());

                membreDAO.ajouter(nouveau);
                afficherSucces("Membre ajouté", "Le nouveau membre a été ajouté avec succès.");
                System.out.println("✅ Nouveau membre ajouté");
            }

            chargerMembres();
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
            List<Membre> resultats = membreDAO.rechercher(critere);
            membresData.clear();
            membresData.addAll(resultats);
            membreTable.setItems(membresData);
            totalMembresLabel.setText("Résultats: " + resultats.size() + " membre(s)");
            System.out.println("🔍 Recherche: " + resultats.size() + " résultat(s)");
        } catch (Exception e) {
            afficherErreur("Erreur de recherche", "Erreur lors de la recherche: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReinitialiser() {
        rechercheField.clear();
        chargerMembres();
        System.out.println("🔄 Liste réinitialisée");
    }

// ============ GESTION FORMULAIRE ============

    private void afficherFormulaire(boolean visible) {
        formulaireBox.setVisible(visible);
        formulaireBox.setManaged(visible);
    }

    private void viderFormulaire() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
        dateNaissancePicker.setValue(null);
        abonnementComboBox.setValue(null);
        membreEnCours = null;
    }

    private void remplirFormulaire(Membre membre) {
        nomField.setText(membre.getNom());
        prenomField.setText(membre.getPrenom());
        emailField.setText(membre.getEmail());
        telephoneField.setText(membre.getTelephone());
        dateNaissancePicker.setValue(membre.getDateNaissance());

        for (Abonnement abo : abonnementComboBox.getItems()) {
            if (abo.getId() == membre.getAbonnementId()) {
                abonnementComboBox.setValue(abo);
                break;
            }
        }
    }

    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        if (nomField.getText().trim().isEmpty()) {
            erreurs.append("• Le nom est obligatoire\n");
        }

        if (prenomField.getText().trim().isEmpty()) {
            erreurs.append("• Le prénom est obligatoire\n");
        }

        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            erreurs.append("• L'email est obligatoire\n");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            erreurs.append("• L'email n'est pas valide\n");
        }

        String telephone = telephoneField.getText().trim();
        if (telephone.isEmpty()) {
            erreurs.append("• Le téléphone est obligatoire\n");
        } else if (!telephone.matches("^0[5-7][0-9]{8}$")) {
            erreurs.append("• Le téléphone doit être au format marocain (06/07/05 + 8 chiffres)\n");
        }

        if (dateNaissancePicker.getValue() == null) {
            erreurs.append("• La date de naissance est obligatoire\n");
        } else {
            LocalDate dateNaissance = dateNaissancePicker.getValue();
            if (dateNaissance.isAfter(LocalDate.now().minusYears(16))) {
                erreurs.append("• Le membre doit avoir au moins 16 ans\n");
            }
        }

        if (abonnementComboBox.getValue() == null) {
            erreurs.append("• Veuillez sélectionner un abonnement\n");
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