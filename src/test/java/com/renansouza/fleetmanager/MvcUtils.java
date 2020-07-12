package com.renansouza.fleetmanager;

import com.renansouza.fleetmanager.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class MvcUtils {

    public static Vehicle getVehicle1() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setName("Ford KA");
        vehicle1.setBrand("Ford");
        vehicle1.setModel("KA");
        vehicle1.setActive(true);
        vehicle1.setFabricationYear(2020);
        vehicle1.setCityConsumption(13.3);
        vehicle1.setRoadConsumption(15.6);

        return vehicle1;
    }

    public static Vehicle getVehicle2() {
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setName("Chevrolet Onix");
        vehicle2.setBrand("Chevrolet");
        vehicle2.setModel("Onix");
        vehicle2.setActive(true);
        vehicle2.setFabricationYear(2020);
        vehicle2.setCityConsumption(13.9);
        vehicle2.setRoadConsumption(16.7);

        return vehicle2;
    }

    public static Vehicle getVehicle3() {
        Vehicle vehicle3 = new Vehicle();
        vehicle3.setName("Volkswagen Gol");
        vehicle3.setBrand("Volkswagen");
        vehicle3.setModel("Gol");
        vehicle3.setActive(true);
        vehicle3.setFabricationYear(2020);
        vehicle3.setCityConsumption(12);
        vehicle3.setRoadConsumption(14.5);

        return vehicle3;
    }

    public static List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(getVehicle1());
        vehicles.add(getVehicle2());
        vehicles.add(getVehicle3());

        return vehicles;
    }

}
