package org.example.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_commande")
    private LocalDate dateCommande;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeProduit> lignes = new ArrayList<>();

    public Commande() {
    }

    public Commande(LocalDate datePassation) {
        this.dateCommande = datePassation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idCommande) {
        this.id = idCommande;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate datePassation) {
        this.dateCommande = datePassation;
    }

    public List<CommandeProduit> getLignes() {
        return lignes;
    }

    public void setLignes(List<CommandeProduit> lignesCommande) {
        this.lignes = lignesCommande;
    }

    public void addProduit(Produit article, int quantite) {
        CommandeProduit ligne = new CommandeProduit(this, article, quantite);
        this.lignes.add(ligne);
    }

    public void removeLigne(CommandeProduit ligne) {
        this.lignes.remove(ligne);
        ligne.setCommande(null);
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", dateCommande=" + dateCommande +
                ", totalLignes=" + (lignes != null ? lignes.size() : 0) +
                '}';
    }
}
