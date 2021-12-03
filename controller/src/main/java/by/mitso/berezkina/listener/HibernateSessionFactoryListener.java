package by.mitso.berezkina.listener;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Payment;
import by.mitso.berezkina.domain.Room;
import by.mitso.berezkina.domain.RoomAssignment;
import by.mitso.berezkina.domain.RoomType;
import by.mitso.berezkina.domain.Tariff;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class HibernateSessionFactoryListener implements ServletContextListener {

    private ServiceRegistry serviceRegistry;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(RoomType.class);
        configuration.addAnnotatedClass(Room.class);
        configuration.addAnnotatedClass(RoomAssignment.class);
        configuration.addAnnotatedClass(Tariff.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAnnotatedClass(Customer.class);

        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        servletContextEvent.getServletContext().setAttribute("SessionFactory", sessionFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        SessionFactory sessionFactory = (SessionFactory) servletContextEvent.getServletContext().getAttribute("SessionFactory");
        if(sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        if(serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
    }
}
