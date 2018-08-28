package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Repository;
import pl.task.parkingmeter.entity.Rates;

@Repository
public interface RatesService {

    Rates findRatesByTypeAndCurrency(String type, String currency);


}
