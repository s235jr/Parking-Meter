package pl.task.parkingmeter.controller;

public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String date) {
        super("INVALID DATE : '" + date + "'.");
    }

}
