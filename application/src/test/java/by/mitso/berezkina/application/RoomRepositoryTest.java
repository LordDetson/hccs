package by.mitso.berezkina.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.application.repository.CrudRepositoryImpl;
import by.mitso.berezkina.application.util.HibernateUtil;
import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Gender;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.RoomAssignment;
import by.mitso.berezkina.domain.RoomType;

class RoomRepositoryTest {

    private RoomRepository roomRepository;
    private CrudRepository<RoomType, Integer> roomTypeRepository;
    private CrudRepository<Customer, Long> customerRepository;
    private CrudRepository<RoomAssignment, Long> assignmentRepository;

    @BeforeEach
    void setUp() {
        EntityManagerFactory entityManagerFactory = HibernateUtil.createEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        roomRepository = new RoomRepository(entityManager);
        roomTypeRepository = new CrudRepositoryImpl<>(entityManager, RoomType.class);
        customerRepository = new CrudRepositoryImpl<>(entityManager, Customer.class);
        assignmentRepository = new CrudRepositoryImpl<>(entityManager, RoomAssignment.class);
    }

    @AfterEach
    void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    void testFind() {
        // Give
        Customer customer = new Customer("Настя", "Березкина", "MP9822648", "8728682A846PB7", "Беларусь", "Republic of Belarus",
                LocalDate.of(2000, 9, 30), Gender.FEMALE);
        customer = customerRepository.save(customer);
        RoomType doubleType = new RoomType("Double", (byte) 1, (byte) 2, (byte) 1, (byte) 2);
        doubleType = roomTypeRepository.save(doubleType);
        Room room = new Room("K001", doubleType);
        room = roomRepository.save(room);
        RoomAssignment assignment = new RoomAssignment(customer, room,
                LocalDate.of(2021, 12, 12),
                LocalDate.of(2022, 1, 1));
        assignment = assignmentRepository.save(assignment);
        roomRepository.refresh(room);

        //when
        List<Room> found = roomRepository.find(LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 3), null, null, null);
        assertEquals(1, found.size());

        found = roomRepository.find(LocalDate.of(2021, 12, 10), LocalDate.of(2021, 12, 12), null, null, null);
        assertEquals(0, found.size());

        found = roomRepository.find(LocalDate.of(2021, 12, 10), LocalDate.of(2021, 12, 13), null, null, null);
        assertEquals(0, found.size());

        found = roomRepository.find(LocalDate.of(2021, 12, 12), LocalDate.of(2021, 12, 13), null, null, null);
        assertEquals(0, found.size());

        found = roomRepository.find(LocalDate.of(2021, 12, 13), LocalDate.of(2021, 12, 15), null, null, null);
        assertEquals(0, found.size());

        found = roomRepository.find(LocalDate.of(2021, 12, 20), LocalDate.of(2022, 1, 1), null, null, null);
        assertEquals(0, found.size());

        found = roomRepository.find(LocalDate.of(2021, 12, 20), LocalDate.of(2022, 1, 3), null, null, null);
        assertEquals(0, found.size());

        found = roomRepository.find(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 4), null, null, null);
        assertEquals(0, found.size());

        found = roomRepository.find(LocalDate.of(2022, 1, 2), LocalDate.of(2022, 1, 4), null, null, null);
        assertEquals(1, found.size());
    }
}