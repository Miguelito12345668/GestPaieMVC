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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Connexion;

/**
 * FXML Controller class
 *
 * @author MIGUEL PYTHON
 */
public class UpdateEmployeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    private TextField abscence;
    @FXML
    private TextField type;
    @FXML
    private TextField secteur;
    @FXML
    private TextField prime;
    @FXML
    private Button modifier;

    // Méthode pour charger les informations de l'employé dans le formulaire
    public void chargerEmploye(int employeId) {
        String query = "SELECT * FROM employe WHERE idEm = ?";

        try (Connection conn = Connexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Remplir les champs du formulaire avec les données récupérées de la base
                    nom.setText(rs.getString("nomEm"));
                    email.setText(rs.getString("email"));
                    salaire.setText(String.valueOf(rs.getDouble("salaire")));
                    prime.setText(String.valueOf(rs.getDouble("prime")));
                    // Récupérer la date d'embauche et la convertir en LocalDate
                    java.sql.Date sqlDate = rs.getDate("dateEmbauche");
                    if (sqlDate != null) {
                        // Convertir sqlDate en LocalDate
                        LocalDate localDate = sqlDate.toLocalDate();
                        // Remplir le DatePicker avec la valeur
                        date.setValue(localDate);
                    } else {
                        // Si la date est null, tu peux gérer cela selon tes besoins
                        date.setValue(null);
                    }
                    tel.setText(rs.getString("numEm"));
                    securite.setText(rs.getString("numSecu"));
                    abscence.setText(String.valueOf(rs.getInt("nbAbs")));
                    type.setText(rs.getString("type"));
                    secteur.setText(rs.getString("secteur"));
                } else {
                    // Si aucun employé n'est trouvé avec cet ID
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun employé trouvé avec cet ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la récupération des données.");
        }
    }

    

    // Méthode pour afficher des messages dans des boîtes de dialogue
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
