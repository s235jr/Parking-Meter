package pl.task.parkingmeter.repository;

import org.junit.Before;
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

    private final Rates REGULAR_RATE = new Rates();
    private final Rates DISABLED_RATE = new Rates();

    private final ValueRate VALUE_RATE_1 = new ValueRate();
    private final ValueRate VALUE_RATE_2 = new ValueRate();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ValueRateRepository valueRateRepository;

    @Autowired
    private RatesRepository ratesRepository;

    @Before
    public void setup() {

        REGULAR_RATE.setCurrency("PLN");
        REGULAR_RATE.setType("regular");
        entityManager.persist(REGULAR_RATE);

        DISABLED_RATE.setCurrency("PLN");
        DISABLED_RATE.setType("disabled");
        entityManager.persist(DISABLED_RATE);

        VALUE_RATE_1.setRates(REGULAR_RATE);
        VALUE_RATE_1.setHours(5);
        VALUE_RATE_1.setValue(BigDecimal.valueOf(17.25));
        entityManager.persist(VALUE_RATE_1);

        VALUE_RATE_2.setRates(DISABLED_RATE);
        VALUE_RATE_2.setHours(12);
        VALUE_RATE_2.setValue(BigDecimal.valueOf(64.30));
        entityManager.persist(VALUE_RATE_2);

    }

    @Test
    public void findValueRateByHoursAndRatesTestONE() {

        Rates ratesFirst = ratesRepository.findByTypeAndCurrency("regular", "PLN");

        ValueRate resultFirst = valueRateRepository.findValueRateByHoursAndRates(5, ratesFirst);

        assertEquals(resultFirst.getValue(), VALUE_RATE_1.getValue());
        assertEquals(resultFirst.getHours(), VALUE_RATE_1.getHours());

    }

    @Test
    public void findValueRateByHoursAndRatesTestTWO() {

        Rates ratesSecond = ratesRepository.findByTypeAndCurrency("disabled", "PLN");

        ValueRate resultSecond = valueRateRepository.findValueRateByHoursAndRates(12, ratesSecond);

        assertEquals(resultSecond.getValue(), VALUE_RATE_2.getValue());
        assertEquals(resultSecond.getHours(), VALUE_RATE_2.getHours());
    }
}
