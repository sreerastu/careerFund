package com.example.Foundation.exception;

public class StudentNotFoundException extends Exception{

    public StudentNotFoundException() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     */
    public StudentNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     * @param cause
     */
    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param cause
     */
    public StudentNotFoundException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
