package org.example.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fournisseurs")
public class Fournisseur extends User {

    @Column(name = "adresse", length = 200)
    private String adresse;

    public Fournisseur() {
        super();
    }

    public Fournisseur(String nomFournisseur, String adresseSociete) {
        super(nomFournisseur);
        this.adresse = adresseSociete;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresseSociete) {
        this.adresse = adresseSociete;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
