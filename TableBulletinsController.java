/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package views;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import models.Connexion;
import models.Employe;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
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
import javafx.stage.Stage;
import javax.swing.text.Document;
import models.BulletinPaie;

/**
 * FXML Controller class
 *
 * @author MIGUEL PYTHON
 */
public class TableBulletinsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    private TableView<BulletinPaie> bulletinTableView;

    @FXML
    private TableColumn<BulletinPaie, Integer> idBulColumn;
    @FXML
    private TableColumn<BulletinPaie, String> nomColumn;
    @FXML
    private TableColumn<BulletinPaie, String> adresseColumn;
    @FXML
    private TableColumn<BulletinPaie, Integer> employeColumn;
    @FXML
    private TableColumn<BulletinPaie, String> dateRemiseColumn;
    @FXML
    private TableColumn<BulletinPaie, String> abscenceColumn;
    @FXML
    private TableColumn<BulletinPaie, String> primeColumn;

    private ObservableList<BulletinPaie> bulletinList = FXCollections.observableArrayList();
    @FXML
    private TextField idbul;  // TextField où l'utilisateur saisit l'ID du bulletin
    @FXML
    private Button printButton; // Bouton pour imprimer le bulletin

    public void loadBulletinsFromDatabase() {
        String query = "SELECT * FROM bulletins"; // Requête SQL pour récupérer les bulletins

        // Vider la liste avant de la remplir avec les nouvelles données
        bulletinList.clear();

        try (Connection conn = Connexion.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            // Parcourir les résultats de la requête
            while (rs.next()) {
                // Créer un objet Bulletin à partir des résultats de la requête
                BulletinPaie bulletin = new BulletinPaie(
                        rs.getInt("idBul"), // Récupérer l'id du bulletin
                        rs.getString("dateRemise"), // Récupérer la date de remise
                        rs.getString("nomSociete"), // Récupérer le nom de la société
                        rs.getString("adresse"), // Récupérer l'adresse de la société
                        rs.getInt("idEm"), // Récupérer l'id de l'employé associé au bulletin
                        rs.getInt("nbAbs"),
                        rs.getString("prime")
                );

                // Ajouter le bulletin à la liste observable
                bulletinList.add(bulletin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeBulletinTable() {
        // Lier les colonnes aux propriétés correspondantes de la classe Bulletin
        idBulColumn.setCellValueFactory(new PropertyValueFactory<>("idBul"));
        dateRemiseColumn.setCellValueFactory(new PropertyValueFactory<>("dateRemise"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomSociete"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        employeColumn.setCellValueFactory(new PropertyValueFactory<>("idEm"));
        abscenceColumn.setCellValueFactory(new PropertyValueFactory<>("nbAbs"));
        primeColumn.setCellValueFactory(new PropertyValueFactory<>("prime"));

        // Lier la TableView avec la liste des bulletins
        bulletinTableView.setItems(bulletinList);
    }

    public void updateBulletinTable() {
        loadBulletinsFromDatabase(); // Charger les bulletins depuis la base de données
        initializeBulletinTable();   // Initialiser la TableView avec les nouvelles données
    }

    // Méthode appelée lors du clic sur le bouton "imprimer le bulletin"
    /* @FXML
    public void printBulletin() {
        // Récupérer l'ID du bulletin saisi dans le TextField
        String bulletinId = idbul.getText();

        if (bulletinId.isEmpty()) {
            // Si l'ID est vide, on affiche un message d'erreur
            System.out.println("Veuillez entrer un identifiant de bulletin.");
            return;
        }

        // Charger les informations depuis la base de données
        BulletinPaie bulletin = getBulletinFromDatabase(bulletinId);

        if (bulletin != null) {
            // Créer un PDF avec les informations du bulletin
            createPdf(bulletin);
        } else {
            // Si aucun bulletin n'a été trouvé, afficher un message
            System.out.println("Aucun bulletin trouvé pour l'ID: " + bulletinId);
        }
    }*/
    // Méthode pour interroger la base de données et récupérer les informations du bulletin
    private BulletinPaie getBulletinFromDatabase(String bulletinId) {
        String query = "SELECT * FROM bulletin WHERE idBul = ?";
        BulletinPaie bulletin = null;

        try (Connection conn = Connexion.getConnection(); // Connexion à la base de données
                 PreparedStatement stmt = conn.prepareStatement(query)) {

            // Paramétrer la requête avec l'ID du bulletin
            stmt.setString(1, bulletinId);

            // Exécuter la requête et récupérer les résultats
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Créer un objet Bulletin avec les résultats
                    bulletin = new BulletinPaie(
                            rs.getInt("idBul"),
                            rs.getString("dateRemise"),
                            rs.getString("nomSociete"),
                            rs.getString("adresseSociete"),
                            rs.getInt("employeAssocie"),
                            rs.getInt("nbAbs"),
                            rs.getString("prime")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bulletin;
    }

    // Méthode pour générer le PDF avec les informations du bulletin
    /* private void createPdf(BulletinPaie bulletin) {
        // Définir le chemin du fichier PDF
        String pdfFilePath = "Bulletin_" + bulletin.getIdBul() + ".pdf";

        try {
            // Créer un PdfWriter pour écrire dans le fichier PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFilePath));

            // Créer un PdfDocument en utilisant le PdfWriter
            PdfDocument pdf = new PdfDocument(writer);

            // Créer un Document à partir du PdfDocument
            Document document = new Document(pdf);

            // Ajouter des paragraphes avec les informations du bulletin
            document.add(new Paragraph("ID Bulletin: " + bulletin.getIdBul()));
            document.add(new Paragraph("Date de remise: " + bulletin.getDateRemise()));
            document.add(new Paragraph("Nom de la société: " + bulletin.getNomSociete()));
            document.add(new Paragraph("Adresse de la société: " + bulletin.getAdresseSociete()));
            document.add(new Paragraph("Employé associé: " + bulletin.getEmployeAssocie()));

            // Fermer le document (enregistrer le fichier PDF)
            document.close();

            System.out.println("Le fichier PDF a été généré avec succès !");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
    @FXML
    public void loadBulletin() {
        try {
            // Charger le fichier FXML pour l'interface administrateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addBulletin.fxml"));

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

    // Méthode pour afficher une boîte de dialogue d'erreur
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher une boîte de dialogue de succès
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connexion réussie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private Label rLabel;  // Label où l'année sera affichée

    @FXML
    private Button rButton;  // Le bouton qui déclenche l'action
    @FXML
    private Label cLabel;  // Label où l'année sera affichée
    @FXML
    private Label pLabel;  // Label où l'année sera affichée
    @FXML
    private Label test;  // Label où l'année sera affichée

    @FXML
    private Button cButton;  // Le bouton qui déclenche l'action
    @FXML
    private Button confirmRedevance;  // Le bouton qui déclenche l'action
    @FXML
    private Button confirmCaisse;  // Le bouton qui déclenche l'action
    @FXML
    private TextField rTextField;
    @FXML
    private TextField cTextField;
    @FXML
    private Button pButton;  // Le bouton qui déclenche l'action
    @FXML
    private Button confirmPrime;  // Le bouton qui déclenche l'action
    @FXML
    private TextField pTextField;

    // Méthode qui est appelée quand le bouton est pressé
    @FXML
    public void Redevance() {
        // Récupérer l'année en cours
        int currentYear = Year.now().getValue();

        // Modifier le texte du label avec l'année en cours
        rLabel.setText("Redevance audiovisuelle de l'annee " + currentYear);
    }

    // Méthode qui est appelée quand le bouton est pressé
    @FXML
    public void Caisse() {
        // Récupérer l'année en cours
        int currentYear = Year.now().getValue();

        // Modifier le texte du label avec l'année en cours
        cLabel.setText("Caisse Nationale de l'annee " + currentYear);
    }

    @FXML
    public void Prime() {
        // Récupérer l'année en cours
        LocalDate currentDate = LocalDate.now();
        int currentYear = Year.now().getValue();
        int currentMonthValue = currentDate.getMonthValue();

        // Récupérer le mois sous forme de nom
        Month currentMonth = currentDate.getMonth();

        // Modifier le texte du label avec l'année en cours
        pLabel.setText("Prime du mois de  " + currentMonth + " " + currentYear);
    }

    // Date de la dernière confirmation
    private LocalDate lastConfirmedDate;

    // Méthode pour vérifier si un an s'est écoulé depuis la dernière confirmation
    private boolean isOneYearPassed() {
        if (lastConfirmedDate == null) {
            return true; // Si aucune confirmation n'a été effectuée, autoriser la confirmation
        }
        long daysBetween = ChronoUnit.DAYS.between(lastConfirmedDate, LocalDate.now());
        return daysBetween >= 365;
    }

    // Méthode pour afficher un message de confirmation
    private void showConfirmationMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirmation réussie");
        alert.setHeaderText("La redevance a été confirmée");
        alert.setContentText("Le champ a été désactivé pour un an.");
        alert.showAndWait();
    }

    // Méthode pour afficher un message d'erreur
    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur lors de la confirmation");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void Caissecon() {
        // Vérifier si un an s'est écoulé depuis la dernière confirmation
        if (isOneYearPassed()) {
            // Si un an est passé, activer à nouveau le champ
            cLabel.setDisable(false);
            cButton.setDisable(false);
            cTextField.setDisable(false);
            cButton.setDisable(false);
            lastConfirmedDate = null; // Réinitialiser la date de confirmation
        }

        // Récupérer le montant saisi par l'administrateur
        String montantText = cTextField.getText();

        // Vérifier si le montant saisi est un nombre valide et positif
        if (!isValidPositiveNumber(montantText)) {
            // Si ce n'est pas un nombre valide ou un nombre négatif, afficher un message d'erreur
            showErrorMessage("Le montant saisi n'est pas valide. Veuillez entrer un nombre positif.");
            return; // Arrêter l'exécution de la méthode
        }

        // Afficher une boîte de dialogue pour confirmer le montant
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Vérification de la Caisse nationale");
        alert.setHeaderText("Vérifiez le montant de la Caisse Nationale");
        alert.setContentText("Le montant saisi est : " + cTextField.getText() + "\n\n"
                + "Veuillez confirmer si ce montant est celui fixé par l'État. "
                + "Si ce n'est pas le cas, des poursuites judiciaires peuvent être engagées.");

        // Ajouter les boutons de confirmation
        ButtonType confirmButton = new ButtonType("Confirmer");
        ButtonType cancelButton = new ButtonType("Annuler");

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        // Afficher l'alerte et gérer la réponse
        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                // Si l'administrateur confirme, désactiver le champ pour un an
                cLabel.setDisable(true); // Désactiver le champ pour un an
                confirmCaisse.setDisable(true); // Désactiver le bouton de confirmation
                cTextField.setDisable(true);
                cButton.setDisable(true);
                // Mettre à jour la date de la dernière confirmation
                lastConfirmedDate = LocalDate.now();

                // Afficher un message de confirmation
                showConfirmationMessage();
            } else {
                // Si l'administrateur annule, afficher un message d'erreur
                showErrorMessage("La caisse nationale n'a pas été confirmée. Aucune modification n'a été effectuée.");
            }
        });
    }

    @FXML
    public void Redevancecon() {
        // Vérifier si un an s'est écoulé depuis la dernière confirmation
        if (isOneYearPassed()) {
            // Si un an est passé, activer à nouveau le champ
            rLabel.setDisable(false);
            rButton.setDisable(false);
            rTextField.setDisable(false);
            rButton.setDisable(false);
            lastConfirmedDate = null; // Réinitialiser la date de confirmation
        }

        // Récupérer le montant saisi par l'administrateur
        String montantText = rTextField.getText();

        // Vérifier si le montant saisi est un nombre valide et positif
        if (!isValidPositiveNumber(montantText)) {
            // Si ce n'est pas un nombre valide ou un nombre négatif, afficher un message d'erreur
            showErrorMessage("Le montant saisi n'est pas valide. Veuillez entrer un nombre positif.");
            return; // Arrêter l'exécution de la méthode
        }

        // Afficher une boîte de dialogue pour confirmer le montant
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Vérification de la redevance");
        alert.setHeaderText("Vérifiez le montant de la redevance");
        alert.setContentText("Le montant saisi est : " + montantText + "\n\n"
                + "Veuillez confirmer si ce montant est celui fixé par l'État. "
                + "Si ce n'est pas le cas, des poursuites judiciaires peuvent être engagées.");

        // Ajouter les boutons de confirmation
        ButtonType confirmButton = new ButtonType("Confirmer");
        ButtonType cancelButton = new ButtonType("Annuler");

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        // Afficher l'alerte et gérer la réponse
        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                // Si l'administrateur confirme, désactiver le champ pour un an
                rLabel.setDisable(true); // Désactiver le champ pour un an
                confirmRedevance.setDisable(true); // Désactiver le bouton de confirmation
                rTextField.setDisable(true);
                rButton.setDisable(true);
                // Mettre à jour la date de la dernière confirmation
                lastConfirmedDate = LocalDate.now();

                // Sauvegarder la date de confirmation pour la persistance
                //saveLastConfirmedDate();
                // Afficher un message de confirmation
                showConfirmationMessage();
            } else {
                // Si l'administrateur annule, afficher un message d'erreur
                showErrorMessage("La redevance n'a pas été confirmée. Aucune modification n'a été effectuée.");
            }
        });
    }

// Méthode pour valider si le montant est un nombre positif
    private boolean isValidPositiveNumber(String montantText) {
        try {
            // Convertir le texte en double
            double montant = Double.parseDouble(montantText);
            // Vérifier que le montant est positif
            return montant > 0;
        } catch (NumberFormatException e) {
            // Si la conversion échoue, c'est un texte ou un nombre invalide
            return false;
        }
    }
    private LocalDate dateDerniereModification;
    // Méthode pour confirmer la saisie de la prime

    public void confirmerPrime() {
        // Vérifier si la saisie est un entier positif
        String input = pTextField.getText();
        if (input.isEmpty() || !isValidPositiveNumber(input)) {
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
                pTextField.setDisable(true); // Désactiver le TextField pour un mois
                pLabel.setDisable(true);
                confirmPrime.setDisable(true);
                pButton.setDisable(true);
                showAlert("Succès", "La prime a été enregistrée et ne peut plus être modifiée pendant un mois.");
            }
        });
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

    @FXML
    public void calculerSalaireAction() {
        try {
            // Récupérer les valeurs des TextField
            double montantRedevance = Double.parseDouble(rTextField.getText());
            double montantCaisse = Double.parseDouble(cTextField.getText());
            double primeVal = Double.parseDouble(pTextField.getText());

            // Récupérer l'ID du bulletin de paie
            int bulletinId = Integer.parseInt(idbul.getText());

            // Appeler la méthode de calcul du salaire depuis la classe Employe
            double salaireFinal = Employe.calculerSalaire(bulletinId, montantCaisse, montantRedevance, primeVal);

            // Afficher ou utiliser le résultat (par exemple dans une alerte ou un label)
            //System.out.println("Salaire final calculé : " + salaireFinal);
            test.setText(Double.toString(salaireFinal));

            // Optionnel : Afficher le salaire final dans une alerte
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Calcul du salaire");
            alert.setHeaderText(null);
            alert.setContentText("Le salaire final de l'employé est : " + salaireFinal);
            alert.showAndWait();

        } catch (NumberFormatException e) {
            // Gérer les erreurs de format des champs numériques
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Valeurs incorrectes");
            alert.setContentText("Veuillez entrer des valeurs numériques valides dans les champs.");
            alert.showAndWait();
        } catch (SQLException e) {
            // Gérer les erreurs liées à la base de données
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de base de données");
            alert.setHeaderText("Erreur lors de la récupération des données");
            alert.setContentText("Une erreur s'est produite lors de la récupération des données du bulletin de paie.");
            alert.showAndWait();
        }
    }

    @FXML
    private Label primetext;  // Label où l'année sera affichée
    @FXML
    private Label caisse;  // Label où l'année sera affichée
    @FXML
    private Label redevance;  // Label où l'année sera affichée
    @FXML
    private Label salNet;  // Label où l'année sera affichée
    @FXML
    private Label nbAbs;  // Label où l'année sera affichée
    @FXML
    private Label secteur;  // Label où l'année sera affichée
    @FXML
    private Label categorie;  // Label où l'année sera affichée
    @FXML
    private Label nom;  // Label où l'année sera affichée
    @FXML
    private Label dateEmbauche;  // Label où l'année sera affichée
    @FXML
    private Label securite;  // Label où l'année sera affichée
    @FXML
    private Label dateRemise;  // Label où l'année sera affichée
    @FXML
    private Label salBaseTable;  // Label où l'année sera affichée
    @FXML
    private Label salBase;  // Label où l'année sera affichée
    @FXML
    private Label prime;  // Label où l'année sera affichée
   

    @FXML
    public void printBulletin() throws SQLException {
        // Requête SQL pour récupérer toutes les informations nécessaires
        String query = "SELECT e.*, b.* "
                + "FROM employe e "
                + "JOIN bulletins b ON e.idEm = b.idEm "
                + "WHERE b.idBul = ?";  // Jointure entre employé et bulletin de paie avec la clé de l'employé

        // Récupérer l'ID du bulletin de paie depuis l'interface
        int bulletinId = Integer.parseInt(idbul.getText());

        // Déclaration des variables en dehors du bloc rs.next
        String nomEmploye = "", secteurEmploye = "", categorieEmploye = "",
                dateEmbaucheEmploye = "", dateRemiseBulletin = "", securiteSociale = "";
        double salaireDeBase = 0.0, salaireNet = 0.0;
        int absences = 0;
        int a = 0;
        String prime1 = "";
        String primeFinal = "";

        try (Connection conn = Connexion.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Définir le paramètre dans la requête SQL
            stmt.setInt(1, bulletinId);

            // Exécuter la requête
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Récupérer les informations de l'employé et du bulletin
                    nomEmploye = rs.getString("e.nomEm");
                    secteurEmploye = rs.getString("e.secteur");
                    categorieEmploye = rs.getString("e.type");
                    dateEmbaucheEmploye = rs.getString("e.dateEmbauche");
                    dateRemiseBulletin = rs.getString("b.dateRemise");
                    securiteSociale = rs.getString("e.numSecu");
                    salaireDeBase = rs.getDouble("e.salaire");
                    absences = rs.getInt("b.nbAbs");
                    prime1 = rs.getString("b.prime");
                    
                   

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
           
           // primetext.setText(pTextField.getText());
            nom.setText(nomEmploye);
            secteur.setText(secteurEmploye);
            categorie.setText(categorieEmploye);
            dateEmbauche.setText(dateEmbaucheEmploye);
            dateRemise.setText(dateRemiseBulletin);
            securite.setText(securiteSociale);
            salBase.setText(String.format("%.2f", salaireDeBase));
            salBaseTable.setText(String.format("%.2f", salaireDeBase));  // Exemple pour afficher le salaire de base dans un autre label
            salNet.setText(test.getText());
            
            // Mettre à jour les autres labels ou TextFields
            nbAbs.setText(absences + " heures");
            salNet.setText(test.getText());  // Afficher le salaire net, par exemple

            // Mettre à jour les TextFields avec les valeurs récupérées
            redevance.setText(cTextField.getText());  // Valeur récupérée depuis le TextField de redevance
            caisse.setText(rTextField.getText());  // Valeur récupérée depuis le TextField de caisse

        }

    }
    
    /* @FXML
    public void printBulletin() {
        // Récupérer les valeurs des labels
        String nomEmploye = nom.getText();
        String categorieEmploye = categorie.getText();
        String secteurEmploye = secteur.getText();
        String salaireBase = salBaseTable.getText();
        String redevanceValue = redevance.getText();
        String caisseValue = caisse.getText();
        String primeValue = prime.getText();
        String salaireNet = salNet.getText();
        String dateEmbaucheValue = dateEmbauche.getText();
        String dateRemiseValue = dateRemise.getText();
        String securiteValue = securite.getText();
        String nbAbsences = nbAbs.getText();

        // Créer un document imprimable
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob(printer);

        if (job != null && job.showPrintDialog(null)) {
            job.printPage(createPrintable(nomEmploye, categorieEmploye, secteurEmploye, salaireBase,
                    redevanceValue, caisseValue, primeValue, salaireNet,
                    dateEmbaucheValue, dateRemiseValue, securiteValue, nbAbsences));
            job.endJob();
        }
    }

    private Printable createPrintable(String nomEmploye, String categorieEmploye, String secteurEmploye,
            String salaireBase, String redevanceValue, String caisseValue,
            String primeValue, String salaireNet, String dateEmbaucheValue,
            String dateRemiseValue, String securiteValue, String nbAbsences) {
        return (Graphics g, PageFormat pageFormat, int pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            // Obtenir les coordonnées de la page
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            // Utilisation d'une police par défaut
            g.setFont(new Font("Serif", Font.PLAIN, 12));

            // Impression du titre
            g.drawString("BULLETIN DE PAIE", 200, 50);

            // Informations sur l'employé
            g.drawString("Nom : " + nomEmploye, 50, 100);
            g.drawString("Catégorie : " + categorieEmploye, 50, 120);
            g.drawString("Secteur : " + secteurEmploye, 50, 140);
            g.drawString("Salaire de base : " + salaireBase, 50, 160);
            g.drawString("Redevance : " + redevanceValue, 50, 180);
            g.drawString("Caisse : " + caisseValue, 50, 200);
            g.drawString("Prime mensuelle : " + primeValue, 50, 220);
            g.drawString("Salaire net : " + salaireNet, 50, 240);
            g.drawString("Absences : " + nbAbsences, 50, 260);

            // Informations sur la date d'embauche et de remise
            g.drawString("Date d'embauche : " + dateEmbaucheValue, 50, 280);
            g.drawString("Date de remise : " + dateRemiseValue, 50, 300);
            g.drawString("Sécurité sociale : " + securiteValue, 50, 320);

            // Retourner la page imprimée
            return Printable.PAGE_EXISTS;
        };
    }*/
}
