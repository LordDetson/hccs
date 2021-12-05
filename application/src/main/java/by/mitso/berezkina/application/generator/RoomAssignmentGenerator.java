package by.mitso.berezkina.application.generator;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Payment;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.RoomAssignment;
import by.mitso.berezkina.domain.Tariff;

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
        Tariff tariff = doubleRoom.getTariffs().iterator().next();
        MonetaryAmount monetaryAmount = tariff.add(tariff);
        int days = tariff.getNumberOfDays() * 2;
        Payment payment = new Payment(monetaryAmount.getNumber(), monetaryAmount.getCurrency().getCurrencyCode(), LocalDateTime.now());
        RoomAssignment assignment = new RoomAssignment(nastya, doubleRoom, tariff, payment, payment.getDateTime().plusDays(days));
        payment.setAssignment(assignment);

        roomAssignmentRepository.save(assignment);
    }
}
