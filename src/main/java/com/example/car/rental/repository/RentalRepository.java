package com.example.car.rental.repository;

import com.example.car.rental.model.Rental;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface RentalRepository extends CrudRepository<Rental, Integer> {
    List<Rental> findByVehicleIDAndEndDateAfter(Integer vehicleId, Date date);
    List<Rental> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(Date endDate, Date startDate);
}
