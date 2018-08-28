package pl.task.parkingmeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.task.parkingmeter.entity.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findVehiclesByPaidFalse();

    Optional<Vehicle> findVehicleByRegNumberAndPaidFalse(String regNumber);

    List<Vehicle> findVehiclesByPayDateBetween(LocalDateTime start, LocalDateTime end);

}
