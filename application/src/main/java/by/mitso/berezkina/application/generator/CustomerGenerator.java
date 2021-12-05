package by.mitso.berezkina.application.generator;

import java.time.LocalDate;
import java.util.List;

import by.mitso.berezkina.application.repository.CrudRepository;
import by.mitso.berezkina.domain.Customer;
import by.mitso.berezkina.domain.Customer.CustomerField;
import by.mitso.berezkina.domain.Gender;

public class CustomerGenerator implements Generator {

    private final CrudRepository<Customer, Long> customerRepository;
    private Customer nastya;
    private Customer romance;
    private Customer dmitry;

    public CustomerGenerator(CrudRepository<Customer, Long> customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void generate() {
        nastya = new Customer("Настя", "Березкина", "MP9822648", "8728682A846PB7", "Беларусь", "Republic of Belarus",
                LocalDate.of(2000, 9, 30), Gender.FEMALE);
        romance = new Customer("Романс", "Рахманый", "MP5624190", "0654872A019PB7", "Беларусь", "Republic of Belarus",
                LocalDate.of(2000, 11, 5), Gender.MALE);
        dmitry = new Customer("Дмитрий", "Бабанин", "MP3018524", "3014520A824PB7", "Беларусь", "Republic of Belarus",
                LocalDate.of(1996, 4, 21), Gender.MALE);

        customerRepository.saveAll(List.of(nastya, romance, dmitry));
    }

    public Customer getNastya() {
        return customerRepository.findByField(CustomerField.IDENTIFIER_NUMBER, nastya.getIdentifierNumber()).get();
    }

    public Customer getRomance() {
        return customerRepository.findByField(CustomerField.IDENTIFIER_NUMBER, romance.getIdentifierNumber()).get();
    }

    public Customer getDmitry() {
        return customerRepository.findByField(CustomerField.IDENTIFIER_NUMBER, dmitry.getIdentifierNumber()).get();
    }
}
