/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package views;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Connexion;
import models.Employe;

/**
 * FXML Controller class
 *
 * @author MIGUEL PYTHON
 */
public class AdminDashboardController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private Label nbEmployes; // Le label pour afficher le nombre d'employés

    // Cette méthode récupère le nombre d'employés dans la base de données
    public void compterEmployes() {
        // Connexion à la base de données
        try (Connection conn = Connexion.getConnection(); Statement stmt = conn.createStatement()) {

            // Requête SQL pour récupérer le nombre d'employés par type
            String sql = "SELECT type, COUNT(*) AS total FROM employe WHERE type IN ('C', 'M', 'R') GROUP BY type";
            ResultSet rs = stmt.executeQuery(sql);

            // Initialisation des variables pour compter les employés par type
            int totalC = 0;
            int totalM = 0;
            int totalR = 0;
            int totalE = 0;

            // Traiter le résultat de la requête
            while (rs.next()) {
                String type = rs.getString("type");
                int total = rs.getInt("total");

                // Mettre à jour le total en fonction du type
                if ("C".equals(type)) {
                    totalC = total;
                } else if ("M".equals(type)) {
                    totalM = total;
                } else if ("R".equals(type)) {
                    totalR = total;
                }
                totalE = totalC + totalM + totalR;
            }

            nbEmployes.setText(totalC + " Caissiers | " + totalM + " Magazinier | " + totalR + " Rayonnistes" + " Total Employes : " + totalE);

        } catch (SQLException e) {
            e.printStackTrace();  // Afficher l'exception dans la console en cas d'erreur
        }
    }

    @FXML
    private TableView<Employe> employeTableView;

    @FXML
    private TableColumn<Employe, Integer> idEmColumn;
    @FXML
    private TableColumn<Employe, String> nomEmColumn;
    @FXML
    private TableColumn<Employe, String> emailColumn;
    @FXML
    private TableColumn<Employe, String> passColumn;
    @FXML
    private TableColumn<Employe, Double> salaireColumn;
    @FXML
    private TableColumn<Employe, String> dateEmbaucheColumn;
    @FXML
    private TableColumn<Employe, String> numEmColumn;
    @FXML
    private TableColumn<Employe, String> numSecuColumn;
    @FXML
    private TableColumn<Employe, String> typeColumn;
    @FXML
    private TableColumn<Employe, String> secteurColumn;

    private ObservableList<Employe> employeList = FXCollections.observableArrayList();

    // Méthode pour charger les employés depuis la base de données
    public void loadEmployesFromDatabase() {
        String query = "SELECT idEm , nomEm ,email,salaire,dateEmbauche,numEm,numSecu,type,secteur FROM employe";

        // Vider la liste avant de la remplir avec les nouvelles données
        employeList.clear();

        try (Connection conn = Connexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Créer un objet Employe et ajouter à la liste
                Employe employe = new Employe(
                        rs.getInt("idEm"),
                        rs.getString("nomEm"),
                        rs.getString("email"),
                        rs.getDouble("salaire"),
                        rs.getString("dateEmbauche"),
                        rs.getString("numEm"),
                        rs.getString("numSecu"),
                        rs.getString("type"),
                        rs.getString("secteur")
                );
                employeList.add(employe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour initialiser la TableView avec les données récupérées
    public void initializeTable() {
        // Lier les colonnes à leurs propriétés correspondantes de la classe Employe
        idEmColumn.setCellValueFactory(new PropertyValueFactory<>("idEm"));
        nomEmColumn.setCellValueFactory(new PropertyValueFactory<>("nomEm"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        salaireColumn.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        dateEmbaucheColumn.setCellValueFactory(new PropertyValueFactory<>("dateEmbauche"));
        numEmColumn.setCellValueFactory(new PropertyValueFactory<>("numEm"));
        numSecuColumn.setCellValueFactory(new PropertyValueFactory<>("numSecu"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        secteurColumn.setCellValueFactory(new PropertyValueFactory<>("secteur"));

        // Lier la TableView avec la liste des employés
        employeTableView.setItems(employeList);
    }

    // Méthode qui met à jour la TableView après avoir chargé les données
    public void updateTable() {
        loadEmployesFromDatabase();
        initializeTable();
    }

    @FXML
    private TextField searchTextField;

    @FXML
    public void handleSearch(KeyEvent event) {
        String searchText = searchTextField.getText().toLowerCase();

        // Créer une liste filtrée d'employés en fonction de l'email
        ObservableList<Employe> filteredList = FXCollections.observableArrayList();

        for (Employe employe : employeList) {
            // Filtrer uniquement sur l'email
            if (employe.getEmail().toLowerCase().contains(searchText)) {
                filteredList.add(employe);
            }
        }
        // Mettre à jour la TableView avec la liste filtrée
        employeTableView.setItems(filteredList);
    }

    @FXML
    private TextField idTextField;

    @FXML
    public void supprimerEmploye() {
        // Récupérer l'ID de l'employé depuis le TextField
        String employeId = idTextField.getText();

        if (employeId.isEmpty()) {
            // Afficher un message d'erreur si l'ID est vide
            showAlert(AlertType.ERROR, "Erreur", "L'ID de l'employé ne peut pas être vide.");
            return;
        }

        // Créer une boîte de dialogue de confirmation avant la suppression
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet employé ?");
        confirmationAlert.setContentText("Cette action est irréversible.");

        // Attendre la réponse de l'utilisateur
        ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

        // Si l'utilisateur clique sur "OK", procéder à la suppression
        if (result == ButtonType.OK) {
            String deleteQuery = "DELETE FROM employe WHERE idEm = ?";

            try (Connection conn = Connexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

                // Lier l'ID à la requête SQL
                stmt.setInt(1, Integer.parseInt(employeId)); // Convertir l'ID en entier

                // Exécuter la requête de suppression
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Afficher un message de succès si l'employé a été supprimé
                    showAlert(AlertType.INFORMATION, "Succès", "L'employé a été supprimé avec succès.");
                } else {
                    // Afficher un message d'erreur si l'employé n'a pas été trouvé
                    showAlert(AlertType.ERROR, "Erreur", "Aucun employé trouvé avec cet ID.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                // Afficher une alerte d'erreur en cas de problème avec la base de données
                showAlert(AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la suppression de l'employé.");
            }
        }
    }

   
    private void openModificationWindow() {
        try {
            // Créer un nouveau stage (fenêtre)
            Stage modificationStage = new Stage();

            // Charger le fichier FXML pour le formulaire de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateEmploye.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre modifiée
            UpdateEmployeController controller = loader.getController();

            // Récupérer l'ID de l'employé depuis le champ idTextField de la scène principale
            String employeId = idTextField.getText();

            // Charger les informations de l'employé dans la nouvelle fenêtre
            if (!employeId.isEmpty()) {
                controller.chargerEmploye(Integer.parseInt(employeId));
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un ID d'employé.");
            }

            // Configurer et afficher la fenêtre
            modificationStage.setTitle("Modifier Employé");
            modificationStage.initModality(Modality.APPLICATION_MODAL);  // Modale, pour empêcher l'interaction avec la fenêtre principale
            modificationStage.setScene(new Scene(root));
            modificationStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture de la fenêtre de modification.");
        }
    }
    //Méthode pour charger les informations de l'employé dans le formulaire

    /*@FXML
    public void chargerEmploye() {
        String employeId = idTextField.getText();

        if (employeId.isEmpty()) {
            showAlert(AlertType.ERROR, "Erreur", "Veuillez entrer un ID d'employé.");
            return;
        }

        // Requête pour récupérer les données de l'employé
        String query = "SELECT * FROM employe WHERE idEm = ?";

        try (Connection conn = Connexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(employeId));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Remplir les champs du formulaire avec les informations de l'employé
                    nomTextField.setText(rs.getString("nomEm"));
                    emailTextField.setText(rs.getString("email"));
                    salaireTextField.setText(String.valueOf(rs.getDouble("salaire")));
                    primeTextField.setText(String.valueOf(rs.getDouble("prime")));
                    dateEmbaucheTextField.setText(rs.getString("dateEmbauche"));
                    numEmTextField.setText(rs.getString("numEm"));
                    numSecuTextField.setText(rs.getString("numSecu"));
                    nbAbsTextField.setText(String.valueOf(rs.getInt("nbAbs")));
                    typeTextField.setText(rs.getString("type"));
                    secteurTextField.setText(rs.getString("secteur"));
                } else {
                    // Afficher un message si l'employé avec cet ID n'existe pas
                    showAlert(AlertType.ERROR, "Erreur", "Aucun employé trouvé avec cet ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la récupération des données.");
        }
    }*/
    // Méthode utilitaire pour afficher des boîtes de dialogue
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Pas de header
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    public void loadFormEmploye() {
        try {
            // Charger le fichier FXML pour l'interface administrateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addEmploye.fxml"));

            // Charger le fichier FXML et obtenir le root de la scène
            Parent root = loader.load();

            // Créer une nouvelle scène avec le root chargé
            Scene adminScene = new Scene(root);

            // Créer un nouveau Stage pour afficher l'interface administrateur
            Stage adminStage = new Stage();
            adminStage.setScene(adminScene);
            adminStage.setTitle("Ajouter un employe");  // Titre de la fenêtre
            adminStage.show();  // Afficher la fenêtre

            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement du formulaire d'ajou de l'employe.");
        }
    }
     
    // Méthode pour afficher une boîte de dialogue d'erreur
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher une boîte de dialogue de succès
    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Connexion réussie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    public void loadBulletin() {
        try {
            // Charger le fichier FXML pour l'interface administrateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableBulletins.fxml"));

            // Charger le fichier FXML et obtenir le root de la scène
            Parent root = loader.load();

            // Créer une nouvelle scène avec le root chargé
            Scene adminScene = new Scene(root);

            // Créer un nouveau Stage pour afficher l'interface administrateur
            Stage adminStage = new Stage();
            adminStage.setScene(adminScene);
            adminStage.setTitle("Table des bulletins enregistres");  // Titre de la fenêtre
            adminStage.show();  // Afficher la fenêtre

            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la fenetre.");
        }
    }
    
    @FXML
    private TextField primeField;

    @FXML
    private Button confirmerPrimeButton;

    private LocalDate dateDerniereModification;

    @FXML
    public void initialize() {
        // Initialisation du TextField pour accepter uniquement les entiers positifs
        primeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {  // Autoriser uniquement des chiffres
                primeField.setText(oldValue); // Revenir à l'ancienne valeur si la saisie n'est pas un entier
            }
        });

        // Action du bouton Confirmer Prime
        confirmerPrimeButton.setOnAction(event -> confirmerPrime());
    }

    // Méthode pour confirmer la saisie de la prime
    public void confirmerPrime() {
        // Vérifier si la saisie est un entier positif
        String input = primeField.getText();
        if (input.isEmpty() || !isPositiveInteger(input)) {
            showAlert("Erreur", "La prime doit être un entier positif.");
            return;
        }

        // Demander à l'utilisateur si la saisie est correcte
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Validation de la prime");
        alert.setHeaderText("Confirmez-vous la prime saisie ?");
        alert.setContentText("Prime : " + input);

        alert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                // Si l'utilisateur valide la saisie, on garde la valeur
                dateDerniereModification = LocalDate.now();
                primeField.setDisable(true); // Désactiver le TextField pour un mois
                showAlert("Succès", "La prime a été enregistrée et ne peut plus être modifiée pendant un mois.");
            }
        });
    }

    // Vérifie si la chaîne représente un entier positif
    private boolean isPositiveInteger(String input) {
        try {
            int value = Integer.parseInt(input);
            return value > 0; // Vérifier que le nombre est positif
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Méthode pour vérifier si le mois est passé depuis la dernière modification
    public boolean peutModifierPrime() {
        if (dateDerniereModification == null) {
            return true; // Si aucune date de modification n'existe, on peut modifier
        }
        long monthsBetween = ChronoUnit.MONTHS.between(dateDerniereModification, LocalDate.now());
        return monthsBetween >= 1; // Si un mois est passé, on peut modifier
    }

}
