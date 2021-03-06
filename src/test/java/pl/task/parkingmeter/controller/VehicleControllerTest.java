package pl.task.parkingmeter.controller;

import org.junit.Before;
import org.junit.BeforeClass;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {VehicleController.class})
@WebAppConfiguration
public class VehicleControllerTest {

    private static final String REG_NUMBER_1 = "ASDFG";
    private static final String REG_NUMBER_2 = "ZXCVB";

    private static final Vehicle VEHICLE_1 = new Vehicle();
    private static final Vehicle VEHICLE_2 = new Vehicle();

    private static final Rates RATES = new Rates();

    private static final ValueRate VALUE_RATE = new ValueRate();

    private static final List<Vehicle> LIST_VEHICLE_PAID_BY_PLN = new ArrayList<>();
    private static final List<Vehicle> LIST_VEHICLE_PAID_BY_EUR = new ArrayList<>();
    private static final List<Vehicle> LIST_VEHICLE_PAID_BY_USD = new ArrayList<>();

    private static final LocalDateTime START_DATE = LocalDateTime.of(2018, 8, 28, 0, 0, 0);
    private static final LocalDateTime END_DATE = LocalDateTime.of(2018, 8, 28, 23, 59, 59);

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

        VEHICLE_1.setRegNumber(REG_NUMBER_1);

        VEHICLE_2.setRegNumber(REG_NUMBER_2);

        RATES.setCurrency("PLN");
        RATES.setType("regular");

