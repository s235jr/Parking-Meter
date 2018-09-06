package pl.task.parkingmeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.task.parkingmeter.entity.Rates;
import pl.task.parkingmeter.entity.ValueRate;

@Repository
public interface ValueRateRepository extends JpaRepository<ValueRate, Long> {

    ValueRate findValueRateByHoursAndRates(long hours, Rates rates);

}
