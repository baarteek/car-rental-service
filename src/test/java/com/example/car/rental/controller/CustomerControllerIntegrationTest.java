package com.example.car.rental.controller;

import com.example.car.rental.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllCustomers() {
        ResponseEntity<Customer[]> response = restTemplate.getForEntity("/customers", Customer[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length >= 0);
    }

    @Test
    public void testCreateAndDeleteCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Borys");
        newCustomer.setLastName("Syrob");
        newCustomer.setPhoneNumber("100100100");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> request = new HttpEntity<>(newCustomer, headers);

        ResponseEntity<Customer> createResponse = restTemplate.postForEntity("/customers", request, Customer.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        Integer createdCustomerId = createResponse.getBody().getCustomerID();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/customers/" + createdCustomerId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }

    @Test
    public void testGetCustomerById() {
        ResponseEntity<Customer> response = restTemplate.getForEntity("/customers/3", Customer.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateCustomer() {
        ResponseEntity<Customer> customer = restTemplate.getForEntity("/customers/3", Customer.class);
        ResponseEntity<Customer> response = restTemplate.exchange("/customers/3", HttpMethod.PUT, new HttpEntity<>(customer.getBody()), Customer.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
