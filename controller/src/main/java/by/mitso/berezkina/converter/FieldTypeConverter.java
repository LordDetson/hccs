package by.mitso.berezkina.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.RoomType;
import by.mitso.berezkina.field.InputField.InputFieldType;

public class FieldTypeConverter implements Converter<Class<?>, InputFieldType>{

    @Override
    public InputFieldType convert(Class<?> type) {
        if(Number.class.isAssignableFrom(type)) {
            return InputFieldType.NUMBER;
        }
        else if(String.class.isAssignableFrom(type) ||
                RoomType.class.isAssignableFrom(type) ||
                Room.class.isAssignableFrom(type) ||
                Customer.class.isAssignableFrom(type)) {
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
        else if(Set.class.isAssignableFrom(type)) {
            return InputFieldType.RADIO;
        }
        throw new UnsupportedOperationException(type + " isn't supported");
    }
}
