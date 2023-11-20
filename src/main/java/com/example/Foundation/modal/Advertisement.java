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
@Table(name = "ADVERTISEMENT_TBL")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int AdId;

    private String name;

    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    private String contactNumber;

    private String emailAddress;

    @Lob
    private byte[] image;

}
