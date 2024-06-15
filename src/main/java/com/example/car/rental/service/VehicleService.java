package com.example.car.rental.service;

import com.example.car.rental.model.Vehicle;
import com.example.car.rental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> findAvailableVehicles() {
        return StreamSupport.stream(vehicleRepository.findAll().spliterator(), false)
                .filter(vehicle -> "available".equals(vehicle.getStatus()))
                .collect(Collectors.toList());
    }

    public Vehicle findVehicleById(Integer id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }
}
