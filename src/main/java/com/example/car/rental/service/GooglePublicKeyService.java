package com.example.car.rental.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class GooglePublicKeyService {
    private static final String GOOGLE_PUBLIC_KEYS_URL = "https://www.googleapis.com/oauth2/v3/certs";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GooglePublicKeyService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<PublicKey> getGooglePublicKeys() {
        try {
            String response = restTemplate.getForObject(GOOGLE_PUBLIC_KEYS_URL, String.class);
            JsonNode keys = objectMapper.readTree(response).get("keys");
            List<PublicKey> publicKeys = new ArrayList<>();
            for (JsonNode key : keys) {
                String n = key.get("n").asText();
                String e = key.get("e").asText();

                byte[] nBytes = Base64.getUrlDecoder().decode(n);
                byte[] eBytes = Base64.getUrlDecoder().decode(e);

                BigInteger modulus = new BigInteger(1, nBytes);
                BigInteger exponent = new BigInteger(1, eBytes);

                RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
                publicKeys.add(publicKey);
            }
            return publicKeys;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching Google public keys", e);
        }
    }
}
