package by.mitso.berezkina.factor;

import java.util.Map;
import java.util.Optional;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.Room.RoomField;
import by.mitso.berezkina.domain.RoomType;
import by.mitso.berezkina.domain.RoomType.RoomTypeField;

public class RoomFactory implements Factory<Room> {

    private static RoomFactory INSTANCE;
    private final Field roomTypesField;
    private final CrudRepository<RoomType, Integer> roomTypeRepository;

    private RoomFactory(Field roomTypesField, CrudRepository<RoomType, Integer> roomTypeRepository) {
        this.roomTypesField = roomTypesField;
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public Room factor(Map<Field, ?> fieldValueMap) {
        String number = (String) fieldValueMap.remove(RoomField.NUMBER);
        String roomTypeName = (String) fieldValueMap.remove(RoomField.ROOM_TYPE);
        Optional<RoomType> roomType = roomTypeRepository.findByField(RoomTypeField.NAME, roomTypeName);
        if(roomType.isPresent()) {
            Room room = new Room(number, roomType.get());
            room.setFieldValue(fieldValueMap);
            return room;
        }
        return null;
    }

    /**
     * Lazy initialization with Double check locking:
     * In this mechanism, we overcome the overhead problem of synchronized code.
     * In this method, getInstance is not synchronized but the block which creates instance is synchronized
     * so that minimum number of threads have to wait and that’s only for first time.
     *
     * @return room factory
     */
    public static RoomFactory getInstance(Field roomTypesField, CrudRepository<RoomType, Integer> roomTypeRepository) {
        if(INSTANCE == null) {
            synchronized(RoomFactory.class) {
                if(INSTANCE == null) {
                    INSTANCE = new RoomFactory(roomTypesField, roomTypeRepository);
                }
            }
        }
        return INSTANCE;
    }
}
