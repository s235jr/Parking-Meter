package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Service;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.ValueRate;

@Service
public class ValueRatesServiceImpl implements ValueRateService {

    private ValueRateRepository repository;

    public ValueRatesServiceImpl(ValueRateRepository repository) {
        this.repository = repository;
    }

    @Override
    public ValueRate findValueRateByHoursAndRatesId(long hours, Rates rates) {
        return repository.findValueRateByHoursAndRates(hours, rates);
    }
}
