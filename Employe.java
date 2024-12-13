/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.beans.property.*;

/**
 *
 * @author MIGUEL PYTHON
 */
public class Employe {

    private IntegerProperty idEm;
    private StringProperty nomEm;
    private StringProperty email;
    private StringProperty pass;
    private DoubleProperty salaire;
    private StringProperty dateEmbauche;
    private StringProperty numEm;
    private StringProperty numSecu;
    private StringProperty type;
    private StringProperty secteur;

    // Constructeur
    public Employe(int idEm, String nomEm, String email, double salaire,
            String dateEmbauche, String numEm, String numSecu, String type, String secteur) {
        this.idEm = new SimpleIntegerProperty(idEm);
        this.nomEm = new SimpleStringProperty(nomEm);
        this.email = new SimpleStringProperty(email);
        this.salaire = new SimpleDoubleProperty(salaire);
        this.dateEmbauche = new SimpleStringProperty(dateEmbauche);
        this.numEm = new SimpleStringProperty(numEm);
        this.numSecu = new SimpleStringProperty(numSecu);
        this.type = new SimpleStringProperty(type);
        this.secteur = new SimpleStringProperty(secteur);
    }

    public Employe() {

    }

    // Getters et Setters pour chaque propriété
    public int getIdEm() {
        return idEm.get();
    }

    public void setIdEm(int idEm) {
        this.idEm.set(idEm);
    }

    public IntegerProperty idEmProperty() {
        return idEm;
    }

    public String getNomEm() {
        return nomEm.get();
    }

    public void setNomEm(String nomEm) {
        this.nomEm.set(nomEm);
    }

    public StringProperty nomEmProperty() {
        return nomEm;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getPass() {
        return pass.get();
    }

    public void setPass(String pass) {
        this.pass.set(pass);
    }

    public StringProperty passProperty() {
        return pass;
    }

    public double getSalaire() {
        return salaire.get();
    }

    public void setSalaire(double salaire) {
        this.salaire.set(salaire);
    }

    public DoubleProperty salaireProperty() {
        return salaire;
    }

    public String getDateEmbauche() {
        return dateEmbauche.get();
    }

    public void setDateEmbauche(String dateEmbauche) {
        this.dateEmbauche.set(dateEmbauche);
    }

    public StringProperty dateEmbaucheProperty() {
        return dateEmbauche;
    }

    public String getNumEm() {
        return numEm.get();
    }

    public void setNumEm(String numEm) {
        this.numEm.set(numEm);
    }

    public StringProperty numEmProperty() {
        return numEm;
    }

    public String getNumSecu() {
        return numSecu.get();
    }

    public void setNumSecu(String numSecu) {
        this.numSecu.set(numSecu);
    }

    public StringProperty numSecuProperty() {
        return numSecu;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getSecteur() {
        return secteur.get();
    }

    public void setSecteur(String secteur) {
        this.secteur.set(secteur);
    }

    public StringProperty secteurProperty() {
        return secteur;
    }

    /*public void setBulletins(List<BulletinPaie> bulletins) {
        this.bulletins = bulletins;
    }*/
    //Meethode pour ajouter un bulletin
    /*public void ajouterBulletin(BulletinPaie b){
        bulletins.add(b);
    }*/
    @Override
    public String toString() {
        return "Employe [id : " + idEm + ", Nom : " + nomEm + ", email : " + email + ", "
                + "Salaire : " + salaire + ", Date embauche:" + dateEmbauche
                + ", Numero :" + numEm + ", Numero de securite sociale :" + numSecu + "]";
    }

    // Méthode pour valider l'email et le mot de passe
    public static boolean seConnecter(String email, String password) {
        // Exemple de validation basique
        // Ici, tu pourrais faire une vérification avec une base de données, par exemple
        if (email.equals("employe@exemple.com") && password.equals("motdepasse")) {
            return true;
        }
        return false;
    }

    public static double calculerSalaire(int bulletinId, double montantCaisse, double montantRedevance, double primeVal) throws SQLException {
        double salaireInitial = 0.0;
        double salaireFinal = 0.0;

        // Requête SQL pour récupérer toutes les informations nécessaires
        String query = "SELECT e.*, b.*"
                + "FROM employe e "
                + "JOIN bulletins b ON e.idEm = b.idEm "
                + "WHERE b.idBul = ?";  // Jointure entre employe et bulletin de paie avec la clé de l'employé

        try (Connection conn = Connexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Remplacer ? par l'ID de l'employé
            stmt.setInt(1, bulletinId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Récupérer le salaire et la prime de l'employé
                salaireInitial = rs.getDouble("e.salaire");
                String prime = rs.getString("b.prime");

                // Si l'employé a droit à une prime, l'ajouter au salaire
                if ("Oui".equalsIgnoreCase(prime)) {
                    salaireInitial += primeVal; // Ajouter la valeur de la prime
                }

               

                // Soustraire les valeurs de redevance et caisse
                salaireFinal = salaireInitial - montantCaisse - montantRedevance;

            } else {
                System.out.println("Aucun bulletin de paie trouvé pour l'ID : " + bulletinId);
                return 0.0; // Retourner 0 si l'employé ou le bulletin n'est pas trouvé
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0; // Retourner 0 en cas d'erreur de base de données
        }

        // Afficher ou retourner le salaire final
        System.out.println("Salaire final de l'employé après déductions : " + salaireFinal);
        return salaireFinal;
    }

}
