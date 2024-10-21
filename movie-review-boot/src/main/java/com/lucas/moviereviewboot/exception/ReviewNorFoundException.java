package com.lucas.moviereviewboot.exception;

/**
 * Exception class for Review not found.
 */
public class ReviewNorFoundException extends RuntimeException {

    private String message;
    private Throwable ex;

    public ReviewNorFoundException(String message, Throwable cause, String message1, Throwable ex) {
        super(message, cause);
        this.message = message1;
        this.ex = ex;
    }

    public ReviewNorFoundException(String message, String message1) {
        super(message);
        this.message = message1;
    }
}
