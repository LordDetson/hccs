package by.mitso.berezkina.domain;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import by.mitso.berezkina.domain.exception.DomainChecker;

@Entity
@Table(name = "room_assignments")
public class RoomAssignment extends Persistent<Long> {

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Customer owner;

    @Column(name = "additional_persons")
    private Integer additionalPersons;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

    @Column(name = "complete_date_time")
    private LocalDateTime completeDateTime;

    @OneToOne(mappedBy = "assignment", cascade = CascadeType.PERSIST)
    private Payment payment;

    @Column(name = "status", nullable = false)
    private RoomAssignmentStatus status;

    public enum RoomAssignmentField implements Field {
        OWNER("owner", "владелец", Customer.class, true, RoomAssignment::getOwner,
                null),
        ADDITIONAL_PERSONS("additionalPersons", "дополнительные жильцы", Integer.class, false, RoomAssignment::getAdditionalPersons,
                null),
        ROOMS("room", "назначенная комната", Room.class, true, RoomAssignment::getRoom,
                null),
        TARIFF("tariff", "тариф", Tariff.class, true, RoomAssignment::getTariff,
                null),
        COMPLETE_DATE_TIME("completeDateTime", "дата и время завершения", LocalDateTime.class, true, RoomAssignment::getCompleteDateTime,
                null),
        PAYMENT("payment", "сумма оплаты", Payment.class, true, RoomAssignment::getPayment,
                (assignment, value) -> assignment.setPayment((Payment) value)),
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

    public RoomAssignment(Customer owner, Room room, Tariff tariff, Payment payment, LocalDateTime completeDateTime) {
        this.owner = owner;
        this.room = room;
        this.tariff = tariff;
        this.payment = payment;
        this.completeDateTime = completeDateTime;
        this.status = RoomAssignmentStatus.IN_PROGRESS;
    }

    public Customer getOwner() {
        return owner;
    }

    public Integer getAdditionalPersons() {
        return additionalPersons;
    }

    public Room getRoom() {
        return room;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public LocalDateTime getCompleteDateTime() {
        return completeDateTime;
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
