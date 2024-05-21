package com.example.car.rental.controller;

import com.example.car.rental.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/pay")
    public String payment() {
        String cancelUrl = "http://localhost:8080/cancel";
        String successUrl = "http://localhost:8080/success";
        try {
            Payment payment = payPalService.createPayment(
                    10.00,
                    "USD",
                    "paypal",
                    "sale",
                    "Test payment",
                    cancelUrl,
                    successUrl);
            for(Links links : payment.getLinks()) {
                if(links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "redirect:/success";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/cancel")
    public String cancelPay() {
        return "redirect:/";
    }
}

