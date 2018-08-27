package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Repository;
import pl.task.parkingmeter.entity.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleService {

    List<Vehicle> findVehiclesByIsPaidFalse();

    Optional<Vehicle> findVehicleByRegNumberAndIsPaidFalse(String regNumber);

    List<Vehicle> findVehiclesByPayDateBetween(LocalDateTime start, LocalDateTime end);

    Vehicle addVehicle(Vehicle vehicle);


}
