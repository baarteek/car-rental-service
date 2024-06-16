package com.example.car.rental.service;

import com.example.car.rental.model.Insurance;
import com.example.car.rental.model.Rental;
import com.example.car.rental.model.User;
import com.example.car.rental.model.Vehicle;
import com.example.car.rental.repository.InsuranceRepository;
import com.example.car.rental.repository.RentalRepository;
import com.example.car.rental.repository.UserRepository;
import com.example.car.rental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class RentalService {
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InsuranceRepository insuranceRepository;

    public Rental createRental(Integer userId, Integer vehicleId, Integer insuranceId, Date startDate, Date endDate, String notes) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new RuntimeException("Vehicle not found"));
        Insurance insurance = insuranceRepository.findById(insuranceId).orElseThrow(() -> new RuntimeException("Insurance not found"));

        if(!vehicle.getStatus().equals("available")) {
            throw new RuntimeException("Vehicle not available");
        }

        vehicle.setStatus("rented");
        vehicleRepository.save(vehicle);

        Rental rental = new Rental();
        rental.setUserID(user.getUserID());
        rental.setVehicleID(vehicle.getVehicleID());
        rental.setInsuranceID(insurance.getInsuranceID());
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setStatus("active");
        rental.setNotes(notes);

        return rentalRepository.save(rental);
    }

    public Rental completeRental(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new RuntimeException("Rental not found"));
        rental.setStatus("completed");
        return rentalRepository.save(rental);
    }
}