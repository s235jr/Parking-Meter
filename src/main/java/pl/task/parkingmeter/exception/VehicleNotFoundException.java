package pl.task.parkingmeter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vehicle not found!")
public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(String regNumber) {
        super("COULD NOT FIND VEHICLE WITH REGISTRATION NUMBER : '" + regNumber + "'.");
    }

}
