package pl.task.parkingmeter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.task.parkingmeter.entity.Vehicle;
import pl.task.parkingmeter.repository.RateRepository;
import pl.task.parkingmeter.repository.VehicleRepository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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

//    @GetMapping("/")
//    public String home() {
//        return "index";
//    }

    @GetMapping("/startPark")
    public String startPark(@RequestParam String regNumber, @RequestParam(required = false) boolean disabled) {

        String validRegNumber = checkRegNumber(regNumber);

        if (validRegNumber != null) {
            Vehicle vehicleFromDB = vehicleRepository.findVehicleByRegNumberAndIsPaidFalse(validRegNumber);
            if (vehicleFromDB == null) {
                Vehicle vehicle = new Vehicle();
                vehicle.setRegNumber(validRegNumber);
                vehicle.setOwnerDisabled(disabled);
                vehicle.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                vehicleRepository.save(vehicle);
                System.out.println("Add vehicle!");
            } else {
                System.out.println("Vehicle was added earlier!");
            }
        } else {
            System.out.println("Invalid regnumber!");
        }
        return "redirect:/";
    }

    @GetMapping("/{regNumber}")
    @ResponseBody
    public Vehicle checkVehicle(@PathVariable String regNumber) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle vehicle = new Vehicle();

        if (validRegNumber != null) {
            vehicle = vehicleRepository.findVehicleByRegNumberAndIsPaidFalse(validRegNumber);
        }
        return vehicle;
    }

    @GetMapping("/checkValue")
    @ResponseBody
    public String checkValue(@RequestParam String regNumber, @RequestParam(required = false, defaultValue = "PLN") String currency) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle vehicle = vehicleRepository.findVehicleByRegNumberAndIsPaidFalse(validRegNumber);
        BigDecimal valueToPay = getValueToPay(currency, vehicle);

        return valueToPay.toString();
    }


    @GetMapping("/pay")
    @ResponseBody
    public String pay(@RequestParam String regNumber, @RequestParam(required = false, defaultValue = "PLN") String currency) {

        String validRegNumber = checkRegNumber(regNumber);
        Vehicle vehicle = vehicleRepository.findVehicleByRegNumberAndIsPaidFalse(validRegNumber);
        if (vehicle != null) {
            vehicle.setPayDate(new Timestamp(System.currentTimeMillis()));
            vehicle.setBill(getValueToPay(currency, vehicle));
            vehicle.setIsPaid(true);
            vehicleRepository.save(vehicle);
        } else {
            System.out.println("PAID EARLIER");
        }
        return "Paid";
    }

    @GetMapping("/checkProfit")
    @ResponseBody
    public String checkProfit(@RequestParam Date date) {

        LocalDateTime startLDT = LocalDateTime.of(date.toLocalDate(), LocalTime.of(0, 0, 00));
        LocalDateTime endLDT = LocalDateTime.of(date.toLocalDate(), LocalTime.of(23, 59, 59));

        Timestamp start = Timestamp.valueOf(startLDT);
        Timestamp end = Timestamp.valueOf(endLDT);

        List<Vehicle> vehicleByPayDate = vehicleRepository.findVehiclesByPayDateBetween(start, end);
        BigDecimal dailyProfit = BigDecimal.valueOf(0);

        for (Vehicle vehicle : vehicleByPayDate) {
            System.out.println(vehicle.getBill());
            dailyProfit = dailyProfit.add(vehicle.getBill());
        }
        return "Today you earn: " + dailyProfit.toString();
    }

    @GetMapping("/vehicles")
    public List<Vehicle> showVehicles() {
        return vehicleRepository.findVehiclesByIsPaidFalse();
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
