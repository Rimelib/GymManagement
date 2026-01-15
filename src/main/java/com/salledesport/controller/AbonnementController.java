package com.salledesport.controller;

import com.salledesport.dao.AbonnementDAO;
import com.salledesport.model.Abonnement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class AbonnementController {

    // ============ COMPOSANTS FXML ============

    @FXML private TableView<Abonnement> abonnementTable;
    @FXML private TableColumn<Abonnement, Integer> idColumn;
    @FXML private TableColumn<Abonnement, String> typeColumn;
    @FXML private TableColumn<Abonnement, Double> prixColumn;
    @FXML private TableColumn<Abonnement, Integer> dureeColumn;
    @FXML private TableColumn<Abonnement, String> descriptionColumn;

    @FXML private Label totalAbonnementsLabel;
    @FXML private Label selectionLabel;

    @FXML private VBox formulaireBox;
    @FXML private Label formulaireTitreLabel;
    @FXML private TextField typeField;
    @FXML private TextField prixField;
    @FXML private TextField dureeField;
    @FXML private TextArea descriptionArea;

    // ============ VARIABLES ============

    private AbonnementDAO abonnementDAO;
    private ObservableList<Abonnement> abonnementsData;
    private Abonnement abonnementEnCours;
    private boolean modeModification = false;

    // ============ INITIALISATION ============

    @FXML
    public void initialize() {
        System.out.println("🔧 Initialisation AbonnementController...");

        abonnementDAO = new AbonnementDAO();
        abonnementsData = FXCollections.observableArrayList();

        configurerTableView();
        chargerAbonnements();
        configurerListeners();

        System.out.println("✅ AbonnementController initialisé");
    }

    private void configurerTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("duree"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Formater le prix
        prixColumn.setCellFactory(column -> new TableCell<Abonnement, Double>() {
            @Override
            protected void updateItem(Double prix, boolean empty) {
                super.updateItem(prix, empty);
                if (empty || prix == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f MAD", prix));
                }
            }
        });

        // Formater la durée
        dureeColumn.setCellFactory(column -> new TableCell<Abonnement, Integer>() {
            @Override
            protected void updateItem(Integer duree, boolean empty) {
                super.updateItem(duree, empty);
                if (empty || duree == null) {
                    setText(null);
                } else {
                    setText(duree + " mois");
                }
            }
        });
    }

    private void configurerListeners() {
        abonnementTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectionLabel.setText("Sélectionné: " + newSelection.getType());
                    } else {
                        selectionLabel.setText("Aucune sélection");
                    }
                }
        );
    }

    // ============ CHARGEMENT DONNÉES ============

    private void chargerAbonnements() {
        try {
            List<Abonnement> abonnements = abonnementDAO.getAll();
            abonnementsData.clear();
            abonnementsData.addAll(abonnements);
            abonnementTable.setItems(abonnementsData);
            totalAbonnementsLabel.setText("Total: " + abonnements.size() + " abonnement(s)");
            System.out.println("✅ " + abonnements.size() + " abonnement(s) chargé(s)");
        } catch (Exception e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les abonnements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============ ACTIONS BOUTONS ============

    @FXML
    private void handleNouvelAbonnement() {
        modeModification = false;
        abonnementEnCours = null;
        formulaireTitreLabel.setText("➕ Nouvel Abonnement");
        viderFormulaire();
        afficherFormulaire(true);
        System.out.println("📝 Formulaire nouvel abonnement affiché");
    }

    @FXML
    private void handleModifier() {
        Abonnement selection = abonnementTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un abonnement à modifier.");
            return;
        }

        modeModification = true;
        abonnementEnCours = selection;
        formulaireTitreLabel.setText("✏️ Modifier Abonnement #" + selection.getId());
        remplirFormulaire(selection);
        afficherFormulaire(true);
        System.out.println("✏️ Modification abonnement #" + selection.getId());
    }

    @FXML
    private void handleSupprimer() {
        Abonnement selection = abonnementTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner un abonnement à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer l'abonnement : " + selection.getType() + " ?");
        confirmation.setContentText("Cette action est irréversible.\nAttention: Les membres ayant cet abonnement seront affectés.");

        Optional<ButtonType> resultat = confirmation.showAndWait();

        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            try {
                abonnementDAO.supprimer(selection.getId());
                chargerAbonnements();
                afficherSucces("Abonnement supprimé", "L'abonnement a été supprimé avec succès.");
                System.out.println("🗑️ Abonnement #" + selection.getId() + " supprimé");
            } catch (Exception e) {
                afficherErreur("Erreur de suppression", "Impossible de supprimer l'abonnement: " + e.getMessage());
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
                abonnementEnCours.setType(typeField.getText().trim());
                abonnementEnCours.setPrix(Double.parseDouble(prixField.getText().trim()));
                abonnementEnCours.setDuree(Integer.parseInt(dureeField.getText().trim()));
                abonnementEnCours.setDescription(descriptionArea.getText().trim());

                abonnementDAO.modifier(abonnementEnCours);
                afficherSucces("Abonnement modifié", "Les modifications ont été enregistrées.");
                System.out.println("✅ Abonnement #" + abonnementEnCours.getId() + " modifié");
            } else {
                Abonnement nouveau = new Abonnement();
                nouveau.setType(typeField.getText().trim());
                nouveau.setPrix(Double.parseDouble(prixField.getText().trim()));
                nouveau.setDuree(Integer.parseInt(dureeField.getText().trim()));
                nouveau.setDescription(descriptionArea.getText().trim());

                abonnementDAO.ajouter(nouveau);
                afficherSucces("Abonnement ajouté", "Le nouvel abonnement a été ajouté avec succès.");
                System.out.println("✅ Nouvel abonnement ajouté");
            }

            chargerAbonnements();
            afficherFormulaire(false);
            viderFormulaire();

        } catch (NumberFormatException e) {
            afficherErreur("Erreur de format", "Le prix et la durée doivent être des nombres valides.");
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

    // ============ GESTION FORMULAIRE ============

    private void afficherFormulaire(boolean visible) {
        formulaireBox.setVisible(visible);
        formulaireBox.setManaged(visible);
    }

    private void viderFormulaire() {
        typeField.clear();
        prixField.clear();
        dureeField.clear();
        descriptionArea.clear();
        abonnementEnCours = null;
    }

    private void remplirFormulaire(Abonnement abonnement) {
        typeField.setText(abonnement.getType());
        prixField.setText(String.valueOf(abonnement.getPrix()));
        dureeField.setText(String.valueOf(abonnement.getDuree()));
        descriptionArea.setText(abonnement.getDescription());
    }

    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        if (typeField.getText().trim().isEmpty()) {
            erreurs.append("• Le type d'abonnement est obligatoire\n");
        }

        String prixText = prixField.getText().trim();
        if (prixText.isEmpty()) {
            erreurs.append("• Le prix est obligatoire\n");
        } else {
            try {
                double prix = Double.parseDouble(prixText);
                if (prix <= 0) {
                    erreurs.append("• Le prix doit être supérieur à 0\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("• Le prix doit être un nombre valide\n");
            }
        }

        String dureeText = dureeField.getText().trim();
        if (dureeText.isEmpty()) {
            erreurs.append("• La durée est obligatoire\n");
        } else {
            try {
                int duree = Integer.parseInt(dureeText);
                if (duree <= 0) {
                    erreurs.append("• La durée doit être supérieure à 0\n");
                }
            } catch (NumberFormatException e) {
                erreurs.append("• La durée doit être un nombre entier valide\n");
            }
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