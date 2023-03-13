package modele;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {

    private int UserId;

    private String nom;
    private String prenom;
    private String adresse;
    private String numeroTelephone;
    private String photoDeProfil;

    public User() {
        this.UserId++;
    }

    public User(int userId, String nom, String prenom, String adresse, String numeroTelephone, String photoDeProfil) {
        UserId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.numeroTelephone = numeroTelephone;
        this.photoDeProfil = photoDeProfil;
    }

    public User(int userId, String nom, String prenom, String adresse, String numeroTelephone) {
        UserId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.numeroTelephone = numeroTelephone;
    }
    public User(String nom, String prenom, String adresse, String numeroTelephone, String photoDeProfil) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.numeroTelephone = numeroTelephone;
        this.photoDeProfil = photoDeProfil;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getPhotoDeProfil() {
        return photoDeProfil;
    }

    public void setPhotoDeProfil(String photoDeProfil) {
        this.photoDeProfil = photoDeProfil;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserId=" + UserId +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", numeroTelephone='" + numeroTelephone + '\'' +
                ", photoDeProfil=" + photoDeProfil +
                '}';
    }
}
