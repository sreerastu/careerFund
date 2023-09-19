package com.example.Foundation.exception;

public class InvalidDonorIdException  extends Exception{


    public InvalidDonorIdException() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     */
    public InvalidDonorIdException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     * @param cause
     */
    public InvalidDonorIdException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param cause
     */
    public InvalidDonorIdException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
