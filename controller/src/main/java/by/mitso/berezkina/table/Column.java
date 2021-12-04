package by.mitso.berezkina.table;

import java.util.Comparator;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Persistent;

public class Column {

    private Field field;
    private String caption;
    private Class<?> classType;
    private Comparator<?> comparator;
    private Function<Object, Object> valueGetter;

    public Column(Field field) {
        setField(field);
    }

    public Object getData(Object object) {
        if(valueGetter != null) {
            return valueGetter.apply(object);
        }
        else if(object instanceof Persistent) {
            return ((Persistent<?>) object).getFieldValue(field);
        }
        assert false : "cannot get value from " + object;
        return null;
    }

    public Field getField() {
        return field;
    }

    public Column setField(Field field) {
        this.field = field;
        this.caption = StringUtils.capitalize(field.getCaption());
        this.classType = field.getType();
        return this;
    }

    public Class<?> getType() {
        return classType;
    }

    public Column setType(Class<?> aClass) {
        this.classType = aClass;
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public Column setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public String getName() {
        return field.getName();
    }

    public Comparator<?> getComparator() {
        return comparator;
    }

    public boolean hasComparator() {
        return comparator != null;
    }

    public Column setComparator(Comparator<?> comparator) {
        this.comparator = comparator;
        return this;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + field.getName() + '\'' +
                '}';
    }

    public Column setValueGetter(Function<Object, Object> valueGetter) {
        this.valueGetter = valueGetter;
        return this;
    }
}