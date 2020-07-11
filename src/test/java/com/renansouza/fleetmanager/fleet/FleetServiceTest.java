package com.renansouza.fleetmanager.fleet;

import com.renansouza.fleetmanager.vehicle.Vehicle;
import com.renansouza.fleetmanager.vehicle.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class FleetServiceTest {

    @Autowired
    private FleetService service;
    @MockBean
    private VehicleService vehicleService;
    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private Vehicle vehicle3;
    private List<Vehicle> vehicles;

    @BeforeEach
    void setUp() {
        vehicle1 = new Vehicle();
        vehicle1.setName("Ford KA");
        vehicle1.setBrand("Ford");
        vehicle1.setModel("KA");
        vehicle1.setActive(true);
        vehicle1.setFabricationYear(2020);
        vehicle1.setCityConsumption(13.3);
        vehicle1.setRoadConsumption(15.6);

        vehicle2 = new Vehicle();
        vehicle2.setName("Chevrolet Onix");
        vehicle2.setBrand("Chevrolet");
        vehicle2.setModel("Onix");
        vehicle2.setActive(true);
        vehicle2.setFabricationYear(2020);
        vehicle2.setCityConsumption(13.9);
        vehicle2.setRoadConsumption(16.7);

        vehicle3 = new Vehicle();
        vehicle3.setName("Volkswagen Gol");
        vehicle3.setBrand("Volkswagen");
        vehicle3.setModel("Gol");
        vehicle3.setActive(true);
        vehicle3.setFabricationYear(2020);
        vehicle3.setCityConsumption(12);
        vehicle3.setRoadConsumption(14.5);

        vehicles = new ArrayList<>();
        vehicles.add(vehicle1);
        vehicles.add(vehicle2);
        vehicles.add(vehicle3);
    }

    @Test
    @DisplayName("Check if context loads")
    void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(vehicleService).isNotNull();
    }

    @Test
    @DisplayName("Test fleet forecast")
    void process() {
        when(vehicleService.all()).thenReturn(vehicles);

        final List<VehicleForecast> vehicleForecasts = service.process(BigDecimal.valueOf(3.99), 100, 100);
        assertThat(vehicleForecasts).hasSize(3);
        assertThat(vehicleForecasts.get(0).getVehicle().getName()).isEqualTo(vehicle2.getName());
        assertThat(vehicleForecasts.get(1).getVehicle().getName()).isEqualTo(vehicle1.getName());
        assertThat(vehicleForecasts.get(2).getVehicle().getName()).isEqualTo(vehicle3.getName());
    }

    @Test
    @DisplayName("Fail to test fleet forecast with gas price less or equal to zero")
    void failToProcessWithWrongGasPrice() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.process(BigDecimal.ZERO, 0, 0);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot process forecast without valid gas price");
    }

    @Test
    @DisplayName("Fail to test fleet forecast with gas price less or equal to zero")
    void failToProcessWithWringDistance() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.process(BigDecimal.ONE, 0, 0);
        });

        assertThat(exception.getMessage()).containsIgnoringCase("Cannot process forecast without valid distance");
    }

}