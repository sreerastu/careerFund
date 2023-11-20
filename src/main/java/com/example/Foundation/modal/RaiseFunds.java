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
@Table(name = "RAISEFUNDS_TBL")
public class RaiseFunds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rfId;

    private Double description;
}
