package by.mitso.berezkina.application.generator;

import java.util.ArrayList;
import java.util.List;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.domain.RoomType;
import by.mitso.berezkina.domain.RoomType.RoomTypeField;

public class RoomTypeGenerator implements Generator {

    private final CrudRepository<RoomType, Integer> roomTypeRepository;

    private RoomType singleType;
    private RoomType doubleType;
    private RoomType tripleType;
    private RoomType extraBedType;

    public RoomTypeGenerator(CrudRepository<RoomType, Integer> roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public void generate() {
        singleType = new RoomType("Single", (byte) 1, (byte) 1, (byte) 1, (byte) 1);
        singleType.setDescription("Однокомнатный, для размещения одного отдыхающего. Спальное место одно.");

        doubleType = new RoomType("Double ", (byte) 1, (byte) 2, (byte) 1, (byte) 2);
        doubleType.setDescription("Однокомнатный номер для двоих. Может быть с одной большой кроватью или с двумя раздельными кроватями (double twin).");

        tripleType = new RoomType("Triple", (byte) 2, (byte) 3, (byte) 2, (byte) 3);
        tripleType.setDescription("Номер для троих отдыхающих. Обычно в номере две кровати и одно дополнительное спальное место (диван, либо раскладушка).");

        extraBedType = new RoomType("Extra Bed", (byte) 3, (byte) 4, (byte) 2, (byte) 4);
        extraBedType.setDescription("Обозначение дополнительного спального места.");

        roomTypeRepository.saveAll(List.of(singleType, doubleType, tripleType, extraBedType));
    }

    public List<RoomType> getAll() {
        List<RoomType> roomTypes = new ArrayList<>();
        for(RoomType roomType : roomTypeRepository.findAll()) {
            roomTypes.add(roomType);
        }
        return roomTypes;
    }

    public RoomType getSingleType() {
        return roomTypeRepository.findByField(RoomTypeField.NAME, singleType.getName()).get();
    }

    public RoomType getDoubleType() {
        return roomTypeRepository.findByField(RoomTypeField.NAME, doubleType.getName()).get();
    }

    public RoomType getTripleType() {
        return roomTypeRepository.findByField(RoomTypeField.NAME, tripleType.getName()).get();
    }

    public RoomType getExtraBedType() {
        return roomTypeRepository.findByField(RoomTypeField.NAME, extraBedType.getName()).get();
    }
}
