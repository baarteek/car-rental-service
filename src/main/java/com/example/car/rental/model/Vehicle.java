package com.example.car.rental.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Setter
@Getter
@Table("vehicles")
public class Vehicle {
    @Id
    private Integer vehicleID;
    private Integer insuranceID;
    private String brand;
    private String model;
    private Integer yearOfManufacture;
    private String registrationNumber;
    private String status;
    private double pricePerDay;
    private Integer mileage;
    private Date lastServiceDate;
    private String imageUrl;

    public Vehicle() {}


}
