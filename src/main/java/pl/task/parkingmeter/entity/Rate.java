package pl.task.parkingmeter.entity;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Rate {

    private static List<Rate> ratesList = new ArrayList<>();
    private String currency;
    private String type;
    private Map<Integer, BigDecimal> rates;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<Integer, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<Integer, BigDecimal> rates) {
        this.rates = rates;
    }

    public static void addToRatesList(Rate rates) {
        ratesList.add(rates);
    }

    public static List<Rate> getRatesList() {
        return ratesList;
    }

    @Override
    public String toString() {
        return "Rates{" +
                "currency='" + currency + '\'' +
                ", type='" + type + '\'' +
                ", rates=" + rates +
                '}';
    }
}
