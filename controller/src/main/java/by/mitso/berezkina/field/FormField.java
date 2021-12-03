package by.mitso.berezkina.field;

public class FormField {
    private final String name;
    private final String caption;
    private final String type;

    public FormField(String name, String caption, String type) {
        this.name = name;
        this.caption = caption;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    public String getType() {
        return type;
    }
}
