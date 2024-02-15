package com.example.Foundation.modal;

import com.example.Foundation.Enum.Course;
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
@Table(name = "TECHNOLOGIES_TBL")
public class Technologies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int techId;

    @Enumerated(EnumType.STRING)
    private Course techTitle;

    private String image;

    private String description;
}
