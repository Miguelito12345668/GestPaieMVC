/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestpaiemvc;

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

/**
 *
 * @author MIGUEL PYTHON
 */
public class GestPaieMVC extends Application{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        // Fermer la connexion après le test (utile si vous avez d'autres opérations dans le main)
        launch(args);
        
    }
     @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML
       /* FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HomePage.fxml"));
        AnchorPane root = loader.load();
        
        // Créer la scène avec le root et définir la taille de la fenêtre
        Scene scene = new Scene(root, 700, 473);
        
       
        
        // Configurer et afficher la fenêtre
        primaryStage.setTitle("GestPaie");
        primaryStage.setScene(scene);
        
         // Bloquer le redimensionnement
        primaryStage.setResizable(false);  // Désactive le redimensionnement
        primaryStage.show();*/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HomePage.fxml"));
        AnchorPane root = loader.load();
        
        // Créer la scène avec le root et définir la taille de la fenêtre
        Scene scene = new Scene(root, 700, 470);
        //Scene scene = new Scene(root,600,600);
        
       
        
        // Configurer et afficher la fenêtre
        primaryStage.setTitle("Bienvenue sur GestPaie");
        primaryStage.setScene(scene);
        
         // Bloquer le redimensionnement
        primaryStage.setResizable(false);  // Désactive le redimensionnement
        primaryStage.show();
    }
}
