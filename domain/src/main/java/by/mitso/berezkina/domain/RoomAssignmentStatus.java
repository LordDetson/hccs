package by.mitso.berezkina.domain;

public enum RoomAssignmentStatus {
    BOOKED("booked", "забронировано"),
    IN_PROGRESS("in progress", "активно"),
    OVERDUE("overdue", "просрочено"),
    COMPLETED("completed", "завершено"),
    CANCELED("canceled", "отменено"),
    ;

    private final String name;
    private final String caption;

    RoomAssignmentStatus(String name, String caption) {
        this.name = name;
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }
}
