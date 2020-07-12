package com.renansouza.fleetmanager.fleet;

import com.renansouza.fleetmanager.vehicle.VehicleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.renansouza.fleetmanager.fleet.FleetUtils.*;
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

    @Test
    @DisplayName("Check if context loads")
    void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(vehicleService).isNotNull();
    }

    @Test
    @DisplayName("Test fleet forecast")
    void process() {
        when(vehicleService.all()).thenReturn(FleetUtils.getVehicles());

        final List<VehicleForecast> vehicleForecasts = service.process(BigDecimal.valueOf(3.99), 100, 100);
        assertThat(vehicleForecasts).hasSize(3);
        assertThat(vehicleForecasts.get(0).getVehicle().getName()).isEqualTo(getVehicle2().getName());
        assertThat(vehicleForecasts.get(1).getVehicle().getName()).isEqualTo(getVehicle1().getName());
        assertThat(vehicleForecasts.get(2).getVehicle().getName()).isEqualTo(getVehicle3().getName());
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