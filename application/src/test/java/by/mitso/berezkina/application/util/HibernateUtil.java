package by.mitso.berezkina.application.util;

import javax.persistence.EntityManagerFactory;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.RoomAssignment;
import by.mitso.berezkina.domain.RoomType;

public class HibernateUtil {

    private static ServiceRegistry serviceRegistry;

    public static EntityManagerFactory createEntityManagerFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.test.xml");
        configuration.addAnnotatedClass(RoomType.class);
        configuration.addAnnotatedClass(Room.class);
        configuration.addAnnotatedClass(RoomAssignment.class);
        configuration.addAnnotatedClass(Customer.class);

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void shutdown() {
        if (serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
    }
}
