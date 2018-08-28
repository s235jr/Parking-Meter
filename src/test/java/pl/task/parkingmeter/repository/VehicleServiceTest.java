//package pl.task.parkingmeter.repository;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import pl.task.parkingmeter.entity.Vehicle;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//
//@RunWith(MockitoJUnitRunner.class)
//public class VehicleServiceTest {
//
//    private final String REG_NUMBER_FOR_REGULAR = "QWERTY";
//    private final String REG_NUMBER_FOR_PAID_EARLIER = "ASDFG";
//    private final String REG_NUMBER_FOR_DISABLED = "ZXCVB";
//
//    private static final Logger log =
//            LoggerFactory.getLogger(VehicleServiceTest.class);
//
//    private VehicleService service;
//
//    @Mock
//    private VehicleRepository repository;
//
//    @Before
//    public void setUp() {
//        service = new VehicleServiceImpl(repository);
//
//    }
//
//    @Test
//    public void findVehicleByRegNumberAndIsPaidFalse() {
//
//        Vehicle vehicleRegular = new Vehicle();
//        vehicleRegular.setRegNumber(REG_NUMBER_FOR_REGULAR);
//        vehicleRegular.setOwnerDisabled(false);
//        LocalDateTime localDateTime = LocalDateTime
//                .of(2018, 8, 20, 7, 0, 0);
//
//        vehicleRegular.setPayDate(localDateTime);
//
//        Mockito.when(repository.findVehicleByRegNumberAndIsPaidFalse(REG_NUMBER_FOR_REGULAR))
//                .thenReturn(Optional.ofNullable(vehicleRegular));
//
//        Optional<Vehicle> result = service.findVehicleByRegNumberAndIsPaidFalse(REG_NUMBER_FOR_REGULAR);
//        System.out.println(result.toString());
//
//        assertEquals(REG_NUMBER_FOR_REGULAR, result.get().getRegNumber());
//
//    }
//
//
//
//}