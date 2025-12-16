package org.example.repository;

import jakarta.persistence.EntityManagerFactory;

public abstract class BaseRepository <T, ID> {

    private final EntityManagerFactory emf;
    protected final Class <T> entityClass;

    protected BaseRepository(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }
}
