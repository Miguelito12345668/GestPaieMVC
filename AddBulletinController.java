/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package views;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Connexion;

/**
 * FXML Controller class
 *
 * @author MIGUEL PYTHON
 */
public class AddBulletinController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
     @FXML
    private DatePicker date;
    @FXML
    private TextField nom;
    @FXML
    private TextField adresse;
    @FXML
    private TextField employe;
    @FXML
    private TextField abscence;
    @FXML
    private CheckBox check;

    
    @FXML
    private Button ajouter;
    
    // Méthode qui sera appelée quand l'utilisateur clique sur "Ajouter"
    @FXML
    private void handleAddBulletin() {
        // Récupérer les valeurs des champs
        String nomText = nom.getText();
        LocalDate dateRemise = date.getValue();
        String adresseText = adresse.getText();
        String employeText = employe.getText();
        boolean etatCheckBox = check.isSelected();  // Récupère l'état de la CheckBox (true ou false)
        String abscenceText = abscence.getText();
    
        

        // Validation de base
        if (nomText.isEmpty() || dateRemise == null ||
            adresseText.isEmpty() || employeText.isEmpty() || abscenceText.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        try {
            // Conversion des champs numériques
            int employeVal = Integer.parseInt(employeText);  
            int abscenceVal = Integer.parseInt(abscenceText); 
            // Convertir l'état de la case à cocher en "Oui" ou "Non"
            String primeVal = etatCheckBox ? "Oui" : "Non";
            if(abscenceVal < 0){
                showAlert("Erreur","Le nombre d'abscence ne peut etre negatif");
            }
            Connection conn = null;

            try {
                // Utilisation de la classe Connexion pour obtenir la connexion à la base de données
                conn = Connexion.getConnection(); // Obtenir la connexion à la base de données
                // La requête SQL pour ajouter un employé
                String sql = "INSERT INTO bulletins (dateRemise, nomSociete, adresse, idEm, nbAbs,prime) "
                           + "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    // Lier les paramètres à la requête SQL
                    stmt.setDate(1, java.sql.Date.valueOf(dateRemise));
                    stmt.setString(2, nomText);
                    stmt.setString(3, adresseText);
                    stmt.setInt(4, employeVal);
                    stmt.setInt(5, abscenceVal);
                    stmt.setString(6, primeVal);
                    // Exécuter la requête
                    stmt.executeUpdate();

                    // Afficher un message de succès
                    showAlert("Succès", "Le bulletin a été ajouté avec succès.");
                } catch (SQLException e) {
                    showAlert("Erreur", "Erreur lors de l'ajout du bulletin: " + e.getMessage());
                }
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur de connexion à la base de données: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Assurez-vous que le champs numérique (employe) soit valide.");
        }
    }

    // Méthode pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
