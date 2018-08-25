package pl.task.parkingmeter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleAddedEarlierException extends RuntimeException {

    public VehicleAddedEarlierException(String regNumber) {
        super("VEHICLE WAS ADDED EARLIER : '" + regNumber + "'.");
    }

}
