package by.mitso.berezkina.factor;

import java.time.LocalDate;
import java.util.Map;

import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Customer.CustomerField;
import by.mitso.berezkina.domain.Field;
import by.mitso.berezkina.domain.Gender;

public class CustomerFactory implements Factory<Customer> {

    private static CustomerFactory INSTANCE;

    private CustomerFactory() {}

    @Override
    public Customer factor(Map<Field, ?> fieldValueMap) {
        String firstName = (String) fieldValueMap.remove(CustomerField.FIRST_NAME);
        String lastName = (String) fieldValueMap.remove(CustomerField.LAST_NAME);
        String passportId = (String) fieldValueMap.remove(CustomerField.PASSPORT_ID);
        String identifierNumber = (String) fieldValueMap.remove(CustomerField.IDENTIFIER_NUMBER);
        String country = (String) fieldValueMap.remove(CustomerField.COUNTRY);
        String nationality = (String) fieldValueMap.remove(CustomerField.NATIONALITY);
        LocalDate birthday = (LocalDate) fieldValueMap.remove(CustomerField.BIRTHDAY);
        Gender gender = (Gender) fieldValueMap.remove(CustomerField.GENDER);
        Customer customer = new Customer(firstName, lastName, passportId, identifierNumber, country, nationality, birthday, gender);
        customer.setFieldValue(fieldValueMap);
        return customer;
    }

    /**
     * Lazy initialization with Double check locking:
     * In this mechanism, we overcome the overhead problem of synchronized code.
     * In this method, getInstance is not synchronized but the block which creates instance is synchronized
     * so that minimum number of threads have to wait and that’s only for first time.
     *
     * @return customer factory
     */
    public static CustomerFactory getInstance() {
        if(INSTANCE == null) {
            synchronized(CustomerFactory.class) {
                if(INSTANCE == null) {
                    INSTANCE = new CustomerFactory();
                }
            }
        }
        return INSTANCE;
    }
}
