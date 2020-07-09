package com.renansouza.fleetmanager.vehicle;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class VehicleService {

    private final VehicleRepository repository;

    List<Vehicle> all() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    Optional<Vehicle> one(int id) {
        if (id <= 0) {
            throw new VehicleNotFoundException("Cannot search for a vehicle with id '" + id + "'.");
        }

        return repository.findById(id);
    }

    public Optional<Vehicle> oneByName(String name) {
        if (Objects.isNull(name)) {
            throw new VehicleNotFoundException("Cannot search for a vehicle with name 'null'.");
        }

        return repository.findVehicleByNameIgnoreCase(name);
    }

    Vehicle add(final Vehicle vehicle) {
        if (vehicle == null || StringUtils.isEmpty(vehicle.getName())) {
            throw new IllegalArgumentException("Cannot insert a vehicle with null data.");
        }

        if (repository.existsVehicleByNameIgnoreCase(vehicle.getName())) {
            throw new IllegalArgumentException("Vehicle already added.");
        }

        return repository.save(vehicle);
    }

    Vehicle update(final int id, final Vehicle vehicle) {
        final Vehicle vehicleToUpdate = get(id).get();
        if (!vehicle.getName().equalsIgnoreCase(vehicleToUpdate.getName())) {
            vehicleToUpdate.setName(vehicle.getName());
        }
        if (!vehicle.getBrand().equalsIgnoreCase(vehicleToUpdate.getBrand())) {
            vehicleToUpdate.setBrand(vehicle.getBrand());
        }
        if (!vehicle.getModel().equalsIgnoreCase(vehicleToUpdate.getModel())) {
            vehicleToUpdate.setModel(vehicle.getModel());
        }
        if (vehicle.getFabricationYear() != vehicleToUpdate.getFabricationYear()) {
            vehicleToUpdate.setFabricationYear(vehicle.getFabricationYear());
        }
        if (vehicle.getCityConsumption() != vehicleToUpdate.getCityConsumption()) {
            vehicleToUpdate.setCityConsumption(vehicle.getCityConsumption());
        }
        if (vehicle.getRoadConsumption() != vehicleToUpdate.getRoadConsumption()) {
            vehicleToUpdate.setRoadConsumption(vehicle.getRoadConsumption());
        }

        return repository.save(vehicleToUpdate);
    }

    void delete(int id) {
        final Vehicle vehicle = get(id).get();
        vehicle.setActive(false);

        repository.save(vehicle);
    }

    private Optional<Vehicle> get(int id) {
        if (!repository.existsVehicleById(id)) {
            throw new VehicleNotFoundException(id);
        }

        final Optional<Vehicle> optionalVehicle = repository.findById(id);
        if (optionalVehicle.isEmpty()) {
            throw new VehicleNotFoundException(id);
        }

        return optionalVehicle;
    }

}