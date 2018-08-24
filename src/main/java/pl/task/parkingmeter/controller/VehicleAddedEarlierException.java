package pl.task.parkingmeter.controller;

public class VehicleAddedEarlierException extends RuntimeException {

    public VehicleAddedEarlierException(String regNumber) {
        super("VEHICLE WAS ADDED EARLIER : '" + regNumber + "'.");
    }

}
