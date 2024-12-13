/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.Date;
import javafx.beans.property.*;

/**
 *
 * @author MIGUEL PYTHON
 */
public class BulletinPaie {
    

    private final SimpleIntegerProperty idBul;
    private final SimpleStringProperty dateRemise;
    private final SimpleStringProperty nomSociete;
    private final SimpleStringProperty adresse;
    private final SimpleIntegerProperty idEm;
    private final SimpleIntegerProperty nbAbs;
    private final SimpleStringProperty prime;

    public BulletinPaie(int idBul, String dateRemise, String nomSociete, String adresse, int idEm,int nbAbs,String prime) {
        this.idBul = new SimpleIntegerProperty(idBul);
        this.dateRemise = new SimpleStringProperty(dateRemise);
        this.nomSociete = new SimpleStringProperty(nomSociete);
        this.adresse = new SimpleStringProperty(adresse);
        this.idEm = new SimpleIntegerProperty(idEm);
        this.nbAbs = new SimpleIntegerProperty(nbAbs);
        this.prime = new SimpleStringProperty(prime);
    }

    public int getIdBul() {
        return idBul.get();
    }

    public String getDateRemise() {
        return dateRemise.get();
    }

    public String getNomSociete() {
        return nomSociete.get();
    }

    public String getAdresse() {
        return adresse.get();
    }

    public int getIdEm() {
        return idEm.get();
    }
    // Les propriétés observables pour JavaFX
    public SimpleIntegerProperty idBulProperty() { return idBul; }
    public SimpleStringProperty dateRemiseProperty() { return dateRemise; }
    public SimpleStringProperty nomSocieteProperty() { return nomSociete; }
    public SimpleStringProperty adresseProperty() { return adresse; }
    public SimpleIntegerProperty idEmProperty() { return idEm; }
    public SimpleIntegerProperty nbAbsProperty() { return nbAbs; }
    public SimpleStringProperty primeProperty() { return prime; }
}
