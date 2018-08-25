package pl.task.parkingmeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.task.parkingmeter.entity.Vehicle;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findVehiclesByIsPaidFalse();

    Optional<Vehicle> findVehicleByRegNumberAndIsPaidFalse(String regNumber);

    List<Vehicle> findVehiclesByPayDateBetween(Timestamp start, Timestamp end);

}
