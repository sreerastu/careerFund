package com.example.Foundation.modal;

import com.example.Foundation.Enum.Course;
import com.example.Foundation.Enum.Gender;
import com.example.Foundation.Enum.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "TRAINER_TBL")
public class Trainer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int trainerId;
    protected String firstName;
    protected String lastName;
    @Size(min = 10, max = 10, message = "Contact number must be 10 digits")
    protected String contactNumber;
    @Column(nullable = false)
    protected String password;
    @Column(nullable = false, unique = true)
    protected String emailAddress;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Course course;

    private String certification;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String image;


    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Student> students = new ArrayList<>();


    // One-to-Many mapping with EmailVerificationToken
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
