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
@Table(name = "SUPER_ADMIN_TBL")
public class SuperAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int sId;
    protected String sName;
    @Column(nullable = false)
    protected String sEmail;
    @Column(nullable = false)
    protected String sPassword;

    @Enumerated(value = EnumType.STRING)
    protected Gender gender;

}
