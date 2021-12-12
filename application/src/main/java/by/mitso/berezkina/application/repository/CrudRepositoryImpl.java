package by.mitso.berezkina.application.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import by.mitso.berezkina.domain.Field;
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
        List<T> found = entityManager.createQuery(cq).getResultList();
        found.forEach(this::refresh);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        List<T> entities = new ArrayList<>();
        for(ID id : ids) {
            findById(id).ifPresent(entities::add);
        }
        entities.forEach(this::refresh);
        return entities;
    }

    @Override
    public Optional<T> findById(ID id) {
        Optional<T> found = Optional.ofNullable(entityManager.find(entityType, id));
        found.ifPresent(this::refresh);
        return found;
    }

    @Override
    public Iterable<T> findAllByField(Field field, Object value) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = qb.createQuery(entityType);
        Root<T> root = cq.from(entityType);
        cq.select(root).where(qb.equal(root.get(field.getName()), value));
        List<T> found = entityManager.createQuery(cq).getResultList();
        found.forEach(this::refresh);
        return found;
    }

    @Override
    public Optional<T> findByField(Field field, Object value) {
        Iterator<T> iterator = findAllByField(field, value).iterator();
        if(iterator.hasNext()) {
            T first = iterator.next();
            refresh(first);
            return Optional.of(first);
        }
        return Optional.empty();
    }

    @Override
    public <S extends T> S save(S entity) {
        entityManager.getTransaction().begin();
        if(entity.getId() == null) {
            entityManager.persist(entity);
        }
        else {
            entityManager.merge(entity);
        }
        entityManager.getTransaction().commit();
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        entityManager.getTransaction().begin();
        int flushCounter = 0;
        for(S entity : entities) {
            if(entity.getId() == null) {
                entityManager.persist(entity);
            }
            else {
                entityManager.merge(entity);
            }
            if(++flushCounter == MAX_COUNT_IN_BATCH) {
                entityManager.flush();
                entityManager.clear();
                flushCounter = 0;
            }
        }
        entityManager.getTransaction().commit();
        return entities;
    }

    @Override
    public <S extends T> void refresh(S entity) {
        entityManager.refresh(entity);
    }
}
