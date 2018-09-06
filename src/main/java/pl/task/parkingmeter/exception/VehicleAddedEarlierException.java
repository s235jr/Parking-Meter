package pl.task.parkingmeter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vehicle was added earlier!")
public class VehicleAddedEarlierException extends RuntimeException {

    public VehicleAddedEarlierException(String regNumber) {
        super("VEHICLE WAS ADDED EARLIER : '" + regNumber + "'.");
    }

}
