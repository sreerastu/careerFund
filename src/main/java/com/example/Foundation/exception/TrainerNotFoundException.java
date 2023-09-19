package com.example.Foundation.exception;

public class TrainerNotFoundException extends Exception{

    public TrainerNotFoundException() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     */
    public TrainerNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     * @param cause
     */
    public TrainerNotFoundException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param cause
     */
    public TrainerNotFoundException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
