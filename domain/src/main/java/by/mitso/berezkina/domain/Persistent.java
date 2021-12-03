package by.mitso.berezkina.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import by.mitso.berezkina.domain.exception.DomainChecker;

@MappedSuperclass
public abstract class Persistent<ID extends Serializable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        DomainChecker.checkNotNull(id, "id");
        this.id = id;
    }

    public Object getFieldValue(Field field) {
        return field.getValue(this);
    }

    public void setFieldValue(Field field, Object value) {
        field.setValue(this, value);
    }

    public void setFieldValue(Map<Field, ?> fieldValueMap) {
        fieldValueMap.forEach(this::setFieldValue);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Persistent<?> that = (Persistent<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
