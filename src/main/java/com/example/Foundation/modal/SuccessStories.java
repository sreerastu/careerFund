package com.example.Foundation.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "SuccessStories_TBL")
public class SuccessStories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ssId;

    @Lob
    private byte[] image;

    private String description;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private Student student;
}
