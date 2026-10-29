package org.example;

import org.example.dao.GenericDAO;
import org.example.dao.ProduitDAO;
import org.example.entities.*;
import org.example.service.CrudService;
import org.example.util.JPAUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class CommercialManagementTest {

    private GenericDAO<Categorie, Long> categorieDAO;
    private ProduitDAO produitDAO;
    private CrudService<Categorie, Long> categorieService;
    private GenericDAO<User, Long> userDAO;

    @Before
    public void setUp() {
        categorieDAO = new GenericDAO<>(Categorie.class);
        produitDAO = new ProduitDAO();
        categorieService = new CrudService<>(categorieDAO);
        userDAO = new GenericDAO<>(User.class);
    }

    @AfterClass
    public static void tearDown() {
        JPAUtil.shutdown();
    }

    @Test
    public void testCreateCategorieAndProduit() {
        Categorie cat = new Categorie("Papeterie Test");
        categorieService.create(cat);
        assertNotNull(cat.getId());

        Produit prod = new Produit("Cahier Test", new BigDecimal("4.50"), LocalDate.now(), cat);
        produitDAO.create(prod);
        assertNotNull(prod.getId());

        Produit retrieved = produitDAO.find(prod.getId());
        assertNotNull(retrieved);
        assertEquals("Cahier Test", retrieved.getLibelle());
        assertEquals(cat.getId(), retrieved.getCategorie().getId());
    }

    @Test
    public void testUserInheritanceJoined() {
        Client client = new Client("Client Dupont", LocalDate.now());
        Fournisseur fournisseur = new Fournisseur("Fournisseur ABC", "Paris");

        userDAO.create(client);
        userDAO.create(fournisseur);

        User foundClient = userDAO.find(client.getId());
        assertTrue(foundClient instanceof Client);
        assertEquals("Client Dupont", foundClient.getNom());

        User foundFournisseur = userDAO.find(fournisseur.getId());
        assertTrue(foundFournisseur instanceof Fournisseur);
        assertEquals("Paris", ((Fournisseur) foundFournisseur).getAdresse());
    }

    @Test
    public void testNamedQueries() {
        Categorie cat = new Categorie("Informatique Test");
        categorieService.create(cat);

        Produit p1 = new Produit("Ecran 24 pouces", new BigDecimal("150.00"), LocalDate.now(), cat);
        Produit p2 = new Produit("Souris Optique", new BigDecimal("20.00"), LocalDate.now(), cat);
        produitDAO.create(p1);
        produitDAO.create(p2);

        List<Produit> byCat = produitDAO.findByCategorie(cat.getId());
        assertTrue(byCat.size() >= 2);

        List<Produit> search = produitDAO.searchByLibelle("ecran");
        assertFalse(search.isEmpty());
        assertTrue(search.get(0).getLibelle().toLowerCase().contains("ecran"));
    }

    @Test
    public void testDynamicSearchAndPagination() {
        List<Produit> results = produitDAO.searchDynamic(null, new BigDecimal("10.00"), new BigDecimal("200.00"));
        assertNotNull(results);

        List<Produit> page = produitDAO.findPage(1, 2);
        assertTrue(page.size() <= 2);
    }
}
