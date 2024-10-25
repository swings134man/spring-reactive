package com.lucas.moviereviewboot.exception;

/**
 * Exception class for Review not found.
 */
public class ReviewNotFoundException extends RuntimeException {

    private String message;
    private Throwable ex;

    public ReviewNotFoundException(String message, Throwable cause, String message1, Throwable ex) {
        super(message, cause);
        this.message = message1;
        this.ex = ex;
    }

    public ReviewNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
