package pl.task.parkingmeter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid date format!")
public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String date) {
        super("INVALID DATE : '" + date + "'.");
    }

}
