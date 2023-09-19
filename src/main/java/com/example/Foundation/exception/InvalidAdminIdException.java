package com.example.Foundation.exception;

public class InvalidAdminIdException extends Exception{

    public InvalidAdminIdException() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     */
    public InvalidAdminIdException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     * @param cause
     */
    public InvalidAdminIdException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param cause
     */
    public InvalidAdminIdException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
