package com.renansouza.fleetmanager.fleet;

import com.renansouza.fleetmanager.vehicle.Vehicle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class VehicleForecast {

    private Vehicle vehicle;
    private double total;

    public VehicleForecast(final Vehicle vehicle, final BigDecimal gasPrice, final long cityDistance, final long roadDistance) {
        this.vehicle = vehicle;
        final BigDecimal cityMileage = BigDecimal.valueOf(cityDistance / vehicle.getCityConsumption());
        final BigDecimal roadMileage = BigDecimal.valueOf(roadDistance / vehicle.getRoadConsumption());
        total = cityMileage.add(roadMileage).multiply(gasPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}