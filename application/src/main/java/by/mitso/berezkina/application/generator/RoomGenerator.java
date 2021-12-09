package by.mitso.berezkina.application.generator;

import java.util.ArrayList;
import java.util.List;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.Room.RoomField;

public class RoomGenerator implements Generator {

    private final CrudRepository<Room, Integer> roomRepository;
    private final RoomTypeGenerator roomTypeGenerator;

    private Room singleRoom;
    private Room doubleRoom;
    private Room tripleRoom;
    private Room extraBedRoom;

    public RoomGenerator(CrudRepository<Room, Integer> roomRepository, RoomTypeGenerator roomTypeGenerator) {
        this.roomRepository = roomRepository;
        this.roomTypeGenerator = roomTypeGenerator;
    }

    @Override
    public void generate() {
        singleRoom = new Room("K001", roomTypeGenerator.getSingleType());
        doubleRoom = new Room("K002", roomTypeGenerator.getDoubleType());
        tripleRoom = new Room("K003", roomTypeGenerator.getTripleType());
        extraBedRoom = new Room("K004", roomTypeGenerator.getExtraBedType());

        roomRepository.saveAll(List.of(singleRoom, doubleRoom, tripleRoom, extraBedRoom));
    }

    public List<Room> getAll() {
        List<Room> rooms = new ArrayList<>();
        for(Room room : roomRepository.findAll()) {
            rooms.add(room);
        }
        return rooms;
    }

    public Room getSingleRoom() {
        return roomRepository.findByField(RoomField.NUMBER, singleRoom.getNumber()).get();
    }

    public Room getDoubleRoom() {
        return roomRepository.findByField(RoomField.NUMBER, doubleRoom.getNumber()).get();
    }

    public Room getTripleRoom() {
        return roomRepository.findByField(RoomField.NUMBER, tripleRoom.getNumber()).get();
    }

    public Room getExtraBedRoom() {
        return roomRepository.findByField(RoomField.NUMBER, extraBedRoom.getNumber()).get();
    }
}
