package pl.task.parkingmeter.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.Vehicle;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RatesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RatesRepository ratesRepository;

    @Test
    public void findByTypeAndCurrency() {

        Rates regularPLN = new Rates();
        regularPLN.setType("regular");
        regularPLN.setCurrency("PLN");
        entityManager.persist(regularPLN);

        Rates disabledPLN = new Rates();
        disabledPLN.setType("disabled");
        disabledPLN.setCurrency("PLN");
        entityManager.persist(disabledPLN);

        Rates disabledEUR = new Rates();
        disabledEUR.setType("disabled");
        disabledEUR.setCurrency("EUR");
        entityManager.persist(disabledEUR);

        Rates resultForRegularPLN = ratesRepository.findByTypeAndCurrency("regular", "PLN");
        Rates resultForDisabledEUR = ratesRepository.findByTypeAndCurrency("disabled", "EUR");

        assertEquals(resultForRegularPLN.getType(), regularPLN.getType());
        assertEquals(resultForRegularPLN.getCurrency(), regularPLN.getCurrency());

        assertEquals(resultForDisabledEUR.getType(), disabledEUR.getType());
        assertEquals(resultForDisabledEUR.getCurrency(), disabledEUR.getCurrency());
    }



}