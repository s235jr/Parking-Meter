package pl.task.parkingmeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.task.parkingmeter.entity.Rate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        createRates();



    }

    public static void createRates() {

        Rate regPLN = new Rate();
        regPLN.setType("regular");
        regPLN.setCurrency("PLN");
        Map<Integer, BigDecimal> valueOfEachHours = new HashMap<>();
        valueOfEachHours.put(1, BigDecimal.valueOf(1));
        valueOfEachHours.put(2, BigDecimal.valueOf(2));
        for (int i = 3; i <= 24; i++) {
            valueOfEachHours.put(i, valueOfEachHours.get(i - 1).multiply(BigDecimal.valueOf(1.5)));
        }

        Map<Integer, BigDecimal> regularPLN = new HashMap<>();
        regularPLN.put(1, BigDecimal.valueOf(1));
        for (int i = 2; i <= 24; i++) {
            regularPLN.put(i, regularPLN.get(i - 1).add(valueOfEachHours.get(i)).setScale(2, RoundingMode.CEILING));
        }
        regPLN.setRates(regularPLN);
        Rate.addToRatesList(regPLN);

        Rate disPLN = new Rate();
        disPLN.setType("disabled");
        disPLN.setCurrency("PLN");
        valueOfEachHours = new HashMap<>();
        valueOfEachHours.put(1, BigDecimal.valueOf(0));
        valueOfEachHours.put(2, BigDecimal.valueOf(2));
        for (int i = 3; i <= 24; i++) {
            valueOfEachHours.put(i, valueOfEachHours.get(i - 1).multiply(BigDecimal.valueOf(1.2)));
        }

        Map<Integer, BigDecimal> disabledPLN = new HashMap<>();
        disabledPLN.put(1, BigDecimal.valueOf(0));
        disabledPLN.put(2, BigDecimal.valueOf(2));
        for (int i = 3; i <= 24; i++) {
            disabledPLN.put(i, disabledPLN.get(i - 1).add(valueOfEachHours.get(i)).setScale(2, RoundingMode.CEILING));
        }
        disPLN.setRates(disabledPLN);
        Rate.addToRatesList(disPLN);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOrigins("http://localhost");
            }
        };
    }
}
