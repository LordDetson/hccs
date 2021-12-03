package by.mitso.berezkina.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import by.mitso.berezkina.domain.exception.DomainChecker;

@Entity
@Table(name = "customers")
public class Customer extends Persistent<Long> {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "passport_id", nullable = false)
    private String passportId;

    @Column(name = "identifier_number", nullable = false, unique = true)
    private String identifierNumber;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "gender", nullable = false)
    private Gender gender;

    @OneToMany(mappedBy="owner", fetch = FetchType.EAGER)
    private Set<RoomAssignment> assignments;

    public enum CustomerField implements Field {
        FIRST_NAME("firstName", "имя", String.class, Customer::getFirstName,
                (customer, value) -> customer.setFirstName((String) value)),
        LAST_NAME("lastName", "фамилия", String.class, Customer::getLastName,
                (customer, value) -> customer.setLastName((String) value)),
        PASSPORT_ID("passportId", "номер паспорта", String.class, Customer::getPassportId,
                (customer, value) -> customer.setPassportId((String) value)),
        IDENTIFIER_NUMBER("identifierNumber", "идентификационный номер", String.class, Customer::getIdentifierNumber,
                (customer, value) -> customer.setIdentifierNumber((String) value)),
        COUNTRY("country", "страна", String.class, Customer::getCountry,
                (customer, value) -> customer.setCountry((String) value)),
        NATIONALITY("nationality", "национальность", String.class, Customer::getNationality,
                (customer, value) -> customer.setNationality((String) value)),
        BIRTHDAY("birthday", "дата рождения", LocalDate.class, Customer::getBirthday,
                (customer, value) -> customer.setBirthday((LocalDate) value)),
        GENDER("gender", "пол", Gender.class, Customer::getGender,
                (customer, value) -> customer.setGender((Gender) value)),
        assignments("assignments", "назначеные комнаты", Set.class, Customer::getAssignments,
                (customer, value) -> customer.setAssignments((Set<RoomAssignment>) value))
        ;

        private final String name;
        private final String caption;
        private final Class<?> type;
        private final Function<Customer, ?> getter;
        private final BiConsumer<Customer, Object> setter;

        CustomerField(String name, String caption, Class<?> type, Function<Customer, ?> getter, BiConsumer<Customer, Object> setter) {
            this.name = name;
            this.caption = caption;
            this.type = type;
            this.getter = getter;
            this.setter = setter;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public Class<?> getType() {
            return type;
        }

        @Override
        public Object getValue(Object component) {
            return getter.apply((Customer) component);
        }

        @Override
        public void setValue(Object component, Object value) {
            if(setter == null) {
                DomainChecker.throwNonEditableField(name);
            }
            setter.accept((Customer) component, value);
        }
    }

    protected Customer() {
        // for ORM
    }

    public Customer(String firstName, String lastName, String passportId, String identifierNumber, String country, String nationality,
            LocalDate birthday, Gender gender) {
        DomainChecker.checkNotNull(firstName, CustomerField.FIRST_NAME.getName());
        DomainChecker.checkNotNull(lastName, CustomerField.LAST_NAME.getName());
        DomainChecker.checkNotNull(passportId, CustomerField.PASSPORT_ID.getName());
        DomainChecker.checkNotNull(identifierNumber, CustomerField.IDENTIFIER_NUMBER.getName());
        DomainChecker.checkNotNull(country, CustomerField.COUNTRY.getName());
        DomainChecker.checkNotNull(nationality, CustomerField.NATIONALITY.getName());
        DomainChecker.checkNotNull(birthday, CustomerField.BIRTHDAY.getName());
        DomainChecker.checkNotNull(gender, CustomerField.GENDER.getName());
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
        this.identifierNumber = identifierNumber;
        this.country = country;
        this.nationality = nationality;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        DomainChecker.checkNotNull(firstName, CustomerField.FIRST_NAME.getName());
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        DomainChecker.checkNotNull(lastName, CustomerField.LAST_NAME.getName());
        this.lastName = lastName;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        DomainChecker.checkNotNull(passportId, CustomerField.PASSPORT_ID.getName());
        this.passportId = passportId;
    }

    public String getIdentifierNumber() {
        return identifierNumber;
    }

    public void setIdentifierNumber(String identifierNumber) {
        DomainChecker.checkNotNull(identifierNumber, CustomerField.IDENTIFIER_NUMBER.getName());
        this.identifierNumber = identifierNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        DomainChecker.checkNotNull(country, CustomerField.COUNTRY.getName());
        this.country = country;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        DomainChecker.checkNotNull(nationality, CustomerField.NATIONALITY.getName());
        this.nationality = nationality;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        DomainChecker.checkNotNull(birthday, CustomerField.BIRTHDAY.getName());
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        DomainChecker.checkNotNull(gender, CustomerField.GENDER.getName());
        this.gender = gender;
    }

    public Set<RoomAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<RoomAssignment> assignments) {
        this.assignments = assignments;
    }

    public static Set<Field> getAllFields() {
        return Set.of(CustomerField.values());
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return identifierNumber.equals(customer.identifierNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifierNumber);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", identifierNumber='" + identifierNumber + '\'' +
                '}';
    }
}
