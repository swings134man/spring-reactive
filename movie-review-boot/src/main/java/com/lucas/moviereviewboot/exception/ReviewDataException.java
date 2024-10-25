package com.lucas.moviereviewboot.exception;

/**
 * Exception class for Review data.
 */
public class ReviewDataException extends RuntimeException {
    private String message;

    public ReviewDataException(String message) {
        super(message);
        this.message = message;
    }
}
