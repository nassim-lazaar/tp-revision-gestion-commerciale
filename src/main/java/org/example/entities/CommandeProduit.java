package org.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "commande_produit")
public class CommandeProduit {

    @EmbeddedId
    private CommandeProduitPK id = new CommandeProduitPK();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commandeId")
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(nullable = false)
    private int quantite;

    public CommandeProduit() {
    }

    public CommandeProduit(Commande commandeLiee, Produit produitLie, int qteCommandee) {
        this.commande = commandeLiee;
        this.produit = produitLie;
        this.quantite = qteCommandee;
        if (commandeLiee != null && commandeLiee.getId() != null && produitLie != null && produitLie.getId() != null) {
            this.id = new CommandeProduitPK(commandeLiee.getId(), produitLie.getId());
        }
    }

    public CommandeProduitPK getId() {
        return id;
    }

    public void setId(CommandeProduitPK idCleComposee) {
        this.id = idCleComposee;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commandeLiee) {
        this.commande = commandeLiee;
        if (commandeLiee != null && commandeLiee.getId() != null) {
            this.id.setCommandeId(commandeLiee.getId());
        }
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produitLie) {
        this.produit = produitLie;
        if (produitLie != null && produitLie.getId() != null) {
            this.id.setProduitId(produitLie.getId());
        }
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int qteCommandee) {
        this.quantite = qteCommandee;
    }

    @Override
    public String toString() {
        return "CommandeProduit{" +
                "id=" + id +
                ", quantite=" + quantite +
                '}';
    }
}
