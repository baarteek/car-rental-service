package com.example.car.rental.controller;

import com.example.car.rental.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllUsers() {
        ResponseEntity<Customer[]> response = restTemplate.getForEntity("/users", Customer[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length >= 0);
    }

    @Test
    public void testCreateAndDeleteUser() {
        User newUser = new User();
        newUser.setEmail("user12@email.com");
        newUser.setPassword("password");
        newUser.setRole("user");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(newUser, headers);


        ResponseEntity<User> createResponse = restTemplate.postForEntity("/users", request, User.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());


        Integer createdUserId = createResponse.getBody().getUserID();
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/users/" + createdUserId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }

    @Test
    public void testGetUserById() {
        ResponseEntity<User> response = restTemplate.getForEntity("/users/1", User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateUser() {
        ResponseEntity<User> user = restTemplate.getForEntity("/users/1", User.class);
        ResponseEntity<User> response = restTemplate.exchange("/users/1", HttpMethod.PUT, new HttpEntity<>(user.getBody()), User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
