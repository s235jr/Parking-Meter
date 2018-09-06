package pl.task.parkingmeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.task.parkingmeter.entity.Rates;

@Repository
public interface RatesRepository extends JpaRepository<Rates, Long> {

    Rates findByTypeAndCurrency(String type, String currency);

}
