package com.renansouza.fleetmanager.fleet;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = {"/fleet"})
public class FleetController {

    private final FleetService service;

    @PostMapping("/{gasPrice}/{cityDistance}/{roadDistance}")
    @ApiOperation(value = "Process cost analysis based on given parameters and known fleet")
    CollectionModel<VehicleForecast> process(@PathVariable double gasPrice, @PathVariable long cityDistance, @PathVariable long roadDistance) {
        return CollectionModel.of(service.process(BigDecimal.valueOf(gasPrice), cityDistance, roadDistance));
    }
}