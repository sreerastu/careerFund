package com.example.Foundation.dto;

import com.example.Foundation.Enum.UserType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginApiDto {

    private String emailAddress;

    private String password;

    //private UserType userType;
}
