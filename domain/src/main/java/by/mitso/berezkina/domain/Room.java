package by.mitso.berezkina.domain;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import by.mitso.berezkina.domain.exception.DomainChecker;
import by.mitso.berezkina.domain.exception.DomainConstraintException;

@Entity
@Table(name = "rooms")
public class Room extends Persistent<Integer> {

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(name = "description")
    private String description;

    @Column(name = "min_people")
    private Byte minPeople;

    @Column(name = "max_people")
    private Byte maxPeople;

    @Column(name = "number_of_beds")
    private Byte numberOfBeds;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<Tariff> tariffs;

    @OneToMany(mappedBy = "room", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<RoomAssignment> assignments;

    public enum RoomField implements Field {
        ID("id", "ID", Integer.class, false, Room::getId,
                (room, value) -> room.setId((Integer) value)),
        NUMBER("number", "номер комнаты", String.class, true, Room::getNumber,
                (room, value) -> room.setNumber((String) value)),
        ROOM_TYPE("roomType", "тип комнаты", RoomType.class, true, Room::getRoomType,
                (room, value) -> room.setRoomType((RoomType) value)),
        DESCRIPTION("description", "дополнительное описание", String.class, false, Room::getDescription,
                (room, value) -> room.setDescription((String) value)),
        MIN_PEOPLE("minPeople", "минимальное количество людей", Byte.class, false, Room::getMinPeople,
                (room, value) -> room.setMinPeople((Byte) value)),
        MAX_PEOPLE("maxPeople", "максимальное количество людей", Byte.class, false, Room::getMaxPeople,
                (room, value) -> room.setMaxPeople((Byte) value)),
        NUMBER_OF_BEDS("numberOfBeds", "количество краватей", Byte.class, false, Room::getNumberOfBeds,
                (room, value) -> room.setNumberOfBeds((Byte) value)),
        TARIFFS("tariffs", "тарифы", Set.class, false, Room::getTariffs,
                (room, value) -> room.setTariffs((Set<Tariff>) value)),
        assignments("assignments", "история назначений", Set.class, false, Room::getAssignments,
                null),
        ;

        private final String name;
        private final String caption;
        private final Class<?> type;
        private final boolean required;
        private final Function<Room, ?> getter;
        private final BiConsumer<Room, Object> setter;

        RoomField(String name, String caption, Class<?> type, boolean required, Function<Room, ?> getter, BiConsumer<Room, Object> setter) {
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
            return getter.apply((Room) component);
        }

        @Override
        public void setValue(Object component, Object value) {
            if(setter == null) {
                DomainChecker.throwNonEditableField(name);
            }
            setter.accept((Room) component, value);
        }
    }

    protected Room() {
        // for ORM
    }

    public Room(String number, RoomType roomType) {
        DomainChecker.checkNotNull(number, RoomField.NUMBER.getName());
        DomainChecker.checkNotNull(roomType, RoomField.ROOM_TYPE.getName());
        this.number = number;
        this.roomType = roomType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        DomainChecker.checkNotNull(number, RoomField.NUMBER.getName());
        this.number = number;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        DomainChecker.checkNotNull(roomType, RoomField.ROOM_TYPE.getName());
        this.roomType = roomType;
    }

    public String getDescription() {
        if(description == null) {
            return "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getMinPeople() {
        if(minPeople == null) {
            return roomType.getMinPeople();
        }
        return minPeople;
    }

    public void setMinPeople(Byte minPeople) {
        DomainChecker.checkGreaterThanOrEqualToZero(minPeople, RoomField.MIN_PEOPLE.getName());
        Byte minPeopleOfRoomType = roomType.getMinPeople();
        Byte maxPeople = getMaxPeople();
        if(minPeople == null) {
            this.minPeople = null;
        }
        else if(minPeople < minPeopleOfRoomType || minPeople > maxPeople) {
            throw new DomainConstraintException(String.format(
                    "The minimum number of beds (%s) must be greater than or equal to the minimum number of beds of room type (%s) and less than or equal to the maximum number of beds (%s)",
                    minPeople, minPeopleOfRoomType, maxPeople));
        }
        else if(!minPeople.equals(minPeopleOfRoomType)) {
            this.minPeople = minPeople;
        }
    }

    public Byte getMaxPeople() {
        if(maxPeople == null) {
            return roomType.getMaxPeople();
        }
        return maxPeople;
    }

    public void setMaxPeople(Byte maxPeople) {
        DomainChecker.checkGreaterThanOrEqualToZero(maxPeople, RoomField.MAX_PEOPLE.getName());
        Byte minPeople = getMinPeople();
        Byte maxPeopleOfRoomType = roomType.getMaxPeople();
        if(maxPeople == null) {
            this.maxPeople = null;
        }
        else if(maxPeople < minPeople || maxPeople > maxPeopleOfRoomType) {
            throw new DomainConstraintException(String.format(
                    "The maximum number of beds (%s) must be greater than or equal to the minimum number of beds (%s) and less than or equal to the maximum number of beds of room type (%s)",
                    maxPeople, minPeople, maxPeopleOfRoomType));
        }
        else if(!minPeople.equals(maxPeopleOfRoomType)) {
            this.maxPeople = maxPeople;
        }
    }

    public Byte getNumberOfBeds() {
        if(numberOfBeds == null) {
            return roomType.getMinBeds();
        }
        return numberOfBeds;
    }

    public void setNumberOfBeds(Byte numberOfBeds) {
        DomainChecker.checkGreaterThanOrEqualToZero(numberOfBeds, RoomField.NUMBER_OF_BEDS.getName());
        Byte minBedsOfRoomType = roomType.getMinBeds();
        Byte maxBedsOfRoomType = roomType.getMaxBeds();
        if(numberOfBeds == null) {
            this.numberOfBeds = null;
        }
        else if(minBedsOfRoomType < numberOfBeds || maxBedsOfRoomType > numberOfBeds) {
            throw new DomainConstraintException(String.format(
                    "The number of beds (%s) must be greater than or equal to the minimum number of beds (%s) and less than or equal to the maximum number of beds (%s)",
                    numberOfBeds, minBedsOfRoomType, maxBedsOfRoomType));
        }
        else if(!numberOfBeds.equals(minBedsOfRoomType)) {
            this.numberOfBeds = numberOfBeds;
        }
    }

    public Set<Tariff> getTariffs() {
        return tariffs;
    }

    public void setTariffs(Set<Tariff> tariffs) {
        this.tariffs = tariffs;
    }

    public Set<RoomAssignment> getAssignments() {
        return assignments;
    }

    public static Set<Field> getAllFields() {
        return Set.of(RoomField.values());
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return number.equals(room.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "Room{" +
                "number='" + number + '\'' +
                '}';
    }
}
