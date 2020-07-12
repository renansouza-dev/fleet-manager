package com.renansouza.fleetmanager.fleet;

class FleetNotFoundException extends RuntimeException {

    FleetNotFoundException(String message) {
        super(message);
    }

}