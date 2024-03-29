package com.example.Foundation.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CLIENTS_TBL")
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientId;

    private String name;

    private String emailAddress;

    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    private String contactNumber;

    private String image;  // New field to store image path

    private String description;

}
