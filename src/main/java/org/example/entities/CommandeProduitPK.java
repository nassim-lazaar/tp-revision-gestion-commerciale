package org.example.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommandeProduitPK implements Serializable {

    @Column(name = "commande_id")
    private Long commandeId;

    @Column(name = "produit_id")
    private Long produitId;

    public CommandeProduitPK() {
    }

    public CommandeProduitPK(Long idCommande, Long idProduit) {
        this.commandeId = idCommande;
        this.produitId = idProduit;
    }

    public Long getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(Long idCommande) {
        this.commandeId = idCommande;
    }

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long idProduit) {
        this.produitId = idProduit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandeProduitPK that = (CommandeProduitPK) o;
        return Objects.equals(commandeId, that.commandeId) &&
               Objects.equals(produitId, that.produitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandeId, produitId);
    }

    @Override
    public String toString() {
        return "CommandeProduitPK{" +
                "commandeId=" + commandeId +
                ", produitId=" + produitId +
                '}';
    }
}
