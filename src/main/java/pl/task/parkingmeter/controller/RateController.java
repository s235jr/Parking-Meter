package pl.task.parkingmeter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.task.parkingmeter.entity.Rate;
import pl.task.parkingmeter.repository.RateRepository;

import java.math.BigDecimal;

@Controller
public class RateController {

    @Autowired
    RateRepository rateRepository;

    @GetMapping("/createRates")
    @ResponseBody
    public String createRates() {

        //normal PLN
        Rate firstHour = new Rate();
        firstHour.setType("regular");
        firstHour.setCurrency("PLN");
        firstHour.setHours(1);
        firstHour.setRate(BigDecimal.valueOf(1));
        firstHour.setSumToThisHour(BigDecimal.valueOf(1));
        rateRepository.save(firstHour);

        Rate secondHour = new Rate();
        secondHour.setType("regular");
        secondHour.setCurrency("PLN");
        secondHour.setHours(2);
        secondHour.setRate(BigDecimal.valueOf(2));
        secondHour.setSumToThisHour(BigDecimal.valueOf(3));
        rateRepository.save(secondHour);

        Rate previousHours = secondHour;

        for (long i = 3; i <= 24; i++) {
            Rate nextRates = new Rate();
            nextRates.setType(previousHours.getType());
            nextRates.setCurrency(previousHours.getCurrency());
            nextRates.setHours(i);
            nextRates.setRate(previousHours.getRate().multiply(BigDecimal.valueOf(1.5)));
            nextRates.setSumToThisHour(previousHours.getSumToThisHour().add(nextRates.getRate()));
            rateRepository.save(nextRates);
            previousHours = nextRates;
        }

        //disabled PLN
        Rate firstDisHour = new Rate();
        firstDisHour.setType("disabled");
        firstDisHour.setCurrency("PLN");
        firstDisHour.setHours(1);
        firstDisHour.setRate(BigDecimal.valueOf(0));
        firstDisHour.setSumToThisHour(BigDecimal.valueOf(0));
        rateRepository.save(firstDisHour);

        Rate secondDisHour = new Rate();
        secondDisHour.setType("disabled");
        secondDisHour.setCurrency("PLN");
        secondDisHour.setHours(2);
        secondDisHour.setRate(BigDecimal.valueOf(2));
        secondDisHour.setSumToThisHour(BigDecimal.valueOf(2));
        rateRepository.save(secondDisHour);

        Rate previousDisHours = secondDisHour;

        for (long i = 3; i <= 24; i++) {
            Rate nextRates = new Rate();
            nextRates.setType(previousDisHours.getType());
            nextRates.setCurrency(previousDisHours.getCurrency());
            nextRates.setHours(i);
            nextRates.setRate(previousDisHours.getRate().multiply(BigDecimal.valueOf(1.2)));
            nextRates.setSumToThisHour(previousDisHours.getSumToThisHour().add(nextRates.getRate()));
            rateRepository.save(nextRates);
            previousDisHours = nextRates;
        }

        return "CREATED";
    }


}

