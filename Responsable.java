/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MIGUEL PYTHON
 */
public class Responsable {
    private String idRes;
    private String nomRes;
    private String prenomRes;
    private String email;
    private String passRes;
    private List<BulletinPaie> bulletinsCrees;

    public Responsable(String idRes, String nomRes, String prenomRes, String email, String passRes) {
        this.idRes = idRes;
        this.nomRes = nomRes;
        this.prenomRes = prenomRes;
        this.email = email;
        this.passRes = passRes;
        bulletinsCrees = new ArrayList<>();
    }
    
    //Getters

    public String getIdRes() {
        return idRes;
    }

    public String getNomRes() {
        return nomRes;
    }

    public String getPrenomRes() {
        return prenomRes;
    }

    public String getEmail() {
        return email;
    }

    public String getPassRes() {
        return passRes;
    }

    public List<BulletinPaie> getBulletinsCrees() {
        return bulletinsCrees;
    }
    
    //Setters

    public void setNomRes(String nomRes) {
        if(nomRes == null)
            throw new IllegalArgumentException("Veillez donnez un nom");
        this.nomRes = nomRes;
    }

    public void setPrenomRes(String prenomRes) {
        if(prenomRes == null)
            throw new IllegalArgumentException("Veillez donner un prenom");
        this.prenomRes = prenomRes;
    }

    public void setEmail(String email) {
        if(email == null)
            throw new IllegalArgumentException("Veillez entrer una adresse mail");
        this.email = email;
    }
    
    
    
}
