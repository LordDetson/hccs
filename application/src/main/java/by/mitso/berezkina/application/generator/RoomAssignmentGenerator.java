package by.mitso.berezkina.application.generator;

import java.time.LocalDate;

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
        LocalDate now = LocalDate.now();
        RoomAssignment assignment = new RoomAssignment(nastya, doubleRoom, now, now.plusDays(20));
        assignment.setAdditionalPersons(1);
        roomAssignmentRepository.save(assignment);
    }
}
