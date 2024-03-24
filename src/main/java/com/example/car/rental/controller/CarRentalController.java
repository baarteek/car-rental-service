package com.example.car.rental.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarRentalController {
    @GetMapping("/")
    public String hello() {
        return "hello world";
    }
}
