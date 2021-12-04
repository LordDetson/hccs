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
import by.mitso.berezkina.domain.RoomType.RoomTypeField;
import jakarta.servlet.http.HttpServletRequest;

public class FieldUtil {

    private static final Converter<Field, InputField> FIELD_CONVERTER = new FieldConverter();

    public static Set<InputField> convertToFormFields(Collection<? extends Field> fields) {
        return fields.stream()
                .map(FieldUtil::convertToFormField)
                .collect(LinkedHashSet::new, Collection::add, Collection::addAll);
    }

    public static InputField convertToFormField(Field field) {
        return FIELD_CONVERTER.convert(field);
    }

    public static Set<InputField> getRoomTypeOrderedFormFields() {
        Set<RoomTypeField> formFields = new LinkedHashSet<>();
        formFields.add(RoomTypeField.NAME);
        formFields.add(RoomTypeField.DESCRIPTION);
        formFields.add(RoomTypeField.MIN_PEOPLE);
        formFields.add(RoomTypeField.MAX_PEOPLE);
        formFields.add(RoomTypeField.MIN_BEDS);
        formFields.add(RoomTypeField.MAX_BEDS);
        return FieldUtil.convertToFormFields(formFields);
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
            return Byte.parseByte(value);
        }
        return value;
    }
}
