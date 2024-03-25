package com.example.Foundation.modal;


import com.example.Foundation.Enum.Course;
import com.example.Foundation.Enum.Gender;
import com.example.Foundation.Enum.UserType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "STUDENT_TBL")
public class Student implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int studentId;
    protected String firstName;
    protected String lastName;
    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    protected String contactNumber;
    @Column(nullable = false)
    protected String password;
    @Column(nullable = false,unique = true)
    protected String emailAddress;

   /* @Pattern(regexp = "^[2-9][0-9]{3} [0-9]{4} [0-9]{4}$", message = "Invalid Aadhaar number format")
    @Column(unique = true)
    private String aadhaarNumber;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$", message = "Invalid PAN number format")
    @Column(unique = true)
    private String panNumber;*/

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Boolean placed;

    @Enumerated(EnumType.STRING)
    private Course course;

    private String description;

    private String image;  // New field to store image path

    @Enumerated(EnumType.STRING)
    private UserType userType;


    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @JsonBackReference
    private Trainer trainer;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonBackReference
    private SuccessStories successStories;

    // One-to-Many mapping with EmailVerificationToken
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EmailVerificationToken> emailVerificationTokens;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority(userType.name()));

        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.emailAddress;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
