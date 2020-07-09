package com.renansouza.fleetmanager.vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Vehicle extends AbstractPersistable<Integer> {

    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String model;
    private int fabricationYear;
    @Column(nullable = false)
    private double cityConsumption;
    @Column(nullable = false)
    private double roadConsumption;
    private boolean active;

}