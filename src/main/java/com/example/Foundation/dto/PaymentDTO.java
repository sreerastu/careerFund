package com.example.Foundation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDTO {

    private Long paymentId;
    private Double amount;
    private LocalDateTime timestamp;
    private String receiptId;
    private String donorFirstName;

}