package com.renansouza.fleetmanager.vehicle;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VehicleModelAssembler implements RepresentationModelAssembler<Vehicle, EntityModel<Vehicle>> {

    @Override
    public EntityModel<Vehicle> toModel(Vehicle vehicle) {
        return EntityModel.of(vehicle,
                linkTo(methodOn(VehicleController.class).one(vehicle.getId())).withSelfRel(),
                linkTo(methodOn(VehicleController.class).all()).withRel("vehicles"));
    }

}