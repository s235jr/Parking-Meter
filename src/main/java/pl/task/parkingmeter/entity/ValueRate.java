package pl.task.parkingmeter.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
public class ValueRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long hours;
    private BigDecimal value;
    @ManyToOne
    private Rates rates;

}
