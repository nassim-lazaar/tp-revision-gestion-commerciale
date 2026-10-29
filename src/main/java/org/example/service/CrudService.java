package org.example.service;

import org.example.dao.GenericDAO;

import java.io.Serializable;
import java.util.List;

public class CrudService<T, ID extends Serializable> {

    protected final GenericDAO<T, ID> dao;

    public CrudService(GenericDAO<T, ID> dao) {
        this.dao = dao;
    }

    public T create(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("L'entité à créer ne peut pas être null.");
        }
        return dao.create(entity);
    }

    public T update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("L'entité à mettre à jour ne peut pas être null.");
        }
        return dao.update(entity);
    }

    public void delete(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("L'entité à supprimer ne peut pas être null.");
        }
        dao.delete(entity);
    }

    public T getById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être null.");
        }
        return dao.find(id);
    }

    public List<T> getAll() {
        return dao.findAll();
    }
}
