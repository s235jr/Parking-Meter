package pl.task.parkingmeter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid registration number!")
public class InvalidRegNumberException extends RuntimeException {

    public InvalidRegNumberException(String regNumber) {
        super("INVALID REGNUMBER : '" + regNumber + "'.");
    }

}
