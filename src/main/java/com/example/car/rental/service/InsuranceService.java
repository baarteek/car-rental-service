package com.example.car.rental.service;

import com.example.car.rental.model.Insurance;
import com.example.car.rental.repository.InsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class InsuranceService {
    @Autowired
    private InsuranceRepository insuranceRepository;

    public List<Insurance> findAllInsurances() {
        return StreamSupport.stream(insuranceRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Insurance findInsuranceById(Integer id) {
        return insuranceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance not found with id " + id));
    }
}
