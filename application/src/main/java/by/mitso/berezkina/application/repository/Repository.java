package by.mitso.berezkina.application.repository;

import java.io.Serializable;

import by.mitso.berezkina.domain.Persistent;

public interface Repository<T extends Persistent<ID>, ID extends Serializable> {

    Class<? extends T> getEntityType();
}
