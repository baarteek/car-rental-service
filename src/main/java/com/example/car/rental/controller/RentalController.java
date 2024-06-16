package com.example.car.rental.controller;

import com.example.car.rental.model.Rental;
import com.example.car.rental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @PostMapping
    public ResponseEntity<Rental> createRental(@RequestParam Integer userId,
                                               @RequestParam Integer vehicleId,
                                               @RequestParam Integer insuranceId,
                                               @RequestParam Date startDate,
                                               @RequestParam Date endDate,
                                               @RequestParam(required = false) String notes) {
        Rental rental = rentalService.createRenal(userId, vehicleId, insuranceId, startDate, endDate, notes);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{rentalId}/complete")
    public ResponseEntity<Rental> completeRental(@PathVariable Integer rentalId) {
        Rental rental = rentalService.completeRental(rentalId);
        return ResponseEntity.ok(rental);
    }
}
