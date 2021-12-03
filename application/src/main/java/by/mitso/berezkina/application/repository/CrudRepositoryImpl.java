package by.mitso.berezkina.application.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import by.mitso.berezkina.domain.Persistent;

public class CrudRepositoryImpl<T extends Persistent<ID>, ID extends Serializable>
        extends AbstractRepository<T, ID>
        implements CrudRepository<T, ID> {

    private static final int MAX_COUNT_IN_BATCH = 100;

    public CrudRepositoryImpl(EntityManager entityManager, Class<T> entityType) {
        super(entityManager, entityType);
    }

    @Override
    public long count() {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(entityType)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public void delete(T entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteAll() {
        deleteAll(findAll());
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        entityManager.getTransaction().begin();
        int flushCounter = 0;
        for(T entity : entities) {
            entityManager.remove(entity);
            if(++flushCounter == MAX_COUNT_IN_BATCH) {
                entityManager.flush();
                entityManager.clear();
                flushCounter = 0;
            }
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {

    }

    @Override
    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public Iterable<T> findAll() {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = qb.createQuery(entityType);
        cq.select(cq.from(entityType));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        List<T> entities = new ArrayList<>();
        for(ID id : ids) {
            findById(id).ifPresent(entities::add);
        }
        return entities;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entityManager.find(entityType, id));
    }

    @Override
    public <S extends T> S save(S entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return null;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        entityManager.getTransaction().begin();
        int flushCounter = 0;
        for(S entity : entities) {
            entityManager.persist(entity);
            if(++flushCounter == MAX_COUNT_IN_BATCH) {
                entityManager.flush();
                entityManager.clear();
                flushCounter = 0;
            }
        }
        entityManager.getTransaction().commit();
        return null;
    }
}
