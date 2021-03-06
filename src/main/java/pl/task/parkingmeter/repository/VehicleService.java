package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Service;
import pl.task.parkingmeter.entity.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface VehicleService {

    List<Vehicle> findVehiclesByPaidFalse();

    Optional<Vehicle> findVehicleByRegNumberAndPaidFalse(String regNumber);

    List<Vehicle> findVehiclesByPayDateBetweenAndCurrency(LocalDateTime start, LocalDateTime end, String currency);

    Vehicle addVehicle(Vehicle vehicle);

    List<String> getAllCurrency();


}