        VALUE_RATE.setRates(RATES);

    }

    @BeforeClass
    public static void setupForCheckProfitTest() {

        Vehicle firstVehicle = new Vehicle();
        firstVehicle.setRegNumber(REG_NUMBER_1);
        firstVehicle.setPayDate(LocalDateTime.of(2018, 8, 28, 11, 11, 11));
        firstVehicle.setCurrency("PLN");
        firstVehicle.setBill(BigDecimal.valueOf(25));

        Vehicle secondVehicle = new Vehicle();
        secondVehicle.setRegNumber(REG_NUMBER_1);
        secondVehicle.setPayDate(LocalDateTime.of(2018, 8, 28, 5, 5, 5));
        secondVehicle.setCurrency("EUR");
        secondVehicle.setBill(BigDecimal.valueOf(5));

        Vehicle thirdVehicle = new Vehicle();
        thirdVehicle.setRegNumber(REG_NUMBER_1);
        thirdVehicle.setPayDate(LocalDateTime.of(2018, 8, 28, 3, 5, 7));
        thirdVehicle.setCurrency("PLN");
        thirdVehicle.setBill(BigDecimal.valueOf(15));

        LIST_VEHICLE_PAID_BY_PLN.add(firstVehicle);
        LIST_VEHICLE_PAID_BY_PLN.add(thirdVehicle);
        LIST_VEHICLE_PAID_BY_EUR.add(secondVehicle);
    }

    @Test
    public void showAllVehicles() throws Exception {

        List<Vehicle> vehicles = Arrays.asList(VEHICLE_1, VEHICLE_2);

        when(this.vehicleService.findVehiclesByPaidFalse()).thenReturn(vehicles);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$[1].regNumber").value(REG_NUMBER_2))
                .andDo(print());
    }

    @Test
    public void addVehicle() throws Exception {

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_1)).thenReturn(java.util.Optional.of(VEHICLE_1));

        mockMvc.perform(post("/{regNumber}", REG_NUMBER_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.ownerDisabled").value(false))
                .andDo(print());
    }

    @Test
    public void addVehicleWithDisableOwner() throws Exception {

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_1)).thenReturn(java.util.Optional.of(VEHICLE_1));

        mockMvc.perform(post("/{regNumber}?disabled=true", REG_NUMBER_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.ownerDisabled").value(true))
                .andDo(print());
    }

    @Test
    public void addVehicleButWasAddedEarlier() throws Exception {

        VEHICLE_1.setId(1);

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_1)).thenReturn(java.util.Optional.of(VEHICLE_1));

        mockMvc.perform(post("/{regNumber}", REG_NUMBER_1))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Vehicle was added earlier!"))
                .andDo(print());
    }

    @Test
    public void addVehicleButInvalidRegNumber() throws Exception {

        String invalidRegNumber = "ASD###";

        mockMvc.perform(post("/{regNumber}", invalidRegNumber))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Invalid registration number!"))
                .andDo(print());

    }

    @Test
    public void getInfoAboutVehicleWhenOk() throws Exception {

        VEHICLE_1.setRunDate(LocalDateTime.now().minusHours(4).minusMinutes(30));
        VEHICLE_1.setOwnerDisabled(false);
        VEHICLE_1.setPaid(false);

        VALUE_RATE.setHours(5);
        VALUE_RATE.setValue(BigDecimal.valueOf(17.25));

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_1)).thenReturn(java.util.Optional.ofNullable(VEHICLE_1));

        when(this.ratesService.findRatesByTypeAndCurrency("regular", "PLN")).thenReturn(RATES);

        when(this.valueRateService.findValueRateByHoursAndRatesId(5, RATES)).thenReturn(VALUE_RATE);

        mockMvc.perform(get("/{regNumber}?currency=PLN", REG_NUMBER_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.bill").value(VALUE_RATE.getValue())).andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.paid").value(false))
                .andExpect(jsonPath("$.currency").value("PLN"))
                .andExpect(jsonPath("$.payDate").isEmpty())
                .andDo(print());

    }

    @Test
    public void getInfoAboutVehicleWhenOkAndCurrencyIsEUR() throws Exception {

        VEHICLE_1.setRunDate(LocalDateTime.now().minusHours(4).minusMinutes(30));
        VEHICLE_1.setOwnerDisabled(false);
        VEHICLE_1.setPaid(false);

        RATES.setCurrency("EUR");

        VALUE_RATE.setHours(5);
        VALUE_RATE.setValue(BigDecimal.valueOf(12.25));

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_1)).thenReturn(java.util.Optional.ofNullable(VEHICLE_1));

        when(this.ratesService.findRatesByTypeAndCurrency("regular", "EUR")).thenReturn(RATES);

        when(this.valueRateService.findValueRateByHoursAndRatesId(5, RATES)).thenReturn(VALUE_RATE);

        mockMvc.perform(get("/{regNumber}?currency=EUR", REG_NUMBER_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.bill").value(VALUE_RATE.getValue())).andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.paid").value(false))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.payDate").isEmpty())
                .andDo(print());

    }

    @Test
    public void getInfoAboutVehicleButVehicleNotFound() throws Exception {

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_1)).thenReturn(java.util.Optional.ofNullable(null));

        mockMvc.perform(get("/{regNumber}?currency=PLN", REG_NUMBER_1))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Vehicle not found!"))
                .andDo(print());

    }

    @Test
    public void getInfoAboutVehicleButInvalidRegNumber() throws Exception {

        String invalidRegNumber = "RTY^^&";

        mockMvc.perform(get("/{regNumber}", invalidRegNumber))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Invalid registration number!"))
                .andDo(print());

    }

    @Test
    public void payForParkingWhenOk() throws Exception {

        VEHICLE_1.setRunDate(LocalDateTime.now().minusHours(4).minusMinutes(30));
        VEHICLE_1.setOwnerDisabled(false);
        VEHICLE_1.setPaid(false);

        VALUE_RATE.setHours(7);
        VALUE_RATE.setValue(BigDecimal.valueOf(25));

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_1)).thenReturn(java.util.Optional.ofNullable(VEHICLE_1));

        when(this.ratesService.findRatesByTypeAndCurrency("regular", "PLN")).thenReturn(RATES);

        when(this.valueRateService.findValueRateByHoursAndRatesId(5, RATES)).thenReturn(VALUE_RATE);

        mockMvc.perform(delete("/{regNumber}", REG_NUMBER_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.bill").value(VALUE_RATE.getValue())).andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.paid").value(true))
                .andExpect(jsonPath("$.payDate").isNotEmpty())
                .andDo(print());
    }

    @Test
    public void payForParkingWhenOkAndNotDefaultCurrency() throws Exception {

        VEHICLE_1.setRunDate(LocalDateTime.now().minusHours(4).minusMinutes(30));
        VEHICLE_1.setOwnerDisabled(false);
        VEHICLE_1.setPaid(false);

        RATES.setCurrency("EUR");

        VALUE_RATE.setHours(7);
        VALUE_RATE.setValue(BigDecimal.valueOf(4));

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_1)).thenReturn(java.util.Optional.ofNullable(VEHICLE_1));

        when(this.ratesService.findRatesByTypeAndCurrency("regular", "EUR")).thenReturn(RATES);

        when(this.valueRateService.findValueRateByHoursAndRatesId(5, RATES)).thenReturn(VALUE_RATE);

        mockMvc.perform(delete("/{regNumber}?currency=EUR", REG_NUMBER_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.bill").value(VALUE_RATE.getValue())).andExpect(jsonPath("$.regNumber").value(REG_NUMBER_1))
                .andExpect(jsonPath("$.paid").value(true))
                .andExpect(jsonPath("$.payDate").isNotEmpty())
                .andDo(print());
    }

    @Test
    public void payForParkingButVehicleNotFound() throws Exception {

        when(this.vehicleService.findVehicleByRegNumberAndPaidFalse(REG_NUMBER_1)).thenReturn(java.util.Optional.ofNullable(null));

        mockMvc.perform(delete("/{regNumber}", REG_NUMBER_1))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Vehicle not found!"))
                .andDo(print());
    }

    @Test
    public void checkProfit() throws Exception {

        String date = "2018-08-28";

        List<String> listOfCurrency = Arrays.asList("PLN", "EUR", "USD");

        when(this.vehicleService.getAllCurrency()).thenReturn(listOfCurrency);

        when(this.vehicleService.findVehiclesByPayDateBetweenAndCurrency(START_DATE, END_DATE, "PLN")).thenReturn(LIST_VEHICLE_PAID_BY_PLN);

        when(this.vehicleService.findVehiclesByPayDateBetweenAndCurrency(START_DATE, END_DATE, "EUR")).thenReturn(LIST_VEHICLE_PAID_BY_EUR);

        when(this.vehicleService.findVehiclesByPayDateBetweenAndCurrency(START_DATE, END_DATE, "USD")).thenReturn(LIST_VEHICLE_PAID_BY_USD);

        mockMvc.perform(put("/{date}", date))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].value").value(40))
                .andExpect(jsonPath("$[0].currency").value("PLN"))
                .andExpect(jsonPath("$[1].value").value(5))
                .andExpect(jsonPath("$[1].currency").value("EUR"))
                .andExpect(jsonPath("$[2].value").value(0))
                .andExpect(jsonPath("$[2].currency").value("USD"))
                .andDo(print());
    }

    @Test
    public void checkProfitButInvalidDateFormat() throws Exception {

        String date = "2018-38-28";

        mockMvc.perform(put("/{date}", date))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Invalid date format!"))
                .andDo(print());

    }

}