package com.example.Foundation.exception;

public class AdminNotFoundException extends Exception{


    public AdminNotFoundException() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     */
    public AdminNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     * @param cause
     */
    public AdminNotFoundException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param cause
     */
    public AdminNotFoundException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
