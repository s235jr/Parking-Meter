package pl.task.parkingmeter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.task.parkingmeter.entity.Profit;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.Vehicle;
import pl.task.parkingmeter.exception.InvalidDateException;
import pl.task.parkingmeter.exception.InvalidRegNumberException;
import pl.task.parkingmeter.exception.VehicleAddedEarlierException;
import pl.task.parkingmeter.exception.VehicleNotFoundException;
import pl.task.parkingmeter.repository.RatesService;
import pl.task.parkingmeter.repository.ValueRateService;
import pl.task.parkingmeter.repository.VehicleService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/")
@EnableWebMvc
public class VehicleController {

    private final VehicleService vehicleService;
    private final RatesService ratesService;
    private final ValueRateService valueRateService;

    private final String DATE_REGEX = "([2][0-9]{3})-([0][1-9]|[1][0-2])-([0-2][0-9]|[3][0-1])";
    private final String REGNUMBER_REGEX = "[A-Z0-9]{3,}";

    private final Pattern PATTERN_DATE = Pattern.compile(DATE_REGEX);
    private final Pattern PATTERN_REGNUMBER = Pattern.compile(REGNUMBER_REGEX);

    @Autowired
    public VehicleController(VehicleService vehicleService, RatesService ratesService, ValueRateService valueRateService) {
        this.vehicleService = vehicleService;
        this.ratesService = ratesService;
        this.valueRateService = valueRateService;
    }


    @GetMapping("")
    public List<Vehicle> showAllVehicles() {
        return vehicleService.findVehiclesByPaidFalse();
    }

    @PostMapping("{regNumber}")
    public Vehicle runParkMeter(@PathVariable String regNumber, @RequestParam(required = false) boolean disabled) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle vehicle = vehicleService.findVehicleByRegNumberAndPaidFalse(validRegNumber).orElse(new Vehicle());

        if (vehicle.getId() == 0) {
            vehicle.setRegNumber(validRegNumber);
            vehicle.setOwnerDisabled(disabled);
            vehicle.setRunDate(LocalDateTime.now());
            vehicleService.addVehicle(vehicle);
        } else {
            throw new VehicleAddedEarlierException(regNumber);
        }

        return vehicle;
    }

    @GetMapping("{regNumber}")
    public Vehicle checkBill(@PathVariable String regNumber, @RequestParam(required = false, defaultValue = "PLN") String currency) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle vehicle = vehicleService.findVehicleByRegNumberAndPaidFalse(validRegNumber)
                .orElseThrow(() -> new VehicleNotFoundException(validRegNumber));

        if (vehicle != null) {
            vehicle.setCurrency(currency);
            vehicle.setBill(getValueToPay(currency, vehicle));
        }

        return vehicle;
    }

    @DeleteMapping("{regNumber}")
    public Vehicle pay(@PathVariable String regNumber, @RequestParam(required = false, defaultValue = "PLN") String currency) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle vehicle = vehicleService.findVehicleByRegNumberAndPaidFalse(validRegNumber).orElseThrow(() -> new VehicleNotFoundException(validRegNumber));

        if (vehicle != null) {
            vehicle.setPayDate(LocalDateTime.now());
            vehicle.setCurrency(currency);
            vehicle.setBill(getValueToPay(currency, vehicle));
            vehicle.setPaid(true);
            vehicleService.addVehicle(vehicle);
        }

        return vehicle;
    }

    @PutMapping("{date}")
    public List<Profit> checkProfit(@PathVariable String date) throws RuntimeException {
        return countProfitForDate(date);
    }

    private List<Profit> countProfitForDate(@PathVariable String date) {

        List<Profit> listOfProfit = new ArrayList<>();
        Matcher matcher = PATTERN_DATE.matcher(date);

        if (matcher.matches()) {

            LocalDateTime[] dates = returnStartAndEndDate(date);
            List<String> allCurrency = vehicleService.getAllCurrency();

            allCurrency.stream().forEach(currency -> {
                Profit profit = new Profit();
                profit.setDate(dates[1].toLocalDate());
                profit.setCurrency(currency);
                profit.setValue(countProfitForCurrency(dates, currency));
                listOfProfit.add(profit);});

        } else {
            throw new InvalidDateException(date);
        }
        return listOfProfit;
    }

    private BigDecimal countProfitForCurrency(LocalDateTime[] dates, String currency) {

        List<Vehicle> vehicles = vehicleService.findVehiclesByPayDateBetweenAndCurrency(dates[0], dates[1], currency);
        BigDecimal profit = new BigDecimal(0);

        if (!vehicles.isEmpty() || vehicles != null) {
            for (Vehicle vehicle : vehicles) {
                profit = profit.add(vehicle.getBill());
            }
        }
        return profit;
    }

    private LocalDateTime[] returnStartAndEndDate(String date) {

        LocalDateTime[] dates = new LocalDateTime[2];

        String[] parsedDate = date.split("-");

        dates[0] = LocalDateTime
                .of(Integer.valueOf(parsedDate[0]), Integer.valueOf(parsedDate[1]), Integer.valueOf(parsedDate[2]), 0, 0, 0);

        dates[1] = LocalDateTime
                .of(Integer.valueOf(parsedDate[0]), Integer.valueOf(parsedDate[1]), Integer.valueOf(parsedDate[2]), 23, 59, 59);

        return dates;
    }

    private String checkRegNumber(String regNumber) {

        regNumber = regNumber.replaceAll(" ", "").toUpperCase();

        Matcher matcher = PATTERN_REGNUMBER.matcher(regNumber);

        if (matcher.matches()) {
            return regNumber;
        } else {
            throw new InvalidRegNumberException(regNumber);
        }
    }

    private long checkHours(LocalDateTime createdDate) {

        LocalDateTime startTime = createdDate;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime actualTime = timestamp.toLocalDateTime();

        return ChronoUnit.HOURS.between(startTime, actualTime) + 1;
    }

    private BigDecimal getValueToPay(String currency, Vehicle vehicle) {

        long hours = checkHours(vehicle.getRunDate());
        BigDecimal valueToPay;

        if (hours > 24) {
            valueToPay = BigDecimal.valueOf(5000);
        } else {
            String type;

            if (vehicle.isOwnerDisabled()) {
                type = "disabled";
            } else {
                type = "regular";
            }

            Rates rates = ratesService.findRatesByTypeAndCurrency(type, currency);
            valueToPay = valueRateService.findValueRateByHoursAndRatesId(hours, rates).getValue();

        }
        return valueToPay;
    }

}
