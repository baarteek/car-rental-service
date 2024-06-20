package com.example.car.rental.service;

import com.example.car.rental.model.Rental;
import com.example.car.rental.model.Vehicle;
import com.example.car.rental.repository.RentalRepository;
import com.example.car.rental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RentalRepository rentalRepository;

    public List<Vehicle> findAllVehicles() {
        return StreamSupport.stream(vehicleRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Vehicle> findAvailableVehicles() {
        return StreamSupport.stream(vehicleRepository.findAll().spliterator(), false)
                .filter(vehicle -> "available".equals(vehicle.getStatus()))
                .collect(Collectors.toList());
    }

    public Vehicle findVehicleById(Integer id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    public List<Vehicle> findAvailableVehicles(Date startDate, Date endDate) {
        List<Rental> conflictingRentals = rentalRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
        List<Vehicle> allVehicles = (List<Vehicle>) vehicleRepository.findAll();

        List<Vehicle> availableVehicles = allVehicles.stream()
                .filter(vehicle -> conflictingRentals.stream()
                        .noneMatch(rental -> rental.getVehicleID().equals(vehicle.getVehicleID())))
                .collect(Collectors.toList());

        return availableVehicles;
    }
}
