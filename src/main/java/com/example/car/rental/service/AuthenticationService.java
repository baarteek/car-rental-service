package com.example.car.rental.service;

import com.example.car.rental.model.Role;
import com.example.car.rental.model.User;
import com.example.car.rental.repository.UserRepository;
import com.example.car.rental.security.AuthenticationRequest;
import com.example.car.rental.security.AuthenticationResponse;
import com.example.car.rental.security.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        repository.findByEmail(request.getEmail())
                .ifPresent(s -> {
                    throw new DataIntegrityViolationException("User with email " + request.getEmail() + " already exists");
                });

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user, user.getUserID());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getUserID())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        var jwtToken = jwtService.generateToken(user, user.getUserID());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getUserID())
                .build();
    }

    private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public AuthenticationResponse oauth2Authenticate(String email) {
        var user = repository.findByEmail(email)
                .orElseGet(() -> {
                    var randomPassword = generateRandomPassword();
                    var newUser = User.builder()
                            .email(email)
                            .password(passwordEncoder.encode(randomPassword))
                            .role(Role.USER)
                            .build();
                    repository.save(newUser);
                    return newUser;
                });

        var jwtToken = jwtService.generateToken(user, user.getUserID());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getUserID())
                .build();
    }
}
