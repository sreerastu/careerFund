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
@Entity
@ToString
@Table(name = "EMAIL_VERIFICATION_TBL")
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String otp;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    // Many-to-One mapping with Donor
    @ManyToOne
    @JoinColumn(name = "donor_id")
    private Donor donor;

    // Many-to-One mapping with Trainer
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    // Many-to-One mapping with Student
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Many-to-One mapping with Admin
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;


}
