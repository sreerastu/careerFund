package com.example.Foundation.modal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "ARTICLES_TBL")
public class Articles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int articleId;
    protected String title;
    private String image;  // New field to store image path

    private String description;
    private String categoryType;


}
