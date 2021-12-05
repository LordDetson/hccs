package by.mitso.berezkina.field;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.converter.FieldTypeConverter;
import by.mitso.berezkina.domain.Field;

public class InputField {

    private static final FieldTypeConverter FIELD_TYPE_CONVERTER = new FieldTypeConverter();

    private final Field field;
    private final InputFieldType type;
    private String value;

    public enum InputFieldType {
        TEXT("text"),
        NUMBER("number"),
        DATE("date"),
        TIME("time"),
        DATE_TIME("datetime"),
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
        this.value = "";
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
