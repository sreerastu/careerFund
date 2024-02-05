package com.example.Foundation.modal;


import com.example.Foundation.Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "CLIENT_TBL")
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int clientId;
    protected String firstName;
    protected String lastName;
    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    protected String contactNumber;
    @Column(nullable = false)
    protected String password;
    @Lob
    private byte[] image;
    @Column(nullable = false, unique = true)
    protected String emailAddress;
    private String companyName;
    private String description;


}
