package com.example.Foundation.exception;

public class InvalidStudentIdException  extends Exception{
    public InvalidStudentIdException() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     */
    public InvalidStudentIdException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param message
     * @param cause
     */
    public InvalidStudentIdException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param cause
     */
    public InvalidStudentIdException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
