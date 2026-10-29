package org.example.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produit> produits = new ArrayList<>();

    public Categorie() {
    }

    public Categorie(String nomCategorie) {
        this.nom = nomCategorie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idCategorie) {
        this.id = idCategorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nomCategorie) {
        this.nom = nomCategorie;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> listeProduits) {
        this.produits = listeProduits;
    }

    public void addProduit(Produit produitAssocie) {
        if (produitAssocie != null) {
            this.produits.add(produitAssocie);
            produitAssocie.setCategorie(this);
        }
    }

    public void removeProduit(Produit produitDissocie) {
        if (produitDissocie != null) {
            this.produits.remove(produitDissocie);
            produitDissocie.setCategorie(null);
        }
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", totalProduits=" + (produits != null ? produits.size() : 0) +
                '}';
    }
}
