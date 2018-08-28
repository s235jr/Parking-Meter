package pl.task.parkingmeter.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ValueRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long hours;
    private BigDecimal value;
    @ManyToOne
    private Rates rates;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "ValueRate{" +
                "id=" + id +
                ", hours=" + hours +
                ", value=" + value +
                ", rates=" + rates +
                '}';
    }
}
