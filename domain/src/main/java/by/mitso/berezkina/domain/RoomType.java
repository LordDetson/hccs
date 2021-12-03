package by.mitso.berezkina.domain;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import by.mitso.berezkina.domain.Room.RoomField;
import by.mitso.berezkina.domain.exception.DomainChecker;

@Entity
@Table(name = "room_types")
public class RoomType extends Persistent<Integer> {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "min_people", nullable = false)
    private Byte minPeople;

    @Column(name = "max_people", nullable = false)
    private Byte maxPeople;

    @Column(name = "min_beds", nullable = false)
    private Byte minBeds;

    @Column(name = "max_beds", nullable = false)
    private Byte maxBeds;

    @OneToMany(mappedBy = "roomType", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<Room> rooms;

    public enum RoomTypeField implements Field {
        ID("id", "ID", Integer.class, RoomType::getId,
                (roomType, value) -> roomType.setId((Integer) value)),
        NAME("name", "название", String.class, RoomType::getName,
                (roomType, value) -> roomType.setName((String) value)),
        DESCRIPTION("description", "описание", String.class, RoomType::getDescription,
                (roomType, value) -> roomType.setDescription((String) value)),
        MIN_PEOPLE("minPeople", "минимальное предпочтительное количество людей", Byte.class, RoomType::getMinPeople,
                (roomType, value) -> roomType.setMinPeople((Byte) value)),
        MAX_PEOPLE("maxPeople", "максимальное предпочтительное количество людей", Byte.class, RoomType::getMaxPeople,
                (roomType, value) -> roomType.setMaxPeople((Byte) value)),
        MIN_BEDS("minBeds", "минимальное предпочтительное количество краватей", Byte.class, RoomType::getMinBeds,
                (roomType, value) -> roomType.setMinBeds((Byte) value)),
        MAX_BEDS("maxBeds", "максимальное предпочтительное количество краватей", Byte.class, RoomType::getMaxBeds,
                (roomType, value) -> roomType.setMaxBeds((Byte) value)),
        ROOMS("rooms", "комнаты", Set.class, RoomType::getRooms,
                (roomType, value) -> roomType.setRooms((Set<Room>) value))
        ;

        private final String name;
        private final String caption;
        private final Class<?> type;
        private final Function<RoomType, ?> getter;
        private final BiConsumer<RoomType, Object> setter;

        RoomTypeField(String name, String caption, Class<?> type, Function<RoomType, ?> getter,
                BiConsumer<RoomType, Object> setter) {
            this.name = name;
            this.caption = caption;
            this.type = type;
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
        public Object getValue(Object component) {
            return getter.apply((RoomType) component);
        }

        @Override
        public void setValue(Object component, Object value) {
            setter.accept((RoomType) component, value);
        }
    }

    protected RoomType() {
        // for ORM
    }

    public RoomType(String name, Byte minPeople, Byte maxPeople, Byte minBeds, Byte maxBeds) {
        DomainChecker.checkNotNull(name, RoomTypeField.NAME.getName());
        DomainChecker.checkNotNull(minPeople, RoomTypeField.MIN_PEOPLE.getName());
        DomainChecker.checkGreaterThanOrEqualToZero(minPeople, RoomTypeField.MIN_PEOPLE.getName());
        DomainChecker.checkNotNull(maxPeople, RoomTypeField.MAX_PEOPLE.getName());
        DomainChecker.checkGreaterThanOrEqualToNumber(maxPeople, RoomTypeField.MAX_PEOPLE.getName(), minPeople);
        DomainChecker.checkNotNull(minBeds, RoomTypeField.MIN_BEDS.getName());
        DomainChecker.checkGreaterThanOrEqualToZero(minBeds, RoomTypeField.MIN_BEDS.getName());
        DomainChecker.checkNotNull(maxBeds, RoomTypeField.MAX_BEDS.getName());
        DomainChecker.checkGreaterThanOrEqualToNumber(maxBeds, RoomTypeField.MAX_BEDS.getName(), minBeds);
        this.name = name;
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
        this.minBeds = minBeds;
        this.maxBeds = maxBeds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        DomainChecker.checkNotNull(name, RoomTypeField.NAME.getName());
        this.name = name;
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
        return minPeople;
    }

    public void setMinPeople(Byte minPeople) {
        DomainChecker.checkNotNull(minPeople, RoomTypeField.MIN_PEOPLE.getName());
        DomainChecker.checkGreaterThanOrEqualToZero(minPeople, RoomTypeField.MIN_PEOPLE.getName());
        this.minPeople = minPeople;
        removeRoomValue(RoomField.MIN_PEOPLE);
    }

    public Byte getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Byte maxPeople) {
        DomainChecker.checkNotNull(maxPeople, RoomTypeField.MAX_PEOPLE.getName());
        DomainChecker.checkGreaterThanOrEqualToNumber(maxPeople, RoomTypeField.MAX_PEOPLE.getName(), minPeople);
        this.maxPeople = maxPeople;
        removeRoomValue(RoomField.MAX_PEOPLE);
    }

    public Byte getMinBeds() {
        return minBeds;
    }

    public void setMinBeds(Byte minBeds) {
        DomainChecker.checkNotNull(minBeds, RoomTypeField.MIN_BEDS.getName());
        DomainChecker.checkGreaterThanOrEqualToZero(minBeds, RoomTypeField.MIN_BEDS.getName());
        this.minBeds = minBeds;
        removeRoomValue(RoomField.NUMBER_OF_BEDS);
    }

    public Byte getMaxBeds() {
        return maxBeds;
    }

    public void setMaxBeds(Byte maxBeds) {
        DomainChecker.checkNotNull(maxBeds, RoomTypeField.MAX_BEDS.getName());
        DomainChecker.checkGreaterThanOrEqualToNumber(maxBeds, RoomTypeField.MAX_BEDS.getName(), minBeds);
        this.maxBeds = maxBeds;
        removeRoomValue(RoomField.NUMBER_OF_BEDS);
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        DomainChecker.checkNotNull(rooms, RoomTypeField.ROOMS.getName());
        this.rooms = rooms;
    }

    public static Set<RoomTypeField> getAllFields() {
        return Set.of(RoomTypeField.values());
    }

    private void removeRoomValue(RoomField field) {
        getRooms().forEach(room -> field.setValue(room, null));
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomType roomType = (RoomType) o;
        return name.equals(roomType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "name='" + name + '\'' +
                '}';
    }
}
