package by.mitso.berezkina.field;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.RoomType.RoomTypeField;
import jakarta.servlet.http.HttpServletRequest;

public class FieldUtil {

    public static List<FormField> convertToFormFields(Collection<? extends Field> fields) {
        return fields.stream()
                .map(FieldUtil::convertToFromField)
                .collect(Collectors.toList());
    }

    public static FormField convertToFromField(Field field) {
        Class<?> type = field.getType();
        String formType;
        if(Number.class.isAssignableFrom(type)) {
            formType = "number";
        }
        else {
            formType = "text";
        }
        String caption = StringUtils.capitalize(field.getCaption());
        return new FormField(field.getName(), caption, formType);
    }

    public static List<FormField> getRoomTypeOrderedFormFields() {
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
