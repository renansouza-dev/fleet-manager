package com.renansouza.fleetmanager.vehicle;

class VehicleNotFoundException extends RuntimeException {

    VehicleNotFoundException(String message) {
        super(message);
    }

    VehicleNotFoundException(int id) {
        super("Could not find vehicle " + id);
    }

}