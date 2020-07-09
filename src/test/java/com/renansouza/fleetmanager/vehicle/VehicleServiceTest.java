package com.renansouza.fleetmanager.vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class VehicleServiceTest {

    @Autowired
    private VehicleService service;

    @MockBean
    private VehicleRepository repository;
    Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setName("Ford KA");
        vehicle.setModel("KA");
        vehicle.setBrand("Ford");
        vehicle.setActive(true);
        vehicle.setFabricationYear(2020);
        vehicle.setCityConsumption(13.3);
        vehicle.setRoadConsumption(15.6);
    }

    @Test
    @DisplayName("Check if context loads")
    void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("Find all vehicles")
    void all() {
        when(repository.findAll()).thenReturn(Collections.singletonList(vehicle));

        assertThat(service.all()).isNotEmpty();
    }

    @Test
    @DisplayName("Find one vehicle")
    void one() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(vehicle));

        final Optional<Vehicle> expected = service.one(1);

        assertThat(expected).isPresent();
        assertThat(expected.get().getName()).isEqualTo(vehicle.getName());
    }

    @Test
    @DisplayName("Find one vehicles by its name ignoring case")
    void oneByName() {
        when(repository.findVehicleByNameIgnoreCase(anyString())).thenReturn(Optional.ofNullable(vehicle));

        final Optional<Vehicle> expected = service.oneByName(vehicle.getName());

        assertThat(expected).isPresent();
        assertThat(expected.get().getName()).isEqualTo(vehicle.getName());
    }

    @Test
    @DisplayName("Add one vehicle")
    void add() {
        when(repository.save(any(Vehicle.class))).thenReturn(vehicle);

        final Vehicle actual = service.add(vehicle);
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(vehicle);
    }

    @Test
    @DisplayName("Update one vehicle")
    void update() {
        when(repository.existsVehicleById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(vehicle));
        when(repository.save(any(Vehicle.class))).thenReturn(vehicle);

        vehicle.setName("Ford KA+");
        vehicle.setBrand("Fordy");
        vehicle.setModel("KA+");
        vehicle.setFabricationYear(2021);
        vehicle.setCityConsumption(14);
        vehicle.setRoadConsumption(17);
        final Vehicle actual = service.update(1, vehicle);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(vehicle);
    }

    @Test
    @DisplayName("Inactivate one vehicle")
    void delete() {
        vehicle.setActive(false);

        when(repository.existsVehicleById(anyInt())).thenReturn(true);
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(vehicle));
        when(repository.save(any(Vehicle.class))).thenReturn(vehicle);

        service.delete(1);
        assertThat(service.one(1).get()).isEqualTo(vehicle);
    }

    @Test
    @DisplayName("Fail to update Vehicle with unknown id")
    void failToUpdateWithUnknownId() {
        final VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> {
            when(repository.existsVehicleById(anyInt())).thenReturn(false);

            service.update(1, vehicle);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Could not find vehicle ");
    }

    @Test
    @DisplayName("Fail to save duplicate Vehicle")
    void failToSaveWithDuplicateRecord() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            when(repository.existsVehicleByNameIgnoreCase(any())).thenReturn(true);

            service.add(vehicle);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Vehicle already added.");
    }

    @Test
    @DisplayName("Fail to save null Vehicle")
    void failToSaveWithNull() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.add(null);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot insert a vehicle with null data.");
    }

    @Test
    @DisplayName("Fail to find with wrong id")
    void failToFindWithWrongId() {
        final VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> {
            service.one(0);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot search for a vehicle with id");
    }

    @Test
    @DisplayName("Fail to find with null name")
    void failToFindWithNullName() {
        final VehicleNotFoundException exception = assertThrows(VehicleNotFoundException.class, () -> {
            service.oneByName(null);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot search for a vehicle with name");
    }

}