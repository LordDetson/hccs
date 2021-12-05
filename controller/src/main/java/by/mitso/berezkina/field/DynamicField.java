package by.mitso.berezkina.field;

import java.util.function.BiConsumer;
import java.util.function.Function;

import by.mitso.berezkina.domain.Field;

public class DynamicField implements Field {

    private final String name;
    private final String caption;
    private final Class<?> type;
    private final boolean required;
    private final Function<Object, ?> getter;
    private final BiConsumer<Object, Object> setter;

    public DynamicField(String name, String caption, Class<?> type, boolean required, Function<Object, ?> getter,
            BiConsumer<Object, Object> setter) {
        this.name = name;
        this.caption = caption;
        this.type = type;
        this.required = required;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public Object getValue(Object component) {
        return getter.apply(component);
    }

    @Override
    public void setValue(Object component, Object value) {
        setter.accept(component, value);
    }
}
