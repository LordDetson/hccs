package by.mitso.berezkina.application.generator;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.application.repository.CrudRepositoryImpl;
import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.RoomAssignment;
import by.mitso.berezkina.domain.RoomType;

public class DataBaseGenerator implements Generator{

    private final EntityManager entityManager;

    public DataBaseGenerator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.date.generator.xml");
        configuration.addAnnotatedClass(RoomType.class);
        configuration.addAnnotatedClass(Room.class);
        configuration.addAnnotatedClass(RoomAssignment.class);
        configuration.addAnnotatedClass(Customer.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        DataBaseGenerator dataBaseGenerator = new DataBaseGenerator(sessionFactory.createEntityManager());
        dataBaseGenerator.generate();

        if(sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        if(serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
    }

    @Override
    public void generate() {
        CrudRepository<RoomType, Integer> roomTypeRepository = new CrudRepositoryImpl<>(entityManager, RoomType.class);
        CrudRepository<Room, Integer> roomRepository = new CrudRepositoryImpl<>(entityManager, Room.class);
        CrudRepository<Customer, Long> customerRepository = new CrudRepositoryImpl<>(entityManager, Customer.class);
        CrudRepository<RoomAssignment, Long> roomAssignmentRepository = new CrudRepositoryImpl<>(entityManager, RoomAssignment.class);


        RoomTypeGenerator roomTypeGenerator = new RoomTypeGenerator(roomTypeRepository);
        RoomGenerator roomGenerator = new RoomGenerator(roomRepository, roomTypeGenerator);
        CustomerGenerator customerGenerator = new CustomerGenerator(customerRepository);
        RoomAssignmentGenerator roomAssignmentGenerator = new RoomAssignmentGenerator(roomAssignmentRepository, customerGenerator,
                roomGenerator);


        List.of(roomTypeGenerator, roomGenerator, customerGenerator, roomAssignmentGenerator)
                .forEach(Generator::generate);
    }
}
