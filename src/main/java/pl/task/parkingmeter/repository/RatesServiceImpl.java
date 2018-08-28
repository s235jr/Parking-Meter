package pl.task.parkingmeter.repository;

import org.springframework.stereotype.Service;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
