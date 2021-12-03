package by.mitso.berezkina.domain;

public interface Field {

    String getName();

    String getCaption();

    Class<?> getType();

    Object getValue(Object component);

    void setValue(Object component, Object value);
}
