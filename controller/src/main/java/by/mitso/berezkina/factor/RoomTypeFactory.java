package by.mitso.berezkina.factor;

import java.util.Map;

import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.RoomType;
import by.mitso.berezkina.domain.RoomType.RoomTypeField;

public class RoomTypeFactory implements Factory<RoomType> {

    private static RoomTypeFactory INSTANCE;

    private RoomTypeFactory() {}

    @Override
    public RoomType factor(Map<Field, ?> fieldValueMap) {
        String name = (String) fieldValueMap.remove(RoomTypeField.NAME);
        Byte minPeople = (Byte) fieldValueMap.remove(RoomTypeField.MIN_PEOPLE);
        Byte maxPeople = (Byte) fieldValueMap.remove(RoomTypeField.MAX_PEOPLE);
        Byte minBeds = (Byte) fieldValueMap.remove(RoomTypeField.MIN_BEDS);
        Byte maxBeds = (Byte) fieldValueMap.remove(RoomTypeField.MAX_BEDS);
        RoomType roomType = new RoomType(name, minPeople, maxPeople, minBeds, maxBeds);
        roomType.setFieldValue(fieldValueMap);
        return roomType;
    }

    /**
     * Lazy initialization with Double check locking:
     * In this mechanism, we overcome the overhead problem of synchronized code.
     * In this method, getInstance is not synchronized but the block which creates instance is synchronized
     * so that minimum number of threads have to wait and that’s only for first time.
     *
     * @return room type factory
     */
    public static RoomTypeFactory getInstance() {
        if(INSTANCE == null) {
            synchronized(RoomTypeFactory.class) {
                if(INSTANCE == null) {
                    INSTANCE = new RoomTypeFactory();
                }
            }
        }
        return INSTANCE;
    }
}
