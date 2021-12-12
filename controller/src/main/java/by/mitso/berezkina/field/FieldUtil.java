package by.mitso.berezkina.field;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import by.mitso.berezkina.converter.Converter;
import by.mitso.berezkina.converter.FieldConverter;
import by.mitso.berezkina.domain.Customer.CustomerField;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Gender;
import by.mitso.berezkina.domain.Room.RoomField;
import by.mitso.berezkina.domain.RoomAssignment.RoomAssignmentField;
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

    public static Set<InputField> getMainRoomOrderedInputFields() {
        Set<RoomField> orderedFields = new LinkedHashSet<>();
        orderedFields.add(RoomField.NUMBER);
        orderedFields.add(RoomField.DESCRIPTION);
        return FieldUtil.convertToInputFields(orderedFields);
    }

    public static Set<InputField> getAllRoomOrderedInputFields() {
        Set<RoomField> orderedFields = new LinkedHashSet<>();
        orderedFields.add(RoomField.NUMBER);
        orderedFields.add(RoomField.DESCRIPTION);
        orderedFields.add(RoomField.ROOM_TYPE);
        orderedFields.add(RoomField.MIN_PEOPLE);
        orderedFields.add(RoomField.MAX_PEOPLE);
        orderedFields.add(RoomField.NUMBER_OF_BEDS);
        return FieldUtil.convertToInputFields(orderedFields);
    }

    public static Set<InputField> getCustomerOrderedInputFields() {
        Set<CustomerField> orderedFields = new LinkedHashSet<>();
        orderedFields.add(CustomerField.FIRST_NAME);
        orderedFields.add(CustomerField.LAST_NAME);
        orderedFields.add(CustomerField.PASSPORT_ID);
        orderedFields.add(CustomerField.IDENTIFIER_NUMBER);
        orderedFields.add(CustomerField.COUNTRY);
        orderedFields.add(CustomerField.NATIONALITY);
        orderedFields.add(CustomerField.BIRTHDAY);
        return FieldUtil.convertToInputFields(orderedFields);
    }

    public static Set<InputField> getAssignmentOrderedInputFields() {
        Set<RoomAssignmentField> orderedFields = new LinkedHashSet<>();
        orderedFields.add(RoomAssignmentField.OWNER);
        orderedFields.add(RoomAssignmentField.ADDITIONAL_PERSONS);
        orderedFields.add(RoomAssignmentField.ROOM);
        orderedFields.add(RoomAssignmentField.START_DATE);
        orderedFields.add(RoomAssignmentField.COMPLETE_DATE);
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
        value = StringUtils.trimToNull(value);
        if(value == null) {
            return null;
        }
        Class<?> type = field.getType();
        if(type.isAssignableFrom(Byte.class)) {
            return Byte.valueOf(value);
        }
        else if(type.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        }
        else if(type.isAssignableFrom(Long.class)) {
            return Long.valueOf(value);
        }
        else if(type.isAssignableFrom(LocalDate.class)) {
            return LocalDate.parse(value);
        }
        else if(type.isAssignableFrom(Gender.class)) {
            for(Gender gender : Gender.values()) {
                if(gender.getName().equals(value)) {
                    return gender;
                }
            }
        }
        return value;
    }
}
