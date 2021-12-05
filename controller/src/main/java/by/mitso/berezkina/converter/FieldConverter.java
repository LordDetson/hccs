package by.mitso.berezkina.converter;

import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.field.InputField;

public class FieldConverter implements Converter<Field, InputField> {

    @Override
    public InputField convert(Field field) {
        return new InputField(field);
    }
}
