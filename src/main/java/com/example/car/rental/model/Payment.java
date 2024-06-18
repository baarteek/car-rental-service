package com.example.car.rental.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Setter
@Getter
@Table("payments")
public class Payment {
    @Id
    private Integer paymentID;
    private Integer rentalID;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;
    private Boolean paymentStatus;

    public Payment() {}

}
