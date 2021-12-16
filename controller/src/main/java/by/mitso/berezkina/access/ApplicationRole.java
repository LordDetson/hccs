package by.mitso.berezkina.access;

public enum ApplicationRole {

    ADMINISTRATOR_ROLE("administrator"),
    USER_ROLE("user");

    private final String name;

    ApplicationRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
