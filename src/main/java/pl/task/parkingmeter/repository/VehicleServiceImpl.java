package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Service;
import pl.task.parkingmeter.entity.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    private VehicleRepository repository;

    public VehicleServiceImpl(VehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Vehicle> findVehiclesByIsPaidFalse() {
        return repository.findVehiclesByIsPaidFalse();
    }

    @Override
    public Optional<Vehicle> findVehicleByRegNumberAndIsPaidFalse(String regNumber) {
        return repository.findVehicleByRegNumberAndIsPaidFalse(regNumber);
    }

    @Override
    public List<Vehicle> findVehiclesByPayDateBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findVehiclesByPayDateBetween(start, end);
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        return repository.save(vehicle);
    }
}
