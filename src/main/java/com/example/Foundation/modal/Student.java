package com.example.Foundation.modal;


import com.example.Foundation.Enum.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "STUDENT_TBL")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int studentId;
    protected String firstName;
    protected String lastName;
    protected String contactNumber;
    @Column(nullable = false)
    protected String password;
    @Column(nullable = false,unique = true)
    protected String emailAddress;
    @Enumerated(EnumType.STRING)
    private Gender gender;




}
