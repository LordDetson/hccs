package by.mitso.berezkina.field;

import java.util.Objects;

public class InputField {

    private final String name;
    private final String caption;
    private final InputFieldType type;
    private final boolean required;

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

    public InputField(String name, String caption, InputFieldType type, boolean required) {
        this.name = name;
        this.caption = caption;
        this.type = type;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    public InputFieldType getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
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
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "InputField{" +
                "name='" + name + '\'' +
                '}';
    }
}
