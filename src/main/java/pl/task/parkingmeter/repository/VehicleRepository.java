package pl.task.parkingmeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.task.parkingmeter.entity.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findVehiclesByPaidFalse();

    Optional<Vehicle> findVehicleByRegNumberAndPaidFalse(String regNumber);

    List<Vehicle> findVehiclesByPayDateBetweenAndCurrency(LocalDateTime start, LocalDateTime end, String currency);

    @Query("SELECT DISTINCT currency FROM Vehicle where currency IS NOT NULL")
    List<String> getAllCurrency();



}
