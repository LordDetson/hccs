package by.mitso.berezkina.domain;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import by.mitso.berezkina.domain.exception.DomainChecker;

@Entity
@Table(name = "room_assignments")
public class RoomAssignment extends Persistent<Long> {

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Customer owner;

    @Column(name = "additional_persons")
    private Byte additionalPersons;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "complete_date")
    private LocalDate completeDate;

    @Column(name = "status", nullable = false)
    private RoomAssignmentStatus status;

    public enum RoomAssignmentField implements Field {
        ID("id", "ID", Long.class, false, RoomAssignment::getId,
                (assignment, value) -> assignment.setId((Long) value)),
        OWNER("owner", "владелец", Customer.class, true, RoomAssignment::getOwner,
                null),
        ADDITIONAL_PERSONS("additionalPersons", "дополнительные жильцы", Byte.class, false, RoomAssignment::getAdditionalPersons,
                (assignment, value) -> assignment.setAdditionalPersons((Byte) value)),
        ROOM("room", "назначенная комната", Room.class, true, RoomAssignment::getRoom,
                null),
        START_DATE("startDate", "дата начала", LocalDate.class, true, RoomAssignment::getStartDate,
                null),
        COMPLETE_DATE("completeDate", "дата завершения", LocalDate.class, true, RoomAssignment::getCompleteDate,
                null),
        STATUS("status", "статус", RoomAssignmentStatus.class, false, RoomAssignment::getStatus,
                (assignment, value) -> assignment.setStatus((RoomAssignmentStatus) value)),
        ;

        private final String name;
        private final String caption;
        private final Class<?> type;
        private final boolean required;
        private final Function<RoomAssignment, ?> getter;
        private final BiConsumer<RoomAssignment, Object> setter;

        RoomAssignmentField(String name, String caption, Class<?> type, boolean required, Function<RoomAssignment, ?> getter,
                BiConsumer<RoomAssignment, Object> setter) {
            this.name = name;
            this.caption = caption;
            this.type = type;
            this.required = required;
            this.getter = getter;
            this.setter = setter;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public Class<?> getType() {
            return type;
        }

        @Override
        public boolean isRequired() {
            return required;
        }

        @Override
        public Object getValue(Object component) {
            return getter.apply((RoomAssignment) component);
        }

        @Override
        public void setValue(Object component, Object value) {
            if(setter == null) {
                DomainChecker.throwNonEditableField(name);
            }
            setter.accept((RoomAssignment) component, value);
        }
    }

    protected RoomAssignment() {
        // for ORM
    }

    public RoomAssignment(Customer owner, Room room, LocalDate startDate, LocalDate completeDate) {
        this.owner = owner;
        this.room = room;
        this.startDate = startDate;
        this.completeDate = completeDate;
        LocalDate now = LocalDate.now();
        this.status = startDate.equals(now) ? RoomAssignmentStatus.IN_PROGRESS : RoomAssignmentStatus.BOOKED;
    }

    public Customer getOwner() {
        return owner;
    }

    public Byte getAdditionalPersons() {
        return additionalPersons;
    }

    public void setAdditionalPersons(Byte additionalPersons) {
        this.additionalPersons = additionalPersons == null ? 0 : additionalPersons;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getCompleteDate() {
        return completeDate;
    }

    public RoomAssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(RoomAssignmentStatus status) {
        this.status = status;
    }

    public static Set<Field> getAllFields() {
        return Set.of(RoomAssignmentField.values());
    }
}
