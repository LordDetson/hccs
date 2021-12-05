package by.mitso.berezkina.application.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.Room.RoomField;
import by.mitso.berezkina.domain.Tariff;

public class RoomGenerator implements Generator {

    private final CrudRepository<Room, Integer> roomRepository;
    private final CrudRepository<Tariff, Long> tariffRepository;
    private final RoomTypeGenerator roomTypeGenerator;

    private Room singleRoom;
    private Room doubleRoom;
    private Room tripleRoom;
    private Room extraBedRoom;

    public RoomGenerator(CrudRepository<Room, Integer> roomRepository,
            CrudRepository<Tariff, Long> tariffRepository,
            RoomTypeGenerator roomTypeGenerator) {
        this.roomRepository = roomRepository;
        this.tariffRepository = tariffRepository;
        this.roomTypeGenerator = roomTypeGenerator;
    }

    @Override
    public void generate() {
        singleRoom = new Room("K001", roomTypeGenerator.getSingleType());
        doubleRoom = new Room("K002", roomTypeGenerator.getDoubleType());
        tripleRoom = new Room("K003", roomTypeGenerator.getTripleType());
        extraBedRoom = new Room("K004", roomTypeGenerator.getExtraBedType());

        roomRepository.saveAll(List.of(singleRoom, doubleRoom, tripleRoom, extraBedRoom));

        List<Room> rooms = getAll();
        AtomicInteger counter = new AtomicInteger();
        rooms.forEach(room -> {
            counter.getAndIncrement();
            List<Tariff> tariffs = new ArrayList<>();
            Tariff tariff = new Tariff(1, 38 * counter.get(), "BYN", room);
            tariff.setDescription("Обычный (рабочие дни)");
            tariffs.add(tariff);
            tariff = new Tariff(1, 42 * counter.get(), "BYN", room);
            tariff.setDescription("Обычный (выходные дни)");
            tariffs.add(tariff);
            tariff = new Tariff(7, 36 * counter.get(), "BYN", room);
            tariff.setDescription("На неделю");
            tariffs.add(tariff);
            tariff = new Tariff(30, 30 * counter.get(), "BYN", room);
            tariff.setDescription("На месяц");
            tariffs.add(tariff);
            room.setTariffs(tariffs);
            tariffRepository.saveAll(tariffs);
        });
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
