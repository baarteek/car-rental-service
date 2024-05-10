package com.example.car.rental.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table("reviews")
public class Review {
    @Id
    private Integer reviewID;
    private Integer rentalID;
    private Integer rating;
    private String comment;

    public Review() {}

}
