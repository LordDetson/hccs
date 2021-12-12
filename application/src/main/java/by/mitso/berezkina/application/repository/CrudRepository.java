package by.mitso.berezkina.application.repository;

import java.io.Serializable;
import java.util.Optional;

import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Persistent;

public interface CrudRepository<T extends Persistent<ID>, ID extends Serializable> extends Repository<T, ID> {

    long count();

    void delete(T entity);

    void deleteAll();

    void deleteAll(Iterable<? extends T> entities);

    void deleteAllById(Iterable<? extends ID> ids);

    void deleteById(ID id);

    boolean existsById(ID id);

    Iterable<T> findAll();

    Iterable<T> findAllById(Iterable<ID> ids);

    Optional<T> findById(ID id);

    Iterable<T> findAllByField(Field field, Object value);

    Optional<T> findByField(Field field, Object value);

    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    <S extends T> void refresh(S entity);
}
