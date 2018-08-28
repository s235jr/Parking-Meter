package pl.task.parkingmeter.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import pl.task.parkingmeter.entity.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VehicleRepositoryTest {

    private final String REG_NUMBER_FOR_REGULAR = "QWERTY";
    private final String REG_NUMBER_FOR_PAID_EARLIER = "ASDFG";
    private final String REG_NUMBER_FOR_DISABLED = "ZXCVB";

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Before
    public void setup() {
        Vehicle vehicleRegular = new Vehicle();
        vehicleRegular.setRegNumber(REG_NUMBER_FOR_REGULAR);
        vehicleRegular.setOwnerDisabled(false);
        LocalDateTime localDateTime = LocalDateTime.of(2018, 8, 20, 7, 0, 0);
        vehicleRegular.setPayDate(localDateTime);
        entityManager.persist(vehicleRegular);

        Vehicle vehiclePaidEarlier = new Vehicle();
        vehiclePaidEarlier.setRegNumber(REG_NUMBER_FOR_PAID_EARLIER);
        vehiclePaidEarlier.setIsPaid(true);
        localDateTime = LocalDateTime.of(2018, 8, 21, 7, 0, 0);
        vehiclePaidEarlier.setPayDate(localDateTime);
        entityManager.persist(vehiclePaidEarlier);

        Vehicle vehicleWithDisabledOwner = new Vehicle();
        vehicleWithDisabledOwner.setRegNumber(REG_NUMBER_FOR_DISABLED);
        vehicleWithDisabledOwner.setIsPaid(false);
        vehicleWithDisabledOwner.setOwnerDisabled(true);
        localDateTime = LocalDateTime.of(2018, 8, 22, 7, 0, 0);
        vehicleWithDisabledOwner.setPayDate(localDateTime);
        entityManager.persist(vehicleWithDisabledOwner);

    }

    @Test
    public void findVehiclesByIsPaidFalse() {
        List<Vehicle> result = vehicleRepository.findVehiclesByPaidFalse();
        assertEquals(2, result.size());
        assertEquals(REG_NUMBER_FOR_REGULAR, result.get(0).getRegNumber());
        assertEquals(REG_NUMBER_FOR_DISABLED, result.get(1).getRegNumber());
    }

    @Test
    public void findVehicleByRegNumberAndIsPaidFalse() {

        Optional<Vehicle> resultForRegular = vehicleRepository.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_FOR_REGULAR);
        assertEquals(REG_NUMBER_FOR_REGULAR, resultForRegular.get().getRegNumber());

        Optional<Vehicle> resultForPaidEarlier = vehicleRepository.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_FOR_PAID_EARLIER);
        assertEquals("Optional.empty", resultForPaidEarlier.toString());

    }

    @Test
    public void findVehiclesByPayDateBetween() {

        LocalDateTime start = LocalDateTime.of(2018, 8, 20, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2018, 8, 23, 23, 59, 59);
        List<Vehicle> result = vehicleRepository.findVehiclesByPayDateBetween(start, end);

        assertEquals(3, result.size());
        assertEquals(REG_NUMBER_FOR_REGULAR, result.get(0).getRegNumber());
        assertEquals(REG_NUMBER_FOR_PAID_EARLIER, result.get(1).getRegNumber());
        assertEquals(REG_NUMBER_FOR_DISABLED, result.get(2).getRegNumber());

        end = LocalDateTime.of(2018, 8, 20, 6, 59, 59);
        result = vehicleRepository.findVehiclesByPayDateBetween(start, end);
        assertEquals(0, result.size());
    }


}