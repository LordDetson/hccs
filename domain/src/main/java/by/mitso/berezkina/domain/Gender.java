package by.mitso.berezkina.domain;

public enum Gender {
    MALE("мужчина", "М"),
    FEMALE("женщина", "Ж");

    private final String name;
    private final String shortName;

    Gender(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}
