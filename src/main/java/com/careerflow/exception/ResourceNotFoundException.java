package com.careerflow.exception;

/** Thrown when a record does not exist OR belongs to another student. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
