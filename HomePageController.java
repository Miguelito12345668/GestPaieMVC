/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package views;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Connexion;
import models.Employe;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import models.*;
import views.*;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.*;
import javafx.scene.Parent;

/**
 * FXML Controller class
 *
 * @author MIGUEL PYTHON
 */
public class HomePageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation si nécessaire
    }

    @FXML
    private TextField email;          // Correspond au TextField fx:id="email"

    @FXML
    private PasswordField password;   // Correspond au PasswordField fx:id="password"

    @FXML
    private Button connecter;     // Correspond au Button fx:id="connecter"

    @FXML
    private SplitMenuButton roleButton; // Correspond au SplitMenuButton fx:id="roleButton"

    // Méthode pour se connecter
    @FXML
    public void seConnecter() {
        String emailInput = email.getText();
        String passwordInput = password.getText();

        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            showError("Tous les champs doivent être remplis.");
        } else {
            if (isValidLogin(emailInput, passwordInput)) {
                // L'utilisateur est connecté avec succès
                showSuccess("Bienvenue cher administrateur");
                // Ici vous pouvez rediriger l'utilisateur vers une nouvelle fenêtre ou faire d'autres actions.
            } else {
                showError("Identifiants incorrects.");
            }
        }
    }

    // Méthode appelée lors de la sélection d'un rôle dans le SplitMenuButton
    @FXML
    public void handle(MenuItem selectedItem) {
        // Met à jour le texte du SplitMenuButton avec le texte de l'élément sélectionné
        roleButton.setText(selectedItem.getText());
    }

    // Méthode pour vérifier les identifiants
    private boolean isValidLogin(String email, String password) {
        boolean isValid = false;
        Connection conn = null;

        if ("admin@gmail.com".equals(email) && "admin".equals(password)) {
            isValid = true;  // Identifiants admin corrects
            loadAdminWindow();  // Charger la fenêtre de l'administrateur
            return isValid;
        }
        try {
            // Utilisation de la classe Connexion pour obtenir la connexion à la base de données
            conn = Connexion.getConnection(); // Obtenir la connexion à la base de données

            // Préparer la requête SQL pour récupérer le mot de passe de l'employé
            String sql = "SELECT pass FROM employe WHERE email = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, email); // Paramètre 1 : l'email

                // Exécuter la requête
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Si un résultat est trouvé, cela signifie que l'employé existe
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("pass");

                        // Vérification du mot de passe
                        if (password.equals(storedPassword)) {
                            isValid = true; // Identifiants corrects
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur de connexion à la base de données.");
        }

        return isValid;
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

    // Méthode pour charger la fenêtre de l'administrateur
    private void loadAdminWindow() {
        try {
            // Charger le fichier FXML pour l'interface administrateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));

            // Charger le fichier FXML et obtenir le root de la scène
            Parent root = loader.load();

            // Créer une nouvelle scène avec le root chargé
            Scene adminScene = new Scene(root);

            // Créer un nouveau Stage pour afficher l'interface administrateur
            Stage adminStage = new Stage();
            adminStage.setScene(adminScene);
            adminStage.setTitle("Tableau de bord Administrateur");  // Titre de la fenêtre
            adminStage.show();  // Afficher la fenêtre

            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la fenêtre administrateur.");
        }
    }

}
