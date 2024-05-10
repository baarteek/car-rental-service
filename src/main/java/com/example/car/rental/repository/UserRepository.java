package com.example.car.rental.repository;

import com.example.car.rental.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    public Optional<User> findByEmail(String email);
}
