package com.example.car.rental.controller;

import com.example.car.rental.model.Role;
import com.example.car.rental.model.User;
import com.example.car.rental.repository.UserRepository;
import com.example.car.rental.security.AuthenticationRequest;
import com.example.car.rental.security.AuthenticationResponse;
import com.example.car.rental.security.RegisterRequest;
import com.example.car.rental.service.AuthenticationService;
import com.example.car.rental.service.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final String CLIENT_ID = "397639903179-pjtd5jcqghmi0624a3n3ulegor5lv294.apps.googleusercontent.com";

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        if (jwtService.isTokenValid(token)) {
            return ResponseEntity.ok().body("User is authenticated");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponse> googleLogin(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String email = verifyGoogleTokenAndGetEmail(token);
        AuthenticationResponse response = service.oauth2Authenticate(email);
        return ResponseEntity.ok(response);
    }

    private String verifyGoogleTokenAndGetEmail(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                return payload.getEmail();
            } else {
                throw new RuntimeException("Invalid Google token");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error verifying Google token", e);
        }
    }
}
