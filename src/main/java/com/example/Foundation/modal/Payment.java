package com.example.Foundation.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @Column(name = "receipt_id")
    private String receiptId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donor_donor_id")
   // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Donor donor;

}
