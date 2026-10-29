package org.example.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "clients")
public class Client extends User {

    @Column(name = "date_inscription")
    private LocalDate dateInscription;

    public Client() {
        super();
    }

    public Client(String nomClient, LocalDate inscriptionDate) {
        super(nomClient);
        this.dateInscription = inscriptionDate;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate inscriptionDate) {
        this.dateInscription = inscriptionDate;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", dateInscription=" + dateInscription +
                '}';
    }
}
