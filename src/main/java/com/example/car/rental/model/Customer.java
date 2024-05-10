package com.example.car.rental.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Setter
@Getter
@Table("customers")
public class Customer {
    @Id
    private Integer customerID;
    private Integer userID;
    private String firstName;
    private String lastName;
    private String phoneNumber;

}
