package org.example.dao;

import org.example.entities.Produit;
import org.example.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO extends GenericDAO<Produit, Long> {

    public ProduitDAO() {
        super(Produit.class);
    }

    public List<Produit> findByCategorie(Long catId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Produit.findByCategorie", Produit.class)
                    .setParameter("catId", catId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produit> searchByLibelle(String term) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Produit.searchByLibelle", Produit.class)
                    .setParameter("term", term)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produit> searchDynamic(String filtreTerm, BigDecimal prixMin, BigDecimal prixMax) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Produit> cq = cb.createQuery(Produit.class);
            Root<Produit> root = cq.from(Produit.class);

            List<Predicate> predicates = new ArrayList<>();

            if (filtreTerm != null && !filtreTerm.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("libelle")), "%" + filtreTerm.toLowerCase().trim() + "%"));
            }

            if (prixMin != null) {
                predicates.add(cb.ge(root.get("prix"), prixMin));
            }

            if (prixMax != null) {
                predicates.add(cb.le(root.get("prix"), prixMax));
            }

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            cq.orderBy(cb.asc(root.get("id")));

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produit> findPage(int numeroPage, int taillePage) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Produit> query = em.createQuery("SELECT p FROM Produit p ORDER BY p.id", Produit.class);
            int start = numeroPage < 1 ? 0 : (numeroPage - 1) * taillePage;
            query.setFirstResult(start);
            query.setMaxResults(taillePage);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long countAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Produit p", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}
