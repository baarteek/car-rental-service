package com.example.car.rental.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;


@Setter
@Getter
@Table("rentals")
public class Rental {
    @Id
    private Integer rentalID;
    private Integer customerID;
    private Integer vehicleID;
    private Integer insuranceID;
    private Date startDate;
    private Date endDate;
    private String status;
    private Date createdAt;
    private String notes;

    public Rental() {}

}
