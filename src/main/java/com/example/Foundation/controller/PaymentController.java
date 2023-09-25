package com.example.Foundation.controller;

import com.example.Foundation.exception.InvalidDonorIdException;
import com.example.Foundation.modal.Donor;
import com.example.Foundation.modal.Payment;
import com.example.Foundation.service.DonorServiceImpl;
import com.example.Foundation.service.PaymentServiceImpl;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class PaymentController {
    private final PaymentServiceImpl paymentService;
    private final DonorServiceImpl donorService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService, DonorServiceImpl donorService) {
        this.paymentService = paymentService;
        this.donorService = donorService;
    }

    @PostMapping("/donor/{donorId}/makePayment")
    public ResponseEntity<?> makePayment(@PathVariable int donorId, @RequestParam Double amount) throws InvalidDonorIdException, RazorpayException {

        Donor donor = donorService.getDonorById(donorId);
        if (donor != null) {
            Payment payment = paymentService.makePayment(donorId, amount);
            if (payment != null) {
                return ResponseEntity.ok(payment);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Payment creation failed");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Donor not found");
        }
    }

    @GetMapping("/donor/{donorId}/history")
    public ResponseEntity<?> getPaymentHistory(@PathVariable int donorId) throws InvalidDonorIdException {
        Optional<Donor> optionalDonor = Optional.ofNullable(donorService.getDonorById(donorId));
        if (optionalDonor.isPresent()) {
            List<Payment> paymentHistory = paymentService.getPaymentHistory(donorId);
            return ResponseEntity.ok(paymentHistory);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Donor not found");
        }
    }

    @GetMapping("/payments")
    public ResponseEntity<?> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }
    @GetMapping("/topPayments")
    public ResponseEntity<?> getAllTopPayments() {
        List<Payment> payments = paymentService.getAllTopPayments();
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

}
