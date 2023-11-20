package com.example.Foundation.controller;

import com.example.Foundation.exception.InvalidDonorIdException;
import com.example.Foundation.modal.DefaultPayment;
import com.example.Foundation.service.DefaultPaymentServiceImpl;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class DefaultPaymentController {

    @Autowired
    private DefaultPaymentServiceImpl paymentService;


    @PostMapping("/makeDefaultPayment")
    public ResponseEntity<?> makeDefaultPayment(@RequestParam Double amount) throws InvalidDonorIdException, RazorpayException {

        DefaultPayment payment = paymentService.makeDefaultPayment(amount);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment creation failed");
        }
    }

    @GetMapping("/defaultPayment/{defaultId}")
    public ResponseEntity<?> getDefaultPaymentHistory(@PathVariable int defaultId) throws InvalidDonorIdException {
        Optional<DefaultPayment> paymentHistory = paymentService.getDefaultPaymentHistory(defaultId);
        return ResponseEntity.ok(paymentHistory);
    }

    @GetMapping("/defaultPayments")
    public ResponseEntity<?> getAllDefaultPayments() {
        List<DefaultPayment> payments = paymentService.getAllDefaultPayments();
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @GetMapping("/topDefaultPayments")
    public ResponseEntity<?> getAllTopPayments() {
        List<DefaultPayment> payments = paymentService.getAllTopDefaultPayments();
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

}
