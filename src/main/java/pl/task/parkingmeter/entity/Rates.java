package pl.task.parkingmeter.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rates {

    private static List<Rates> ratesList = new ArrayList<>();
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

    public static void addToRatesList(Rates rates) {
        ratesList.add(rates);
    }

    public static List<Rates> getRatesList() {
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
