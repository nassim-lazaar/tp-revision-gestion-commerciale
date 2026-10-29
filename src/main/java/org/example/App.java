package org.example;

import org.example.dao.GenericDAO;
import org.example.dao.ProduitDAO;
import org.example.entities.*;
import org.example.service.CrudService;
import org.example.util.JPAUtil;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("   LAB 407 : REVISION GESTION COMMERCIALE (HIBERNATE 5.6 & JPA 2.2)     ");
        System.out.println("========================================================================");

        try {
            // Initialisation des DAOs et Services
            GenericDAO<Categorie, Long> categorieDAO = new GenericDAO<>(Categorie.class);
            GenericDAO<User, Long> userDAO = new GenericDAO<>(User.class);
            GenericDAO<Commande, Long> commandeDAO = new GenericDAO<>(Commande.class);
            ProduitDAO produitDAO = new ProduitDAO();

            CrudService<Categorie, Long> categorieService = new CrudService<>(categorieDAO);

            // 1. Creation des Categories & Produits
            System.out.println("\n[1] CREATION DES CATEGORIES ET PRODUITS ASSOCIES");
            System.out.println("------------------------------------------------------------------------");
            Categorie catInformatique = new Categorie("Informatique");
            Categorie catBureautique = new Categorie("Papeterie & Fournitures");
            Categorie catMobilier = new Categorie("Mobilier de Bureau");

            categorieService.create(catInformatique);
            categorieService.create(catBureautique);
            categorieService.create(catMobilier);

            Produit pClavier = new Produit("Clavier Mecanique RGB", new BigDecimal("79.99"), LocalDate.now(), catInformatique);
            Produit pSouris = new Produit("Souris Ergonomique Sans Fil", new BigDecimal("49.50"), LocalDate.now(), catInformatique);
            Produit pStylo = new Produit("Stylo Plume Luxe", new BigDecimal("15.00"), LocalDate.now(), catBureautique);
            Produit pCarnet = new Produit("Carnet Notes Cuir A5", new BigDecimal("12.90"), LocalDate.now(), catBureautique);
            Produit pBureau = new Produit("Bureau Assis-Debout Electrique", new BigDecimal("349.00"), LocalDate.now(), catMobilier);

            produitDAO.create(pClavier);
            produitDAO.create(pSouris);
            produitDAO.create(pStylo);
            produitDAO.create(pCarnet);
            produitDAO.create(pBureau);

            System.out.println("Categories et 5 produits persistes avec succes.");

            // 2. Hierarchie d'heritage JOINED (User -> Client, Fournisseur)
            System.out.println("\n[2] GESTION DE L'HERITAGE JOINED (USERS / CLIENTS / FOURNISSEURS)");
            System.out.println("------------------------------------------------------------------------");
            Client clientNassim = new Client("Nassim Lazaar", LocalDate.of(2025, 1, 15));
            Client clientAmine = new Client("Amine Mansouri", LocalDate.of(2025, 6, 20));
            Fournisseur fournisseurDell = new Fournisseur("Dell Technologies France", "1 Rond-point Benjamin Franklin, Montpellier");
            Fournisseur fournisseurClairefontaine = new Fournisseur("Papeteries Clairefontaine", "Etival-Clairefontaine");

            userDAO.create(clientNassim);
            userDAO.create(clientAmine);
            userDAO.create(fournisseurDell);
            userDAO.create(fournisseurClairefontaine);

            List<User> allUsers = userDAO.findAll();
            System.out.println("Lecture polymorphique de tous les utilisateurs (" + allUsers.size() + ") :");
            for (User u : allUsers) {
                if (u instanceof Client) {
                    Client c = (Client) u;
                    System.out.println("  -> [CLIENT] ID=" + c.getId() + " | Nom=" + c.getNom() + " | Inscrit le=" + c.getDateInscription());
                } else if (u instanceof Fournisseur) {
                    Fournisseur f = (Fournisseur) u;
                    System.out.println("  -> [FOURNISSEUR] ID=" + f.getId() + " | Nom=" + f.getNom() + " | Adresse=" + f.getAdresse());
                }
            }

            // 3. Commandes et table associative avec cle composee (@EmbeddedId CommandeProduitPK)
            System.out.println("\n[3] COMMANDES ET ASSOCIATION N:N AVEC CLE COMPOSEE");
            System.out.println("------------------------------------------------------------------------");
            EntityManager em = JPAUtil.getEntityManager();
            em.getTransaction().begin();

            Commande commande1 = new Commande(LocalDate.now());
            em.persist(commande1);

            Produit managedClavier = em.find(Produit.class, pClavier.getId());
            Produit managedSouris = em.find(Produit.class, pSouris.getId());

            commande1.addProduit(managedClavier, 2);
            commande1.addProduit(managedSouris, 1);

            em.getTransaction().commit();
            em.close();

            System.out.println("Commande ID=" + commande1.getId() + " persistee avec " + commande1.getLignes().size() + " ligne(s) d'articles.");

            // 4. Test des Named Queries
            System.out.println("\n[4] REQUETES AVANCEES : NAMED QUERIES");
            System.out.println("------------------------------------------------------------------------");
            List<Produit> prodsInformatique = produitDAO.findByCategorie(catInformatique.getId());
            System.out.println("Produits de la categorie 'Informatique' (NamedQuery findByCategorie) :");
            prodsInformatique.forEach(p -> System.out.println("  -> " + p.getLibelle() + " (" + p.getPrix() + " EUR)"));

            List<Produit> rechercheStylo = produitDAO.searchByLibelle("stylo");
            System.out.println("Recherche par terme 'stylo' (NamedQuery searchByLibelle) : " + rechercheStylo.size() + " resultat(s)");
            rechercheStylo.forEach(p -> System.out.println("  -> " + p.getLibelle()));

            // 5. Test Criteria API dynamique
            System.out.println("\n[5] REQUETES DYNAMIQUES MULTI-CRITERES (CRITERIA API)");
            System.out.println("------------------------------------------------------------------------");
            BigDecimal maxPrice = new BigDecimal("80.00");
            List<Produit> critResults = produitDAO.searchDynamic(null, new BigDecimal("40.00"), maxPrice);
            System.out.println("Produits entre 40 EUR et 80 EUR :");
            critResults.forEach(p -> System.out.println("  -> " + p.getLibelle() + " | Prix=" + p.getPrix() + " EUR"));

            // 6. Test Pagination serveur
            System.out.println("\n[6] TEST DE PAGINATION SERVEUR (PAGE SIZE = 2)");
            System.out.println("------------------------------------------------------------------------");
            long totalArticles = produitDAO.countAll();
            int pageSize = 2;
            int totalPages = (int) Math.ceil((double) totalArticles / pageSize);

            System.out.println("Total articles : " + totalArticles + " | Nombre total de pages : " + totalPages);
            List<Produit> page1 = produitDAO.findPage(1, pageSize);
            System.out.println("Page 1 :");
            page1.forEach(p -> System.out.println("  [P1] ID=" + p.getId() + " - " + p.getLibelle()));

            List<Produit> page2 = produitDAO.findPage(2, pageSize);
            System.out.println("Page 2 :");
            page2.forEach(p -> System.out.println("  [P2] ID=" + p.getId() + " - " + p.getLibelle()));

            System.out.println("\n========================================================================");
            System.out.println("   VALIDATION TERMINEE AVEC SUCCES : LAB 407 INTEGRALEMENT OPERATIONNEL ");
            System.out.println("========================================================================");

        } finally {
            JPAUtil.shutdown();
        }
    }
}
