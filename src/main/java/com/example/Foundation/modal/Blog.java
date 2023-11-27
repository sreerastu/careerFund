package com.example.Foundation.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "BLOG_TBL")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bId;

    @Lob
    private byte[] image;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    Date postedDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    Date upcomingDateTime;
}
