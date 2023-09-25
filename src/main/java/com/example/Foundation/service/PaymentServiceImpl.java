package com.example.Foundation.service;

import com.example.Foundation.exception.InvalidDonorIdException;
import com.example.Foundation.modal.Donor;
import com.example.Foundation.modal.Payment;
import com.example.Foundation.repositories.DonorRepository;
import com.example.Foundation.repositories.PaymentRepository;
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
public class PaymentServiceImpl {


    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonorServiceImpl donorService;

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment getPaymentById(int paymentId) throws Exception {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new Exception("Invalid Id"));
    }


    public Payment makePayment(int donorId, Double amount) throws InvalidDonorIdException, RazorpayException {
        // Create a new Payment record
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setTimestamp(LocalDateTime.now());

        // Find the donor by ID and associate the payment
        Optional<Donor> optionalDonor = Optional.ofNullable(donorService.getDonorById(donorId));
        if (optionalDonor.isPresent()) {
            Donor donor = optionalDonor.get();
            payment.setDonor(donor);

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
        } else {
            // Handle the case where the donor is not found
            return null;
        }
    }

    public List<Payment> getPaymentHistory(int donorId) throws InvalidDonorIdException {


        Donor donor = donorRepository.findById(donorId).orElseThrow(() -> new InvalidDonorIdException("Invalid donorId" + donorId));
        // Check if the donor exists
        if (donor != null) {
            // Retrieve payment history for the donor from the repository
            return paymentRepository.findByDonorId(donorId);
        } else {
            throw new InvalidDonorIdException("Invalid donorId" + ":" + donorId);
        }
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getAllTopPayments() {
        List<Payment> list = paymentRepository.findAll();
        List<Payment> sortedList = list.stream()
                .sorted(Comparator.comparing(Payment::getAmount).reversed()) // Sort by Payment amount in descending order
                .limit(10) // Limit to the top 10 payments
                .collect(Collectors.toList());
        return sortedList;
    }

}
