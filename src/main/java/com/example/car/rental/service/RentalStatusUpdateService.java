package com.example.car.rental.service;

import com.example.car.rental.model.Rental;
import com.example.car.rental.model.Vehicle;
import com.example.car.rental.repository.RentalRepository;
import com.example.car.rental.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalStatusUpdateService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;

    @Scheduled(cron = "0 * * * * ?") // Co 1 minutę
    @Transactional
    public void updateRentalStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Rental> rentals = (List<Rental>) rentalRepository.findAll();

        for (Rental rental : rentals) {
            LocalDateTime startDate = convertToLocalDateTime(rental.getStartDate());
            LocalDateTime endDate = convertToLocalDateTime(rental.getEndDate());

            if (now.isAfter(endDate) && "active".equals(rental.getStatus())) {
                rental.setStatus("completed");
                updateVehicleStatus(rental.getVehicleID(), "available");
            } else if (now.isBefore(endDate) && now.isAfter(startDate) && !"active".equals(rental.getStatus())) {
                rental.setStatus("active");
                updateVehicleStatus(rental.getVehicleID(), "rented");
            }
        }

        rentalRepository.saveAll(rentals);
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void updateVehicleStatus(Integer vehicleId, String status) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
        vehicle.setStatus(status);
        vehicleRepository.save(vehicle);
    }
}
