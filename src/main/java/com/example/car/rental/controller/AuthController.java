package com.example.car.rental.controller;

import com.example.car.rental.model.Role;
import com.example.car.rental.model.User;
import com.example.car.rental.repository.UserRepository;
import com.example.car.rental.security.AuthenticationRequest;
import com.example.car.rental.security.AuthenticationResponse;
import com.example.car.rental.security.RegisterRequest;
import com.example.car.rental.service.AuthenticationService;
import com.example.car.rental.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
