package com.example.Foundation.modal;


import com.example.Foundation.Enum.Gender;
import com.example.Foundation.Enum.UserType;
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
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "DONOR_TBL")
public class Donor implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int donorId;
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
    private UserType userType;


    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Payment> payments = new ArrayList<>();


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
