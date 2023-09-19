package com.example.Foundation.exception;

public class InvalidTrainerIdException extends Exception{

    public InvalidTrainerIdException() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     */
    public InvalidTrainerIdException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     * @param cause
     */
    public InvalidTrainerIdException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param cause
     */
    public InvalidTrainerIdException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
