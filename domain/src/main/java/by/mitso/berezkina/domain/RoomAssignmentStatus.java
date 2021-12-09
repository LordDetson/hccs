package by.mitso.berezkina.domain;

public enum RoomAssignmentStatus {
    BOOKED("booked", "забронирован"),
    IN_PROGRESS("in progress", "активно"),
    OVERDUE("overdue", "просроченый"),
    COMPLETED("completed", "завершено"),
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
