package by.mitso.berezkina.application.generator;

import java.time.LocalDateTime;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.RoomAssignment;

public class RoomAssignmentGenerator implements Generator {

    private final CrudRepository<RoomAssignment, Long> roomAssignmentRepository;
    private final CustomerGenerator customerGenerator;
    private final RoomGenerator roomGenerator;

    public RoomAssignmentGenerator(CrudRepository<RoomAssignment, Long> roomAssignmentRepository, CustomerGenerator customerGenerator,
            RoomGenerator roomGenerator) {
        this.roomAssignmentRepository = roomAssignmentRepository;
        this.customerGenerator = customerGenerator;
        this.roomGenerator = roomGenerator;
    }

    @Override
    public void generate() {
        Customer nastya = customerGenerator.getNastya();
        Room doubleRoom = roomGenerator.getDoubleRoom();
        RoomAssignment assignment = new RoomAssignment(nastya, doubleRoom, LocalDateTime.now().plusDays(20));
        roomAssignmentRepository.save(assignment);
    }
}
