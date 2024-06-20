package com.example.car.rental.controller;

import com.example.car.rental.model.Rental;
import com.example.car.rental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @PostMapping
    public ResponseEntity<Rental> createRental(@RequestBody Map<String, Object> requestData) {
        try {
            Integer userId = Integer.parseInt((String) requestData.get("userId"));
            Integer vehicleId = Integer.parseInt((String) requestData.get("vehicleId"));
            Integer insuranceId = Integer.parseInt((String) requestData.get("insuranceId"));
            String startDateStr = (String) requestData.get("startDate");
            String endDateStr = (String) requestData.get("endDate");
            String notes = (String) requestData.get("notes");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);

            System.out.println("Rental request received from userId: " + userId);
            System.out.println("vehicleId: " + vehicleId);
            System.out.println("insuranceId: " + insuranceId);
            System.out.println("startDate: " + startDate);
            System.out.println("endDate: " + endDate);
            System.out.println("notes: " + notes);

            Rental rental = rentalService.createRental(userId, vehicleId, insuranceId, startDate, endDate, notes);
            return ResponseEntity.ok(rental);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{rentalId}/complete")
    public ResponseEntity<Rental> completeRental(@PathVariable Integer rentalId) {
        Rental rental = rentalService.completeRental(rentalId);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/vehicle/{vehicleId}/future")
    public ResponseEntity<List<Rental>> getFutureRentalsByVehicleId(@PathVariable Integer vehicleId) {
        List<Rental> rentals = rentalService.getFutureRentalsByVehicleId(vehicleId);
        return ResponseEntity.ok(rentals);
    }
}
