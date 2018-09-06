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
    public List<Vehicle> findVehiclesByPaidFalse() {
        return repository.findVehiclesByPaidFalse();
    }

    @Override
    public Optional<Vehicle> findVehicleByRegNumberAndPaidFalse(String regNumber) {
        return repository.findVehicleByRegNumberAndPaidFalse(regNumber);
    }

    @Override
    public List<Vehicle> findVehiclesByPayDateBetweenAndCurrency(LocalDateTime start, LocalDateTime end, String currency) {
        return repository.findVehiclesByPayDateBetweenAndCurrency(start, end, currency);
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    @Override
    public List<String> getAllCurrency() {
        return repository.getAllCurrency();
    }


}
