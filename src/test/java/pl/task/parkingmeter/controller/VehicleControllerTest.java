package pl.task.parkingmeter.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.ValueRate;
import pl.task.parkingmeter.entity.Vehicle;
import pl.task.parkingmeter.repository.RatesService;
import pl.task.parkingmeter.repository.ValueRateService;
import pl.task.parkingmeter.repository.VehicleService;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {VehicleController.class})
@WebAppConfiguration
public class VehicleControllerTest {

    private final String REG_NUMBER = "ASDFG";
    private final String REG_NUMBER_2 = "ZXCVB";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private RatesService ratesService;

    @MockBean
    private ValueRateService valueRateService;

    @Autowired
    WebApplicationContext webApplicationContext;

    private static MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @Test
    public void showAllVehicles() throws Exception {

        Vehicle vehicle = new Vehicle();
        vehicle.setRegNumber(REG_NUMBER);

        Vehicle vehicle_2 = new Vehicle();
        vehicle_2.setRegNumber(REG_NUMBER_2);

        List<Vehicle> vehicles = Arrays.asList(vehicle, vehicle_2);

        when(this.vehicleService.findVehiclesByPaidFalse()).thenReturn(vehicles);

        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andDo(print());
    }

    @Test
    public void addVehicle() throws Exception {

        Vehicle vehicle = new Vehicle();
        vehicle.setRegNumber(REG_NUMBER);
        vehicle.setId(0);

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER)).thenReturn(java.util.Optional.of(vehicle));

        mockMvc.perform(post("/vehicles/{regNumber}", REG_NUMBER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regNumber").value(REG_NUMBER))
                .andExpect(jsonPath("$.ownerDisabled").value(false))
                .andDo(print());
    }

    @Test
    public void addVehicleWithDisableOwner() throws Exception {

        Vehicle vehicle = new Vehicle();
        vehicle.setRegNumber(REG_NUMBER);
        vehicle.setId(0);

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER)).thenReturn(java.util.Optional.of(vehicle));

        mockMvc.perform(post("/vehicles/{regNumber}?disabled=true", REG_NUMBER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regNumber").value(REG_NUMBER))
                .andExpect(jsonPath("$.ownerDisabled").value(true))
                .andDo(print());
    }

    @Test
    public void addVehicleButWasAddedEarlier() throws Exception {

        Vehicle vehicle = new Vehicle();
        vehicle.setRegNumber(REG_NUMBER);
        vehicle.setId(1);

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER)).thenReturn(java.util.Optional.of(vehicle));

        mockMvc.perform(post("/vehicles/{regNumber}", REG_NUMBER))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Vehicle was added earlier!"))
                .andDo(print());
    }

    @Test
    public void addVehicleButInvalidRegNumber() throws Exception {

        String invalidRegNumber = "ASD###";

        mockMvc.perform(post("/vehicles/{regNumber}", invalidRegNumber))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Invalid registration number!"))
                .andDo(print());

    }

    @Test
    public void getInfoAboutVehicleWhenOk() throws Exception {

        Vehicle vehicle = new Vehicle();
        vehicle.setRegNumber(REG_NUMBER);
        vehicle.setRunDate(LocalDateTime.now().minusHours(4).minusMinutes(30));
        vehicle.setOwnerDisabled(false);
        vehicle.setIsPaid(false);

        Rates regularPLN = new Rates();
        regularPLN.setCurrency("PLN");
        regularPLN.setType("regular");

        ValueRate valueRate = new ValueRate();
        valueRate.setRates(regularPLN);
        valueRate.setHours(5);
        valueRate.setValue(BigDecimal.valueOf(17.25));
        valueRate.setRates(regularPLN);

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER)).thenReturn(java.util.Optional.ofNullable(vehicle));

        when(this.ratesService.findRatesByTypeAndCurrency("regular", "PLN")).thenReturn(regularPLN);

        when(this.valueRateService.findValueRateByHoursAndRatesId(5, regularPLN)).thenReturn(valueRate);

        mockMvc.perform(get("/vehicles/{regNumber}?currency=PLN", REG_NUMBER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regNumber").value(REG_NUMBER))
                .andExpect(jsonPath("$.bill").value(valueRate.getValue())).andExpect(jsonPath("$.regNumber").value(REG_NUMBER))
                .andExpect(jsonPath("$.paid").value(false))
                .andExpect(jsonPath("$.payDate").isEmpty())
                .andDo(print());

    }

}