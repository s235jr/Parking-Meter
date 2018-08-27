package pl.task.parkingmeter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pl.task.parkingmeter.entity.Profit;
import pl.task.parkingmeter.entity.Rate;
import pl.task.parkingmeter.entity.Vehicle;
import pl.task.parkingmeter.exception.InvalidDateException;
import pl.task.parkingmeter.exception.InvalidRegNumberException;
import pl.task.parkingmeter.exception.VehicleAddedEarlierException;
import pl.task.parkingmeter.exception.VehicleNotFoundException;
import pl.task.parkingmeter.repository.VehicleService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/vehicles")
@EnableWebMvc
public class VehicleController {

    private VehicleService service;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.service = vehicleService;
    }


    @GetMapping("")
    public List<Vehicle> showAllVehicles() {
        return service.findVehiclesByIsPaidFalse();
    }

    @PostMapping("/{regNumber}")
    public Vehicle runParkMeter(@PathVariable String regNumber, @RequestParam(required = false) boolean disabled) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle vehicle = service.findVehicleByRegNumberAndIsPaidFalse(validRegNumber).orElse(new Vehicle());

        if (vehicle.getId() == 0) {
            vehicle.setRegNumber(validRegNumber);
            vehicle.setOwnerDisabled(disabled);
            vehicle.setCreatedDate(LocalDateTime.now());
            service.addVehicle(vehicle);
        } else {
            throw new VehicleAddedEarlierException(regNumber);
        }

        return vehicle;
    }

    @GetMapping("/{regNumber}")
    public Vehicle checkBill(@PathVariable String regNumber, @RequestParam(required = false, defaultValue = "PLN") String currency) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle vehicle = service.findVehicleByRegNumberAndIsPaidFalse(validRegNumber)
                .orElseThrow(() -> new VehicleNotFoundException(validRegNumber));

        if (vehicle != null) {

            vehicle.setBill(getValueToPay(currency, vehicle));
        }

        return vehicle;
    }

    @DeleteMapping("/{regNumber}")
    public Vehicle pay(@PathVariable String regNumber, @RequestParam(required = false, defaultValue = "PLN") String currency) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle vehicle = service.findVehicleByRegNumberAndIsPaidFalse(validRegNumber).orElseThrow(() -> new VehicleNotFoundException(validRegNumber));

        if (vehicle != null) {
            vehicle.setPayDate(LocalDateTime.now());
            vehicle.setBill(getValueToPay(currency, vehicle));
            vehicle.setIsPaid(true);
            service.addVehicle(vehicle);
        }

        return vehicle;
    }

    @PutMapping("/{date}")
    public Profit checkProfit(@PathVariable String date) throws RuntimeException {
        return countProfitForDate(date);
    }

    private Profit countProfitForDate(@PathVariable String date) {
        Profit profit = new Profit();
        String regex = "([2][0-9]{3})-([0][1-9]|[1][0-2])-([0-2][0-9]|[3][0-1])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        if (matcher.matches()) {
            String[] parsedDate = date.split("-");

            LocalDateTime startDate = LocalDateTime
                    .of(Integer.valueOf(parsedDate[0]), Integer.valueOf(parsedDate[1]), Integer.valueOf(parsedDate[2]), 0, 0, 0);

            LocalDateTime endDate = LocalDateTime
                    .of(Integer.valueOf(parsedDate[0]), Integer.valueOf(parsedDate[1]), Integer.valueOf(parsedDate[2]), 23, 59, 59);

            List<Vehicle> vehicleByPayDate = service.findVehiclesByPayDateBetween(startDate, endDate);
            profit.setDate(startDate.toLocalDate());
            if (!vehicleByPayDate.isEmpty() || vehicleByPayDate != null) {
                BigDecimal dailyProfit = BigDecimal.valueOf(0);
                for (Vehicle vehicle : vehicleByPayDate) {
                    dailyProfit = dailyProfit.add(vehicle.getBill());
                }
                profit.setValue(dailyProfit);
            }
        } else {
            throw new InvalidDateException(date);
        }
        return profit;
    }

    private String checkRegNumber(String regNumber) {

        regNumber = regNumber.replaceAll(" ", "").toUpperCase();
        String regex = "[A-Z0-9]{3,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(regNumber);

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


        long hours = checkHours(vehicle.getCreatedDate());
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


            List<Rate> ratesList = Rate.getRatesList();

            System.out.println("HHHHHHHHHHHHHHHHHHHHHHHH" + ratesList);

            Rate ratesHours = ratesList.stream().filter(rates -> rates.getType()
                    .equals(type) && rates.getCurrency().equals(currency)).findFirst().get();

            valueToPay = ratesHours.getRates().get((int) hours);
        }
        return valueToPay;
    }

}