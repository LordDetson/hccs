package by.mitso.berezkina.field;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import by.mitso.berezkina.converter.Converter;
import by.mitso.berezkina.converter.FieldConverter;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Room.RoomField;
import by.mitso.berezkina.domain.RoomType.RoomTypeField;
import jakarta.servlet.http.HttpServletRequest;

public class FieldUtil {

    private static final Converter<Field, InputField> FIELD_CONVERTER = new FieldConverter();

    public static Set<InputField> convertToInputFields(Collection<? extends Field> fields) {
        return fields.stream()
                .map(FieldUtil::convertToInputField)
                .collect(LinkedHashSet::new, Collection::add, Collection::addAll);
    }

    public static InputField convertToInputField(Field field) {
        return FIELD_CONVERTER.convert(field);
    }

    public static Set<InputField> getRoomTypeOrderedInputFields() {
        Set<RoomTypeField> orderedFields = new LinkedHashSet<>();
        orderedFields.add(RoomTypeField.NAME);
        orderedFields.add(RoomTypeField.DESCRIPTION);
        orderedFields.add(RoomTypeField.MIN_PEOPLE);
        orderedFields.add(RoomTypeField.MAX_PEOPLE);
        orderedFields.add(RoomTypeField.MIN_BEDS);
        orderedFields.add(RoomTypeField.MAX_BEDS);
        return FieldUtil.convertToInputFields(orderedFields);
    }

    public static Set<InputField> getRoomOrderedInputFields() {
        Set<RoomField> orderedFields = new LinkedHashSet<>();
        orderedFields.add(RoomField.NUMBER);
        orderedFields.add(RoomField.DESCRIPTION);
        return FieldUtil.convertToInputFields(orderedFields);
    }

    public static Map<Field, Object> createFieldValueMap(Set<? extends Field> fields, HttpServletRequest req) {
        Map<Field, Object> fieldValueMap = new HashMap<>();
        Enumeration<String> parameterNames = req.getParameterNames();
        while(parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            fields.stream()
                    .filter(field -> field.getName().equals(parameterName))
                    .findFirst().ifPresent(field -> fieldValueMap.put(field, mapStringToCorrectType(field, req.getParameter(parameterName))));
        }
        return fieldValueMap;
    }

    private static Object mapStringToCorrectType(Field field, String value) {
        Class<?> type = field.getType();
        if(type.isAssignableFrom(Byte.class)) {
            return Byte.valueOf(value);
        }
        else if(type.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        }
        return value;
    }
}
