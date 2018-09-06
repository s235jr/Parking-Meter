package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Service;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.ValueRate;

@Service
public interface ValueRateService {

    ValueRate findValueRateByHoursAndRatesId(long hours, Rates rates);


}
