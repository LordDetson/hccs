package by.mitso.berezkina.domain;

import java.time.LocalDateTime;
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.javamoney.moneta.Money;

import by.mitso.berezkina.domain.Tariff.TariffField;
import by.mitso.berezkina.domain.exception.DomainChecker;

@Entity
@Table(name = "payments")
public class Payment extends Persistent<Long> implements MonetaryAmount {

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "currency", nullable = false)
    private String currencyCode;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_assignment_id", referencedColumnName = "id", nullable = false)
    private RoomAssignment assignment;

    @Transient
    private MonetaryAmount monetaryAmount;

    public enum PaymentField implements Field {
        AMOUNT("amount", "сумма", Number.class, true, Payment::getNumber,
                (tariff, value) -> tariff.setAmount((Double) value)),
        CURRENCY_CODE("currencyCode", "валюта", String.class, true, Payment::getCurrencyCode,
                (tariff, value) -> tariff.setCurrencyCode((String) value)),
        DATE_TIME("dateTime", "дата и время", LocalDateTime.class, true, Payment::getDateTime,
                null),
        ASSIGNMENT("assignment", "назначен", RoomAssignment.class, false, Payment::getAssignment,
                null),
        ;

        private final String name;
        private final String caption;
        private final Class<?> type;
        private final boolean required;
        private final Function<Payment, ?> getter;
        private final BiConsumer<Payment, Object> setter;

        PaymentField(String name, String caption, Class<?> type, boolean required, Function<Payment, ?> getter,
                BiConsumer<Payment, Object> setter) {
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
            return getter.apply((Payment) component);
        }

        @Override
        public void setValue(Object component, Object value) {
            if(setter == null) {
                DomainChecker.throwNonEditableField(name);
            }
            setter.accept((Payment) component, value);
        }
    }

    protected Payment() {
        // for ORM
    }

    public Payment(Number amount, String currencyCode, LocalDateTime dateTime) {
        this.monetaryAmount = Money.of(amount, currencyCode);
        this.dateTime = dateTime;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public RoomAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(RoomAssignment assignment) {
        this.assignment = assignment;
    }

    public static Set<Field> getAllFields() {
        return Set.of(PaymentField.values());
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
        Payment that = (Payment) o;
        return monetaryAmount.equals(that.monetaryAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monetaryAmount);
    }

    @Override
    public String toString() {
        return monetaryAmount.toString();
    }
}
