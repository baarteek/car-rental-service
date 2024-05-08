package com.example.car.rental.repository;

import com.example.car.rental.model.Insurance;
import org.springframework.data.repository.CrudRepository;

public interface InsuranceRepository  extends CrudRepository<Insurance, Integer> {
}
