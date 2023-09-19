package com.example.Foundation.exception;

public class DonorNotFoundException  extends Exception{

    public DonorNotFoundException() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     */
    public DonorNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     * @param cause
     */
    public DonorNotFoundException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param cause
     */
    public DonorNotFoundException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
