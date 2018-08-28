package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Service;
import pl.task.parkingmeter.entity.Rates;

@Service
public class RatesServiceImpl implements RatesService {

    private RatesRepository repository;

    public RatesServiceImpl(RatesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Rates findRatesByTypeAndCurrency(String type, String currency) {
        return repository.findByTypeAndCurrency(type, currency);
    }
}
