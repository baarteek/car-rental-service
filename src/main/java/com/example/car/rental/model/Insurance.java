package com.example.car.rental.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Setter
@Getter
@Table("insurance")
public class Insurance {
    @Id
    private Integer insuranceID;
    private String name;
    private String description;
    private double pricePerDay;
}
