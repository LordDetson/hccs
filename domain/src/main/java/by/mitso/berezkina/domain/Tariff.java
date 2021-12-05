package by.mitso.berezkina.domain;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.NumberValue;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.javamoney.moneta.Money;

import by.mitso.berezkina.domain.exception.DomainChecker;

@Entity
@Table(name = "tariffs")
public class Tariff extends Persistent<Long> implements MonetaryAmount {

    @Column(name = "number_of_days", nullable = false)
    private Integer numberOfDays;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "currency", nullable = false)
    private String currencyCode;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "tariff", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<RoomAssignment> assignments;

    @Transient
    private MonetaryAmount monetaryAmount;

    public enum TariffField implements Field {
        NUMBER_OF_DAYS("numberOfDays", "количество дней", Integer.class, true, Tariff::getNumberOfDays,
                (tariff, value) -> tariff.setNumberOfDays((Integer) value)),
        DESCRIPTION("description", "описание", String.class, false, Tariff::getDescription,
                (tariff, value) -> tariff.setDescription((String) value)),
        AMOUNT("amount", "стоимость", Number.class, true, Tariff::getNumber,
                (tariff, value) -> tariff.setAmount((Double) value)),
        CURRENCY_CODE("currencyCode", "валюта", String.class, true, Tariff::getCurrencyCode,
                (tariff, value) -> tariff.setCurrencyCode((String) value)),
        ROOM("room", "комната", Room.class, true, Tariff::getRoom,
                null),
        ASSIGNMENTS("assignments", "история назначений", Set.class, false, Tariff::getAssignments,
                null),
        ;
        
        private final String name;
        private final String caption;
        private final Class<?> type;
        private final boolean required;
        private final Function<Tariff, ?> getter;
        private final BiConsumer<Tariff, Object> setter;

        TariffField(String name, String caption, Class<?> type, boolean required, Function<Tariff, ?> getter,
                BiConsumer<Tariff, Object> setter) {
            this.name = name;
            this.caption = caption;
            this.type = type;
            this.required = required;
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
        public boolean isRequired() {
            return required;
        }

        @Override
        public Object getValue(Object component) {
            return getter.apply((Tariff) component);
        }

        @Override
        public void setValue(Object component, Object value) {
            if(setter == null) {
                DomainChecker.throwNonEditableField(name);
            }
            setter.accept((Tariff) component, value);
        }
    }

    protected Tariff() {
        // for ORM
    }

    public Tariff(Integer numberOfDays, Number amount, String currencyCode, Room room) {
        DomainChecker.checkNotNull(numberOfDays, TariffField.NUMBER_OF_DAYS.getName());
        DomainChecker.checkNotNull(amount, TariffField.AMOUNT.getName());
        DomainChecker.checkNotNull(currencyCode, TariffField.CURRENCY_CODE.getName());
        DomainChecker.checkNotNull(room, TariffField.ROOM.getName());
        this.numberOfDays = numberOfDays;
        this.monetaryAmount = Money.of(amount, currencyCode);
        this.room = room;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        DomainChecker.checkNotNull(numberOfDays, TariffField.NUMBER_OF_DAYS.getName());
        this.numberOfDays = numberOfDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return getNumber().numberValue(Double.class);
    }

    public void setAmount(Double amount) {
        DomainChecker.checkNotNull(amount, TariffField.AMOUNT.getName());
        this.monetaryAmount = Money.of(amount, getCurrency());
    }

    private String getCurrencyCode() {
        return monetaryAmount.getCurrency().getCurrencyCode();
    }

    public void setCurrencyCode(String currencyCode) {
        DomainChecker.checkNotNull(currencyCode, TariffField.CURRENCY_CODE.getName());
        monetaryAmount = Money.of(getAmount(), currencyCode);
    }

    public Room getRoom() {
        return room;
    }

    public Set<RoomAssignment> getAssignments() {
        return assignments;
    }

    public static Set<Field> getAllFields() {
        return Set.of(TariffField.values());
    }

    @PostLoad
    private void postLoad() {
        this.monetaryAmount = Monetary.getDefaultAmountFactory()
                .setCurrency(Monetary.getCurrency(currencyCode))
                .setNumber(amount)
                .create();
    }

    @PrePersist
    private void prePersist() {
        amount = getAmount();
        currencyCode = getCurrencyCode();
    }

    @PreUpdate
    private void preUpdate() {
        amount = getAmount();
        currencyCode = getCurrencyCode();
    }

    @PreRemove
    private void preRemove() {
        amount = getAmount();
        currencyCode = getCurrencyCode();
    }

    @Override
    public MonetaryContext getContext() {
        return monetaryAmount.getContext();
    }

    @Override
    public MonetaryAmountFactory<? extends MonetaryAmount> getFactory() {
        return this.monetaryAmount.getFactory();
    }

    @Override
    public boolean isGreaterThan(MonetaryAmount monetaryAmount) {
        return this.monetaryAmount.isGreaterThan(monetaryAmount);
    }

    @Override
    public boolean isGreaterThanOrEqualTo(MonetaryAmount monetaryAmount) {
        return this.monetaryAmount.isGreaterThanOrEqualTo(monetaryAmount);
    }

    @Override
    public boolean isLessThan(MonetaryAmount monetaryAmount) {
        return this.monetaryAmount.isLessThan(monetaryAmount);
    }

    @Override
    public boolean isLessThanOrEqualTo(MonetaryAmount monetaryAmount) {
        return this.monetaryAmount.isLessThanOrEqualTo(monetaryAmount);
    }

    @Override
    public boolean isEqualTo(MonetaryAmount monetaryAmount) {
        return this.monetaryAmount.isEqualTo(monetaryAmount);
    }

    @Override
    public int signum() {
        return this.monetaryAmount.signum();
    }

    @Override
    public MonetaryAmount add(MonetaryAmount monetaryAmount) {
        return this.monetaryAmount.add(monetaryAmount);
    }

    @Override
    public MonetaryAmount subtract(MonetaryAmount monetaryAmount) {
        return this.monetaryAmount.subtract(monetaryAmount);
    }

    @Override
    public MonetaryAmount multiply(long l) {
        return this.monetaryAmount.multiply(l);
    }

    @Override
    public MonetaryAmount multiply(double v) {
        return this.monetaryAmount.multiply(v);
    }

    @Override
    public MonetaryAmount multiply(Number number) {
        return this.monetaryAmount.multiply(number);
    }

    @Override
    public MonetaryAmount divide(long l) {
        return this.monetaryAmount.divide(l);
    }

    @Override
    public MonetaryAmount divide(double v) {
        return this.monetaryAmount.divide(v);
    }

    @Override
    public MonetaryAmount divide(Number number) {
        return this.monetaryAmount.divide(number);
    }

    @Override
    public MonetaryAmount remainder(long l) {
        return this.monetaryAmount.remainder(l);
    }

    @Override
    public MonetaryAmount remainder(double v) {
        return this.monetaryAmount.remainder(v);
    }

    @Override
    public MonetaryAmount remainder(Number number) {
        return this.monetaryAmount.remainder(number);
    }

    @Override
    public MonetaryAmount[] divideAndRemainder(long l) {
        return this.monetaryAmount.divideAndRemainder(l);
    }

    @Override
    public MonetaryAmount[] divideAndRemainder(double v) {
        return this.monetaryAmount.divideAndRemainder(v);
    }

    @Override
    public MonetaryAmount[] divideAndRemainder(Number number) {
        return this.monetaryAmount.divideAndRemainder(number);
    }

    @Override
    public MonetaryAmount divideToIntegralValue(long l) {
        return this.monetaryAmount.divideToIntegralValue(l);
    }

    @Override
    public MonetaryAmount divideToIntegralValue(double v) {
        return this.monetaryAmount.divideToIntegralValue(v);
    }

    @Override
    public MonetaryAmount divideToIntegralValue(Number number) {
        return this.monetaryAmount.divideToIntegralValue(number);
    }

    @Override
    public MonetaryAmount scaleByPowerOfTen(int i) {
        return this.monetaryAmount.scaleByPowerOfTen(i);
    }

    @Override
    public MonetaryAmount abs() {
        return this.monetaryAmount.abs();
    }

    @Override
    public MonetaryAmount negate() {
        return this.monetaryAmount.negate();
    }

    @Override
    public MonetaryAmount plus() {
        return this.monetaryAmount.plus();
    }

    @Override
    public MonetaryAmount stripTrailingZeros() {
        return this.monetaryAmount.stripTrailingZeros();
    }

    @Override
    public int compareTo(MonetaryAmount o) {
        return this.monetaryAmount.compareTo(o);
    }

    @Override
    public CurrencyUnit getCurrency() {
        return this.monetaryAmount.getCurrency();
    }

    @Override
    public NumberValue getNumber() {
        return this.monetaryAmount.getNumber();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Tariff tariff = (Tariff) o;
        return numberOfDays.equals(tariff.numberOfDays) && room.equals(tariff.room) && Objects.equals(monetaryAmount,
                tariff.monetaryAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfDays, room, monetaryAmount);
    }

    @Override
    public String toString() {
        return monetaryAmount.toString();
    }
}
