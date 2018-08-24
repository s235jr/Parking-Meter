package pl.task.parkingmeter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.task.parkingmeter.entity.Profit;
import pl.task.parkingmeter.entity.Vehicle;
import pl.task.parkingmeter.repository.RateRepository;
import pl.task.parkingmeter.repository.VehicleRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/vehicles")
public class HomeController {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    RateRepository rateRepository;

    @GetMapping("")
    public List<Vehicle> showVehicles() {
        return vehicleRepository.findVehiclesByIsPaidFalse();
    }

    @PostMapping("/{regNumber}")
    public Vehicle startPark(@PathVariable String regNumber, @RequestParam(required = false) boolean disabled) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle result = new Vehicle();

        if (validRegNumber != null) {
            Vehicle vehicleFromDB = vehicleRepository.findVehicleByRegNumberAndIsPaidFalse(validRegNumber);
            if (vehicleFromDB == null) {
                Vehicle vehicle = new Vehicle();
                vehicle.setRegNumber(validRegNumber);
                vehicle.setOwnerDisabled(disabled);
                vehicle.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                vehicleRepository.save(vehicle);
                result = vehicle;
            }
        }
        return result;
    }

    @GetMapping("/{regNumber}")
    public Vehicle checkBill(@PathVariable String regNumber, @RequestParam(required = false, defaultValue = "PLN") String currency) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle result = new Vehicle();

        if (validRegNumber != null) {
            Vehicle vehicle = vehicleRepository.findVehicleByRegNumberAndIsPaidFalse(validRegNumber);
            if (vehicle != null) {
                BigDecimal valueToPay = getValueToPay(currency, vehicle);
                vehicle.setBill(valueToPay);
                result = vehicle;
            }
        }
        return result;
    }

    @DeleteMapping("/{regNumber}")
    public Vehicle pay(@PathVariable String regNumber, @RequestParam(required = false, defaultValue = "PLN") String currency) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle result = new Vehicle();
        Vehicle vehicle = vehicleRepository.findVehicleByRegNumberAndIsPaidFalse(validRegNumber);
        if (vehicle != null) {
            vehicle.setPayDate(new Timestamp(System.currentTimeMillis()));
            vehicle.setBill(getValueToPay(currency, vehicle));
            vehicle.setIsPaid(true);
            vehicleRepository.save(vehicle);
            result = vehicle;
        }
        return result;
    }

    @PutMapping("/{date}")
    public Profit checkProfit(@PathVariable String date) {

        Profit profit = new Profit();

        try {
            profit = countProfitForDate(date);
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
        }

        return profit;
    }

    private Profit countProfitForDate(@PathVariable String date) {

        Profit profit = new Profit();
        String regex = "([2][0-9]{3})-([0][1-9]|[1][0-2])-([0-2][0-9]|[3][0-1])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        if (matcher.matches()) {

            String[] parsedDate = date.split("-");
            LocalDate localDate = LocalDate.of(Integer.valueOf(parsedDate[0]), Integer.valueOf(parsedDate[1]), Integer.valueOf(parsedDate[2]));

            LocalDateTime startLDT = LocalDateTime.of(localDate, LocalTime.of(0, 0, 00));
            Timestamp start = Timestamp.valueOf(startLDT);

            LocalDateTime endLDT = LocalDateTime.of(localDate, LocalTime.of(23, 59, 59));
            Timestamp end = Timestamp.valueOf(endLDT);

            List<Vehicle> vehicleByPayDate = vehicleRepository.findVehiclesByPayDateBetween(start, end);
            profit.setDate(localDate);
            if (!vehicleByPayDate.isEmpty() || vehicleByPayDate != null) {
                BigDecimal dailyProfit = BigDecimal.valueOf(0);
                for (Vehicle vehicle : vehicleByPayDate) {
                    dailyProfit = dailyProfit.add(vehicle.getBill());
                }
                profit.setValue(dailyProfit);
            } else {
                profit.setValue(BigDecimal.valueOf(0));
            }
        }
        return profit;
    }

    private String checkRegNumber(String regNumber) {

        regNumber = regNumber.replaceAll(" ", "").toUpperCase();
        String regex = "[A-Z0-9]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(regNumber);

        if (matcher.matches()) {
            return regNumber;
        } else {
            return null;
        }
    }

    private long checkHours(Timestamp createdDate) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime startTime = createdDate.toLocalDateTime();
        LocalDateTime actualTime = timestamp.toLocalDateTime();
        return ChronoUnit.HOURS.between(startTime, actualTime) + 1;
    }

    private BigDecimal getValueToPay(@RequestParam(required = false, defaultValue = "PLN") String currency, Vehicle vehicle) {
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
            valueToPay = rateRepository.findRateByHoursAndTypeAndCurrency(hours, type, currency).getSumToThisHour();
        }
        return valueToPay;
    }
}
