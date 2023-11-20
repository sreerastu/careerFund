package com.example.Foundation.service;

import com.example.Foundation.exception.InvalidDonorIdException;
import com.example.Foundation.modal.DefaultPayment;
import com.example.Foundation.repositories.DefaultPaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DefaultPaymentServiceImpl {


    @Autowired
    private DefaultPaymentRepository paymentRepository;

    public DefaultPayment getDefaultPaymentById(int paymentId) throws Exception {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new Exception("Invalid Id"));
    }


    public DefaultPayment makeDefaultPayment(Double amount) throws InvalidDonorIdException, RazorpayException {
        // Create a new Payment record
        DefaultPayment payment = new DefaultPayment();
        payment.setAmount(amount);
        payment.setTimestamp(LocalDateTime.now());

        // Generate a unique receipt ID (you can customize this logic)
        String receiptId = "txn_" + UUID.randomUUID().toString();
        payment.setReceiptId(receiptId);


        // Create an order using Razorpay API
        var client = new RazorpayClient("rzp_test_mOsWfEZouKIqSx", "XgOFZ2sYI2nTODoh6Hwq1r05");
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Amount in paise (100 paise = 1 INR)
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receiptId);

        try {
            Order order = client.orders.create(orderRequest);
        } catch (Exception e) {
            // Handle exceptions, e.g., if the order creation fails
            e.printStackTrace();
            // You should add proper error handling here
            return null;
        }
        // Save the payment record
        return paymentRepository.save(payment);
    }


    public Optional<DefaultPayment> getDefaultPaymentHistory(int defaultId) throws InvalidDonorIdException {


        return paymentRepository.findById(defaultId);
    }

    public List<DefaultPayment> getAllDefaultPayments() {
        return paymentRepository.findAll();
    }

    public List<DefaultPayment> getAllTopDefaultPayments() {
        List<DefaultPayment> list = paymentRepository.findAll();
        List<DefaultPayment> sortedList = list.stream()
                .sorted(Comparator.comparing(DefaultPayment::getAmount).reversed())
                .limit(10)// Sort by Payment amount in descending order
                .collect(Collectors.toList());
        return sortedList;
    }

}
