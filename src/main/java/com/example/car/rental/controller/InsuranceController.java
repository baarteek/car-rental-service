package com.example.car.rental.controller;

import com.example.car.rental.model.Insurance;
import com.example.car.rental.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/insurance")
public class InsuranceController {
    @Autowired
    private InsuranceService insuranceService;

    @GetMapping("/")
    public ResponseEntity<List<Insurance>> getAllInsurances() {
        List<Insurance> insurances = insuranceService.findAllInsurances();
        return ResponseEntity.ok(insurances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getInsuranceById(@PathVariable Integer id) {
        Insurance insurance = insuranceService.findInsuranceById(id);
        return ResponseEntity.ok(insurance);
    }
}
