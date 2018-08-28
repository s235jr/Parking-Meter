package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Repository;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.ValueRate;

@Repository
public interface ValueRateService {

    ValueRate findValueRateByHoursAndRatesId(long hours, Rates rates);


}
