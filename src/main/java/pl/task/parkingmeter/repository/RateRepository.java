package pl.task.parkingmeter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.task.parkingmeter.entity.Rate;

public interface RateRepository extends JpaRepository<Rate, Long> {


    Rate findRateByHoursAndTypeAndCurrency(long hours, String type, String currency);



}
