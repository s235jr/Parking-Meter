package pl.task.parkingmeter.controller;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(String regNumber) {
        super("COULD NOT FIND VEHICLE WITH REGISTRATION NUMBER : '" + regNumber + "'.");
    }

}
