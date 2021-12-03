package by.mitso.berezkina.factor;

import java.util.Map;

import by.mitso.berezkina.domain.Field;

public interface Factory<T> {

    T factor(Map<Field, ?> fieldValueMap);
}
