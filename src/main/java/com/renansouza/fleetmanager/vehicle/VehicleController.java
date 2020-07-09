package com.renansouza.fleetmanager.vehicle;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping(value = {"/vehicles"})
public class VehicleController {

    private final VehicleService service;
    private final VehicleModelAssembler assembler;

    @GetMapping
    @ApiOperation(value = "all vehicles")
    CollectionModel<EntityModel<Vehicle>> all() {
        List<EntityModel<Vehicle>> vehicles = service.all().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(vehicles, linkTo(methodOn(VehicleController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "one vehicle")
    EntityModel<Vehicle> one(@PathVariable int id) {
        Vehicle vehicle = service.one(id).orElseThrow(() -> new VehicleNotFoundException(id));

        return assembler.toModel(vehicle);
    }

    @PostMapping()
    @ApiOperation(value = "add a vehicle")
    ResponseEntity<?> add(@RequestBody Vehicle vehicle) {
        EntityModel<Vehicle> entityModel = assembler.toModel(service.add(vehicle));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "update a vehicle")
    ResponseEntity<EntityModel<Vehicle>> update(@PathVariable("id") int id, @RequestBody Vehicle vehicle) {
        EntityModel<Vehicle> entityModel = assembler.toModel(service.update(id, vehicle));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "inactive a vehicle")
    ResponseEntity<EntityModel<Vehicle>> delete(@PathVariable("id") int id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

}