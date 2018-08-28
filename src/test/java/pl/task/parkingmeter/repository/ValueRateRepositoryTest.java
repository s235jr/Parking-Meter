package pl.task.parkingmeter.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.ValueRate;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ValueRateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ValueRateRepository valueRateRepository;

    @Autowired
    private RatesRepository ratesRepository;

    @Test
    public void findValueRateByHoursAndRates() {

        Rates regularPLN = new Rates();
        regularPLN.setCurrency("PLN");
        regularPLN.setType("regular");
        entityManager.persist(regularPLN);

        Rates disabledPLN = new Rates();
        disabledPLN.setCurrency("PLN");
        disabledPLN.setType("disabled");
        entityManager.persist(disabledPLN);

        ValueRate valueRateFirst = new ValueRate();
        valueRateFirst.setRates(regularPLN);
        valueRateFirst.setHours(5);
        valueRateFirst.setValue(BigDecimal.valueOf(17.25));
        entityManager.persist(valueRateFirst);

        ValueRate valueRateSecond = new ValueRate();
        valueRateSecond.setRates(disabledPLN);
        valueRateSecond.setHours(12);
        valueRateSecond.setValue(BigDecimal.valueOf(64.30));
        entityManager.persist(valueRateSecond);

        Rates ratesFirst = ratesRepository.findByTypeAndCurrency("regular", "PLN");
        Rates ratesSecond = ratesRepository.findByTypeAndCurrency("disabled", "PLN");

        ValueRate resultFirst = valueRateRepository.findValueRateByHoursAndRates(5, ratesFirst);
        assertEquals(resultFirst.getValue(), valueRateFirst.getValue());
        assertEquals(resultFirst.getHours(), valueRateFirst.getHours());

        ValueRate resultSecond = valueRateRepository.findValueRateByHoursAndRates(12, ratesSecond);
        assertEquals(resultSecond.getValue(), valueRateSecond.getValue());
        assertEquals(resultSecond.getHours(), valueRateSecond.getHours());

    }
}
