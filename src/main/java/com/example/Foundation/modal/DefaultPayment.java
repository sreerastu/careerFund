package com.example.Foundation.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "DEFAULT_PAYMENT_TBL")
public class DefaultPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int DefaultPaymentId;
    private Double amount;
    private LocalDateTime timestamp;

    @Column(name = "receipt_id")
    private String receiptId;
}
