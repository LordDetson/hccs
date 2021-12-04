package by.mitso.berezkina.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.field.InputField;
import by.mitso.berezkina.field.InputField.InputFieldType;

public class FieldConverter implements Converter<Field, InputField> {

    @Override
    public InputField convert(Field field) {
        InputFieldType type = convertToFormType(field.getType());
        String caption = StringUtils.capitalize(field.getCaption());
        return new InputField(field.getName(), caption, type, field.isRequired());
    }

    private InputFieldType convertToFormType(Class<?> type) {
        if(Number.class.isAssignableFrom(type)) {
            return InputFieldType.NUMBER;
        }
        else if(String.class.isAssignableFrom(type)) {
            return InputFieldType.TEXT;
        }
        else if(LocalDate.class.isAssignableFrom(type)) {
            return InputFieldType.DATE;
        }
        else if(LocalTime.class.isAssignableFrom(type)) {
            return InputFieldType.TIME;
        }
        else if(LocalDateTime.class.isAssignableFrom(type)) {
            return InputFieldType.DATE_TIME;
        }
        throw new UnsupportedOperationException(type + "");
    }
}
