package by.mitso.berezkina.field;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.converter.FieldTypeConverter;
import by.mitso.berezkina.domain.Field;

public class InputField {

    private static final FieldTypeConverter FIELD_TYPE_CONVERTER = new FieldTypeConverter();

    private final Field field;
    private final InputFieldType type;
    private boolean required;
    private boolean readonly;
    private Set<String> values;
    private String selectedValue;

    public enum InputFieldType {
        TEXT("text"),
        NUMBER("number"),
        DATE("date"),
        TIME("time"),
        DATE_TIME("datetime"),
        RADIO("radio"),
        ;

        private final String name;

        InputFieldType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public InputField(Field field) {
        this.field = field;
        this.type = FIELD_TYPE_CONVERTER.convert(field.getType());
        this.required = field.isRequired();
        this.values = Collections.emptySet();
    }

    public String getName() {
        return field.getName();
    }

    public String getCaption() {
        return StringUtils.capitalize(field.getCaption());
    }

    public InputFieldType getType() {
        return type;
    }

    public void setRequired(boolean required) {
        if(required) {
            readonly = false;
        }
        this.required = required;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        if(readonly) {
            required = false;
        }
        this.readonly = readonly;
    }

    public Set<String> getValues() {
        return values;
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }

    public void setValue(String values) {
        setValues(Collections.singleton(values));
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public boolean isRequired() {
        return field.isRequired();
    }

    public Field getField() {
        return field;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        InputField that = (InputField) o;
        return field.equals(that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
