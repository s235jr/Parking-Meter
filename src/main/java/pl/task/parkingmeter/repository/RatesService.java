package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Service;
import pl.task.parkingmeter.entity.Rates;

@Service
public interface RatesService {

    Rates findRatesByTypeAndCurrency(String type, String currency);


}
