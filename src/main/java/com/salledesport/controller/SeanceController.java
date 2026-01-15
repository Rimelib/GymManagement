package com.salledesport.controller;

import com.salledesport.dao.CoachDAO;
import com.salledesport.dao.MembreDAO;
import com.salledesport.dao.SeanceDAO;
import com.salledesport.model.Coach;
import com.salledesport.model.Membre;
import com.salledesport.model.Seance;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class SeanceController {

    // ============ COMPOSANTS FXML ============

    @FXML private TableView<Seance> seanceTable;
    @FXML private TableColumn<Seance, Integer> idColumn;
    @FXML private TableColumn<Seance, LocalDate> dateColumn;
    @FXML private TableColumn<Seance, LocalTime> heureColumn;
    @FXML private TableColumn<Seance, String> typeColumn;
    @FXML private TableColumn<Seance, String> coachColumn;
    @FXML private TableColumn<Seance, String> membreColumn;

    @FXML private TextField rechercheField;
    @FXML private Label totalSeancesLabel;
    @FXML private Label selectionLabel;

    @FXML private VBox formulaireBox;
    @FXML private Label formulaireTitreLabel;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> heureSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<Coach> coachComboBox;
    @FXML private ComboBox<Membre> membreComboBox;

    // ============ VARIABLES ============

    private SeanceDAO seanceDAO;
    private CoachDAO coachDAO;
    private MembreDAO membreDAO;
    private ObservableList<Seance> seancesData;
    private Seance seanceEnCours;
    private boolean modeModification = false;

    // ============ INITIALISATION ============

    @FXML
    public void initialize() {
        System.out.println("🔧 Initialisation SeanceController...");

        seanceDAO = new SeanceDAO();
        coachDAO = new CoachDAO();
        membreDAO = new MembreDAO();
        seancesData = FXCollections.observableArrayList();

        configurerTableView();
        configurerSpinners();
        chargerSeances();
        chargerCoachs();
        chargerMembres();
        chargerTypesSeance();
        configurerListeners();

        System.out.println("✅ SeanceController initialisé");
    }

    private void configurerTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateColumn.setCellFactory(column -> new TableCell<Seance, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText((empty || date == null) ? null : dateFormatter.format(date));
            }
        });

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        heureColumn.setCellFactory(column -> new TableCell<Seance, LocalTime>() {
            @Override
            protected void updateItem(LocalTime heure, boolean empty) {
                super.updateItem(heure, empty);
                setText((empty || heure == null) ? null : timeFormatter.format(heure));
            }
        });

        coachColumn.setCellValueFactory(cellData -> {
            int coachId = cellData.getValue().getCoachId();
            Coach coach = coachDAO.getById(coachId);
            return new SimpleStringProperty(coach != null ? coach.getNom() + " " + coach.getPrenom() : "N/A");
        });

        membreColumn.setCellValueFactory(cellData -> {
            int membreId = cellData.getValue().getMembreId();
            Membre membre = membreDAO.getById(membreId);
            return new SimpleStringProperty(membre != null ? membre.getNom() + " " + membre.getPrenom() : "N/A");
        });
    }

    private void configurerSpinners() {
        SpinnerValueFactory<Integer> heureFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 22, 9);
        heureSpinner.setValueFactory(heureFactory);

        SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15);
        minuteSpinner.setValueFactory(minuteFactory);
    }

    private void configurerListeners() {
        seanceTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectionLabel.setText("Sélectionnée: " + newSelection.getType() + " - " + newSelection.getDate());
                    } else {
                        selectionLabel.setText("Aucune sélection");
                    }
                }
        );
    }

    // ============ CHARGEMENT DONNÉES ============

    private void chargerSeances() {
        try {
            List<Seance> seances = seanceDAO.getAll();
            seancesData.clear();
            seancesData.addAll(seances);
            seanceTable.setItems(seancesData);
            totalSeancesLabel.setText("Total: " + seances.size() + " séance(s)");
            System.out.println("✅ " + seances.size() + " séance(s) chargée(s)");
        } catch (Exception e) {
            afficherErreur("Erreur de chargement", "Impossible de charger les séances: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void chargerCoachs() {
        try {
            List<Coach> coachs = coachDAO.getAll();
            ObservableList<Coach> coachData = FXCollections.observableArrayList(coachs);
            coachComboBox.setItems(coachData);
            System.out.println("✅ " + coachs.size() + " coach(s) chargé(s)");
        } catch (Exception e) {
            afficherErreur("Erreur", "Impossible de charger les coachs: " + e.getMessage());
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

    private void chargerTypesSeance() {
        ObservableList<String> types = FXCollections.observableArrayList(
                "Musculation", "Cardio", "Yoga", "CrossFit", "Pilates",
                "Boxing", "Spinning", "HIIT", "Stretching", "Autre"
        );
        typeComboBox.setItems(types);
    }

    // ============ ACTIONS BOUTONS ============

    @FXML
    private void handleNouvelleSeance() {
        modeModification = false;
        seanceEnCours = null;
        formulaireTitreLabel.setText("➕ Nouvelle Séance");
        viderFormulaire();
        afficherFormulaire(true);
        System.out.println("📝 Formulaire nouvelle séance affiché");
    }

    @FXML
    private void handleModifier() {
        Seance selection = seanceTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner une séance à modifier.");
            return;
        }

        modeModification = true;
        seanceEnCours = selection;
        formulaireTitreLabel.setText("✏️ Modifier Séance #" + selection.getId());
        remplirFormulaire(selection);
        afficherFormulaire(true);
        System.out.println("✏️ Modification séance #" + selection.getId());
    }

    @FXML
    private void handleSupprimer() {
        Seance selection = seanceTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            afficherAvertissement("Aucune sélection", "Veuillez sélectionner une séance à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer la séance : " + selection.getType() + " du " + selection.getDate() + " ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> resultat = confirmation.showAndWait();

        if (resultat.isPresent() && resultat.get() == ButtonType.OK) {
            try {
                seanceDAO.supprimer(selection.getId());
                chargerSeances();
                afficherSucces("Séance supprimée", "La séance a été supprimée avec succès.");
                System.out.println("🗑️ Séance #" + selection.getId() + " supprimée");
            } catch (Exception e) {
                afficherErreur("Erreur de suppression", "Impossible de supprimer la séance: " + e.getMessage());
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
            LocalTime heure = LocalTime.of(heureSpinner.getValue(), minuteSpinner.getValue());

            if (modeModification) {
                seanceEnCours.setDate(datePicker.getValue());
                seanceEnCours.setHeure(heure);
                seanceEnCours.setType(typeComboBox.getValue());
                seanceEnCours.setCoachId(coachComboBox.getValue().getId());
                seanceEnCours.setMembreId(membreComboBox.getValue().getId());
                seanceDAO.modifier(seanceEnCours);
                afficherSucces("Séance modifiée", "Les modifications ont été enregistrées.");
                System.out.println("✅ Séance #" + seanceEnCours.getId() + " modifiée");
            } else {
                Seance nouvelle = new Seance();
                nouvelle.setDate(datePicker.getValue());
                nouvelle.setHeure(heure);
                nouvelle.setType(typeComboBox.getValue());
                nouvelle.setCoachId(coachComboBox.getValue().getId());
                nouvelle.setMembreId(membreComboBox.getValue().getId());

                seanceDAO.ajouter(nouvelle);
                afficherSucces("Séance ajoutée", "La nouvelle séance a été ajoutée avec succès.");
                System.out.println("✅ Nouvelle séance ajoutée");
            }

            chargerSeances();
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
            List<Seance> resultats = seanceDAO.rechercher(critere);
            seancesData.clear();
            seancesData.addAll(resultats);
            seanceTable.setItems(seancesData);
            totalSeancesLabel.setText("Résultats: " + resultats.size() + " séance(s)");
            System.out.println("🔍 Recherche: " + resultats.size() + " résultat(s)");
        } catch (Exception e) {
            afficherErreur("Erreur de recherche", "Erreur lors de la recherche: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReinitialiser() {
        rechercheField.clear();
        chargerSeances();
        System.out.println("🔄 Liste réinitialisée");
    }

    // ============ GESTION FORMULAIRE ============

    private void afficherFormulaire(boolean visible) {
        formulaireBox.setVisible(visible);
        formulaireBox.setManaged(visible);
    }

    private void viderFormulaire() {
        datePicker.setValue(null);
        heureSpinner.getValueFactory().setValue(9);
        minuteSpinner.getValueFactory().setValue(0);
        typeComboBox.setValue(null);
        coachComboBox.setValue(null);
        membreComboBox.setValue(null);
        seanceEnCours = null;
    }

    private void remplirFormulaire(Seance seance) {
        datePicker.setValue(seance.getDate());
        heureSpinner.getValueFactory().setValue(seance.getHeure().getHour());
        minuteSpinner.getValueFactory().setValue(seance.getHeure().getMinute());
        typeComboBox.setValue(seance.getType());

        for (Coach coach : coachComboBox.getItems()) {
            if (coach.getId() == seance.getCoachId()) {
                coachComboBox.setValue(coach);
                break;
            }
        }

        for (Membre membre : membreComboBox.getItems()) {
            if (membre.getId() == seance.getMembreId()) {
                membreComboBox.setValue(membre);
                break;
            }
        }
    }

    private boolean validerFormulaire() {
        StringBuilder erreurs = new StringBuilder();

        if (datePicker.getValue() == null) {
            erreurs.append("• La date est obligatoire\n");
        } else if (datePicker.getValue().isBefore(LocalDate.now())) {
            erreurs.append("• La date ne peut pas être dans le passé\n");
        }

        if (typeComboBox.getValue() == null || typeComboBox.getValue().isEmpty()) {
            erreurs.append("• Le type de séance est obligatoire\n");
        }

        if (coachComboBox.getValue() == null) {
            erreurs.append("• Veuillez sélectionner un coach\n");
        }

        if (membreComboBox.getValue() == null) {
            erreurs.append("• Veuillez sélectionner un membre\n");
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
