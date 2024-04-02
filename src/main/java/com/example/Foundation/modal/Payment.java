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
@Table(name = "PAYMENT_TBL")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;
    private Double amount;
    private LocalDateTime timestamp;

    private String bankRRN;
    private String paymentMethod;

    @Column(name = "receipt_id")
    private String receiptId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donor_donor_id")
    private Donor donor;

}
