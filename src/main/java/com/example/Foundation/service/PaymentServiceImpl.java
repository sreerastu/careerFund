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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl {

    final static Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonorServiceImpl donorService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${spring.mail.username}")
    private String sender;

    public Payment getPaymentById(int paymentId) throws Exception {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new Exception("Invalid Id"));
    }


    public Payment makePayment(int donorId, Double amount) throws InvalidDonorIdException, RazorpayException, MessagingException {
        // Create a new Payment record
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setTimestamp(LocalDateTime.now());

        Donor donor = donorRepository.findById(donorId).orElseThrow(() -> new InvalidDonorIdException("Donor not found for ID: " + donorId));

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

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Payment Confirmation for Invoice" + ":" + receiptId );
        helper.setText("This is to confirm that we have received your payment"+
                " The total amount received is" +":"+ amount +
                ". Thank you for your payment. We appreciate your interest for supporting CareerFund " +
                "Best regards" +
                "CareerFund");
        helper.setFrom(sender);
        helper.setTo(donor.getEmailAddress());
        javaMailSender.send(message);
        log.info("Mail Sent Successfully......");

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
