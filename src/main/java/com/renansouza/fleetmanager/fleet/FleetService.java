package com.renansouza.fleetmanager.fleet;

import com.renansouza.fleetmanager.vehicle.Vehicle;
import com.renansouza.fleetmanager.vehicle.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FleetService {

    private final VehicleService service;

    // FIXME ignore active by default
    List<VehicleForecast> process(final BigDecimal gasPrice, final long cityDistance, final long roadDistance) {
        if (gasPrice == null || gasPrice.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Cannot process forecast without valid gas price.");
        }

        if (cityDistance == 0 && roadDistance == 0) {
            throw new IllegalArgumentException("Cannot process forecast without valid distance.");
        }

        return service.all().stream()
                .filter(Vehicle::isActive)
                .map(vehicle -> new VehicleForecast(vehicle, gasPrice, cityDistance, roadDistance))
                .sorted(Comparator.comparingDouble(VehicleForecast::getTotal))
                .collect(Collectors.toList());
    }
}