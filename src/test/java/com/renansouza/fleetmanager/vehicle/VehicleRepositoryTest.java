package com.renansouza.fleetmanager.vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class VehicleRepositoryTest {

    @Autowired
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
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("Add a new vehicle and find by its name ignoring case")
    void findVehicleByNameIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();

        repository.save(vehicle);
        assertThat(repository.findVehicleByNameIgnoreCase("ford ka").get()).isEqualTo(vehicle);
    }

    @Test
    @DisplayName("Add a new vehicle and check if exists by its name ignoring case")
    void existsVehicleByNameIgnoreCase() {
        assertThat(repository.findAll()).isEmpty();

        repository.save(vehicle);
        assertThat(repository.existsVehicleByNameIgnoreCase("ford ka")).isTrue();
    }

    @Test
    @DisplayName("Add a new vehicle and check if exists by its id")
    void existsVehicleById() {
        assertThat(repository.findAll()).isEmpty();

        final Vehicle vehicle = repository.save(this.vehicle);
        assertThat(repository.existsVehicleById(vehicle.getId())).isTrue();
    }

    @Test
    @DisplayName("Add a new vehicle and find by its id")
    void findVehicleById() {
        assertThat(repository.findAll()).isEmpty();

        vehicle = repository.save(vehicle);

        assertThat(vehicle).isNotNull();
        assertThat(repository.findById(vehicle.getId()).get()).isEqualTo(vehicle);
    }

    @Test
    @DisplayName("Add a new vehicle and update his name")
    void update() {
        assertThat(repository.findAll()).isEmpty();

        vehicle = repository.save(vehicle);

        vehicle.setModel("KA+");
        vehicle.setName("Ford KA+");
        assertThat(vehicle).isNotNull();
        assertThat(repository.findById(vehicle.getId()).get()).isEqualTo(vehicle);
    }

    @Test
    @DisplayName("Fail to save a new vehicle with null name")
    void failToSaveWithoutName() {
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            assertThat(repository.findAll()).isEmpty();

            vehicle.setName(null);
            repository.save(vehicle);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("not-null property references");
    }

    @Test
    @DisplayName("Fail to save a duplicate vehicle")
    void failToSaveDuplicateVehicle() {
        final DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            assertThat(repository.findAll()).isEmpty();

            repository.save(vehicle);

            final Vehicle duplicateVehicle = new Vehicle();
            duplicateVehicle.setName(vehicle.getName());

            repository.save(duplicateVehicle);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("not-null property references");
    }

}