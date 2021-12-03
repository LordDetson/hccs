package by.mitso.berezkina.domain.exception;

public class DomainChecker {

    private static final String X_MUST_BE_NOT_NULL = "'%s' must be not null";
    private static final String X_MUST_BE_GREATER_THAN_OR_EQUAL_TO_Y = "'%s' must be greater than or equal to '%s'";
    private static final String X_FIELD_NON_EDITABLE = "'%s' field is non-editable";

    public static void checkNotNull(Object object, String objectName) {
        if(object == null) {
            throw new DomainConstraintException(String.format(X_MUST_BE_NOT_NULL, objectName));
        }
    }

    public static void checkGreaterThanOrEqualToNumber(Byte numberToCheck, String objectName, Byte number) {
        if(numberToCheck != null && numberToCheck < number) {
            throw new DomainConstraintException(String.format(X_MUST_BE_GREATER_THAN_OR_EQUAL_TO_Y, objectName, number));
        }
    }

    public static void checkGreaterThanOrEqualToZero(Byte numberToCheck, String objectName) {
        checkGreaterThanOrEqualToNumber(numberToCheck, objectName, (byte) 0);
    }

    public static void checkGreaterThanOrEqualToNumber(Long numberToCheck, String objectName, Long number) {
        if(numberToCheck != null && numberToCheck < number) {
            throw new DomainConstraintException(String.format(X_MUST_BE_GREATER_THAN_OR_EQUAL_TO_Y, objectName, number));
        }
    }

    public static void checkGreaterThanOrEqualToZero(Long numberToCheck, String objectName) {
        checkGreaterThanOrEqualToNumber(numberToCheck, objectName, 0L);
    }

    public static void throwNonEditableField(String fieldName) {
        throw new DomainConstraintException(String.format(X_FIELD_NON_EDITABLE, fieldName));
    }
}
