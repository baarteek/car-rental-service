package com.example.car.rental.controller;

import com.example.car.rental.model.Rental;
import com.example.car.rental.service.JwtService;
import com.example.car.rental.service.PayPalService;
import com.example.car.rental.service.RentalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @Autowired
    private RentalService rentalService;

    @Value("${paypal.success.url}")
    private String successUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestBody Map<String, Object> request) throws UnsupportedEncodingException {
        double total = ((Number) request.get("total")).doubleValue();
        String currency = (String) request.get("currency");
        String method = (String) request.get("method");
        String intent = (String) request.get("intent");
        String description = (String) request.get("description");
        Integer vehicleId = Integer.parseInt(request.get("vehicleId").toString());
        Integer insuranceId = Integer.parseInt(request.get("insuranceId").toString());
        String startDate = (String) request.get("startDate");
        String endDate = (String) request.get("endDate");
        String notes = (String) request.get("notes");
        String userId = request.get("userId").toString();  // Dodanie userId z requestu

        // Kodowanie parametrów w URL
        String encodedSuccessUrl = successUrl + "?vehicleId=" + URLEncoder.encode(vehicleId.toString(), StandardCharsets.UTF_8.toString()) +
                "&insuranceId=" + URLEncoder.encode(insuranceId.toString(), StandardCharsets.UTF_8.toString()) +
                "&startDate=" + URLEncoder.encode(startDate, StandardCharsets.UTF_8.toString()) +
                "&endDate=" + URLEncoder.encode(endDate, StandardCharsets.UTF_8.toString()) +
                "&notes=" + URLEncoder.encode(notes != null ? notes : "", StandardCharsets.UTF_8.toString()) +
                "&userId=" + URLEncoder.encode(userId, StandardCharsets.UTF_8.toString());

        try {
            Payment payment = payPalService.createPayment(total, currency, method, intent, description, cancelUrl, encodedSuccessUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(500).body("Error during payment creation");
    }

    @GetMapping("/success")
    public RedirectView successPay(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("vehicleId") String vehicleId,
            @RequestParam("insuranceId") String insuranceId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam("userId") String userId) {
        try {
            System.out.println("User ID: " + userId);

            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                Date start = Date.valueOf(startDate);
                Date end = Date.valueOf(endDate);

                Rental rental = rentalService.createRental(Integer.valueOf(userId), Integer.valueOf(vehicleId), Integer.valueOf(insuranceId), start, end, notes);
                return new RedirectView("http://localhost:5173/payment-success");
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new RedirectView("http://localhost:5173/payment-failure");
    }

    @GetMapping("/cancel")
    public RedirectView cancelPay() {
        return new RedirectView("http://localhost:5173/payment-failure");
    }
}
