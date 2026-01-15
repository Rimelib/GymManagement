package com.salledesport.controller;

import com.salledesport.dao.CoachDAO;
import com.salledesport.model.Coach;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class CoachController {

    // ============ COMPOSANTS FXML ============

    @FXML private TableView<Coach> coachTable;
    @FXML private TableColumn<Coach, Integer> idColumn;
    @FXML private TableColumn<Coach, String> nomColumn;
    @FXML private TableColumn<Coach, String> prenomColumn;
    @FXML private TableColumn<Coach, String> specialiteColumn;
    @FXML private TableColumn<Coach, String> telephoneColumn;
    @FXML private TableColumn<Coach, String> emailColumn;

    @FXML private TextField rechercheField;
    @FXML private Label totalCoachsLabel;
    @FXML private Label selectionLabel;

    @FXML private VBox formulaireBox;
    @FXML private Label formulaireTitreLabel;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField specialiteField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;

    // ============ VARIABLES ============

    private CoachDAO coachDAO;
    private ObservableList<Coach> coachsData;
    private Coach coachEnCours;
    private boolean modeModification = false;

    // ============ INITIALISATION ============

    @FXML
    public void initialize() {
        System.out.println("🔧 Initialisation CoachController...");

        coachDAO = new CoachDAO();
        coachsData = FXCollections.observableArrayList();

        configurerTableView();
        chargerCoachs();
        configurerListeners();

        System.out.println("✅ CoachController initialisé");
    }

    private void configurerTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        specialiteColumn.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void configurerListeners() {
        coachTable.getSelectionModel().selectedItemProperty().addListener(
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

    private void chargerCoachs() {
        try {
            List<Coach> coachs = coachDAO.getAll();
            coachsData.clear();
            coachsData.addAll(coachs);
            coachTable.setItems(coachsData);
            totalCoachsLabel.setText("Total: " + coachs.size() + " coach(s)");
            System.out.println("✅ " + coachs.size() + " coach(s) chargé(s)");
        } catch (Exception e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les coachs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============ ACTIONS BOUTONS ============

    @FXML
    private void handleNouveauCoach() {
        modeModification = false;
        coachEnCours = null;
        formulaireTitreLabel.setText("➕ Nouveau Coach");
        viderFormulaire();
        afficherFormulaire(true);
        System.out.println("📝 Formulaire nouveau coach affiché");
    }

    @FXML
    private void handleModifier() {
        Coach selection = coachTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un coach à modifier.");
            return;
        }

        modeModification = true;
        coachEnCours = selection;
        formulaireTitreLabel.setText("✏️ Modifier Coach #" + selection.getId());
        remplirFormulaire(selection);
        afficherFormulaire(true);
        System.out.println("✏️ Modification coach #" + selection.getId());
    }

    @FXML
    private void handleSupprimer() {
        Coach selection = coachTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un coach à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le coach : " + selection.getNom() + " " + selection.getPrenom() + " ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> resultat = confirmation.showAndWait();

        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            try {
                coachDAO.supprimer(selection.getId());
                chargerCoachs();
                afficherSucces("Coach supprimé", "Le coach a été supprimé avec succès.");
                System.out.println("🗑️ Coach #" + selection.getId() + " supprimé");
            } catch (Exception e) {
                afficherErreur("Erreur de suppression", "Impossible de supprimer le coach: " + e.getMessage());
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
                coachEnCours.setNom(nomField.getText().trim());
                coachEnCours.setPrenom(prenomField.getText().trim());
                coachEnCours.setSpecialite(specialiteField.getText().trim());
                coachEnCours.setEmail(emailField.getText().trim());
                coachEnCours.setTelephone(telephoneField.getText().trim());

                coachDAO.modifier(coachEnCours);
                afficherSucces("Coach modifié", "Les modifications ont été enregistrées.");
                System.out.println("✅ Coach #" + coachEnCours.getId() + " modifié");
            } else {
                Coach nouveau = new Coach();
                nouveau.setNom(nomField.getText().trim());
                nouveau.setPrenom(prenomField.getText().trim());
                nouveau.setSpecialite(specialiteField.getText().trim());
                nouveau.setEmail(emailField.getText().trim());
                nouveau.setTelephone(telephoneField.getText().trim());

                coachDAO.ajouter(nouveau);
                afficherSucces("Coach ajouté", "Le nouveau coach a été ajouté avec succès.");
                System.out.println("✅ Nouveau coach ajouté");
            }

            chargerCoachs();
            afficherFormulaire(false);
            viderFormulaire();

        } catch (Exception e) {
            afficherErreur("Erreur d'enregistrement", "Impossible d'enregistrer: " + e.getMessage());e.printStackTrace();
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
            List<Coach> resultats = coachDAO.rechercher(critere);
            coachsData.clear();
            coachsData.addAll(resultats);
            coachTable.setItems(coachsData);
            totalCoachsLabel.setText("Résultats: " + resultats.size() + " coach(s)");
            System.out.println("🔍 Recherche: " + resultats.size() + " résultat(s)");
        } catch (Exception e) {
            afficherErreur("Erreur de recherche", "Erreur lors de la recherche: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReinitialiser() {
        rechercheField.clear();
        chargerCoachs();
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
        specialiteField.clear();
        emailField.clear();
        telephoneField.clear();
        coachEnCours = null;
    }

    private void remplirFormulaire(Coach coach) {
        nomField.setText(coach.getNom());
        prenomField.setText(coach.getPrenom());
        specialiteField.setText(coach.getSpecialite());
        emailField.setText(coach.getEmail());
        telephoneField.setText(coach.getTelephone());
    }

    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        if (nomField.getText().trim().isEmpty()) {
            erreurs.append("• Le nom est obligatoire\n");
        }

        if (prenomField.getText().trim().isEmpty()) {
            erreurs.append("• Le prénom est obligatoire\n");
        }

        if (specialiteField.getText().trim().isEmpty()) {
            erreurs.append("• La spécialité est obligatoire\n");
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