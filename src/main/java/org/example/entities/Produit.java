package org.example.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produits")
@NamedQueries({
        @NamedQuery(name = "Produit.findAll", query = "SELECT p FROM Produit p"),
        @NamedQuery(name = "Produit.findByCategorie", query = "SELECT p FROM Produit p WHERE p.categorie.id = :catId"),
        @NamedQuery(name = "Produit.searchByLibelle", query = "SELECT p FROM Produit p WHERE LOWER(p.libelle) LIKE LOWER(CONCAT('%', :term, '%'))")
})
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String libelle;

    @Column(precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    public Produit() {
    }

    public Produit(String nomArticle, BigDecimal prixUnitaire, LocalDate creationDate, Categorie categorieAppartenance) {
        this.libelle = nomArticle;
        this.prix = prixUnitaire;
        this.dateCreation = creationDate;
        this.categorie = categorieAppartenance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idArticle) {
        this.id = idArticle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String nomArticle) {
        this.libelle = nomArticle;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prixUnitaire) {
        this.prix = prixUnitaire;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate creationDate) {
        this.dateCreation = creationDate;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorieAppartenance) {
        this.categorie = categorieAppartenance;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", prix=" + prix +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
