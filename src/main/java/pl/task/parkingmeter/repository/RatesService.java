package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Repository;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatesService {

    Rates findRatesByTypeAndCurrency(String type, String currency);


}
