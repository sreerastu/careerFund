package com.example.Foundation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class
ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;
    private int code;

    public ExceptionResponse(Date timestamp, String message, String details, HttpStatus httpStatus) {
        super();
        this.code = httpStatus.value();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
