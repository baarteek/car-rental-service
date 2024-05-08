package com.example.car.rental.repository;

import com.example.car.rental.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
