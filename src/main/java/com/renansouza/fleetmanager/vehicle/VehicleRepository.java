package com.renansouza.fleetmanager.vehicle;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "vehicle", path = "vehicle")
public interface VehicleRepository extends PagingAndSortingRepository<Vehicle, Integer> {

    Optional<Vehicle> findVehicleByNameIgnoreCase(String name);

    boolean existsVehicleByNameIgnoreCase(String name);

    boolean existsVehicleById(int id);

}