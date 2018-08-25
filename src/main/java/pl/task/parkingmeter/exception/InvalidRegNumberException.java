package pl.task.parkingmeter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidRegNumberException extends RuntimeException {

    public InvalidRegNumberException(String regNumber) {
        super("INVALID REGNUMBER : '" + regNumber + "'.");
    }

}
