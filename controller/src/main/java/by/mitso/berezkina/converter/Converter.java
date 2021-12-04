package by.mitso.berezkina.converter;

@FunctionalInterface
public interface Converter<From, To> {

    To convert(From component);
}
