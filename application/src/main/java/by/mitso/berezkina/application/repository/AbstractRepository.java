package by.mitso.berezkina.application.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

import by.mitso.berezkina.domain.Persistent;

public abstract class AbstractRepository<T extends Persistent<ID>, ID extends Serializable> implements Repository<T, ID> {

    protected final EntityManager entityManager;
    protected final Class<T> entityType;

    protected AbstractRepository(EntityManager entityManager, Class<T> entityType) {
        this.entityManager = entityManager;
        this.entityType = entityType;
    }

    @Override
    public Class<? extends T> getEntityType() {
        return entityType;
    }
}
