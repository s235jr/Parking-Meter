package pl.task.parkingmeter.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    private String currency;
    private long hours;
    private BigDecimal rate;
    private BigDecimal sumToThisHour;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getSumToThisHour() {
        return sumToThisHour;
    }

    public void setSumToThisHour(BigDecimal sumToThisHour) {
        this.sumToThisHour = sumToThisHour;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", currency='" + currency + '\'' +
                ", hours=" + hours +
                ", rate=" + rate +
                ", sumToThisHour=" + sumToThisHour +
                '}';
    }
}
