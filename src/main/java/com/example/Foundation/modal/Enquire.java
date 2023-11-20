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
@Table(name = "ENQUIRE_TBL")
public class Enquire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int enquireId;

    private String name;

    private String emailAddress;

    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    private String contactNumber;
    @Lob
    private byte[] image;

    private String description;

}
