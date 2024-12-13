/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package views;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Connexion;

/**
 * FXML Controller class
 *
 * @author MIGUEL PYTHON
 */
public class AddEmployeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    public void initialize() {
        // Date de début de la plage (1er janvier 1990)
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        
        // Date de fin de la plage (une semaine avant la date actuelle)
        LocalDate endDate = LocalDate.now().minusWeeks(1);
        
        // Définir l'intervalle de dates sélectionnables
        date.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                
                // Bloquer les dates en dehors de la plage
                setDisable(item.isBefore(startDate) || item.isAfter(endDate));
            }
        });
    }



    @FXML
    private TextField nom;
    @FXML
    private TextField email;
    @FXML
    private PasswordField pass;
    @FXML
    private TextField salaire;
    @FXML
    private DatePicker date;
    @FXML
    private TextField tel;
    @FXML
    private TextField securite;
    @FXML
    private TextField type;
    @FXML
    private TextField secteur;
    @FXML
    private Button ajouter;
    
    // Méthode qui sera appelée quand l'utilisateur clique sur "Ajouter"
    @FXML
    private void handleAddEmploye() {
        // Récupérer les valeurs des champs
        String nomText = nom.getText();
        String emailText = email.getText();
        String passText = pass.getText();
        String salaireText = salaire.getText();
        LocalDate dateEmbauche = date.getValue();
        String telText = tel.getText();
        String securiteText = securite.getText();
        String typeText = type.getText();
        String secteurText = secteur.getText();

        // Validation de base
        if (nomText.isEmpty() || emailText.isEmpty() || passText.isEmpty() || salaireText.isEmpty() || dateEmbauche == null ||
            telText.isEmpty() || securiteText.isEmpty()  || typeText.isEmpty() || secteurText.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        try {
            // Conversion des champs numériques
            double salaireVal = Double.parseDouble(salaireText);
            int telVal = Integer.parseInt(telText);
            
            
            // Récupérer et transformer le premier caractère du champ "type" en majuscule
            if (!typeText.isEmpty()) {
                typeText = typeText.substring(0, 1).toUpperCase();
            }
            
            Connection conn = null;

            try {
                // Utilisation de la classe Connexion pour obtenir la connexion à la base de données
                conn = Connexion.getConnection(); // Obtenir la connexion à la base de données
                // La requête SQL pour ajouter un employé
                String sql = "INSERT INTO employe (nomEm, email, pass, salaire, dateEmbauche, numEm, numSecu, type, secteur) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    // Lier les paramètres à la requête SQL
                    stmt.setString(1, nomText);
                    stmt.setString(2, emailText);
                    stmt.setString(3, passText);
                    stmt.setDouble(4, salaireVal);
                    stmt.setDate(5, java.sql.Date.valueOf(dateEmbauche));
                    stmt.setInt(6, telVal);
                    stmt.setString(7, securiteText);
                    stmt.setString(8, typeText);
                    stmt.setString(9, secteurText);

                    // Exécuter la requête
                    stmt.executeUpdate();

                    // Afficher un message de succès
                    showAlert("Succès", "L'employé a été ajouté avec succès.");
                } catch (SQLException e) {
                    showAlert("Erreur", "Erreur lors de l'ajout de l'employé: " + e.getMessage());
                }
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur de connexion à la base de données: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Assurez-vous que les champs numériques (Salaire, Prime, Téléphone, Sécurité sociale, Absences) sont valides.");
        }
    }

    // Méthode pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
