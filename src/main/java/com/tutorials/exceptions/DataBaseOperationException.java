package com.tutorials.exceptions;

public class DataBaseOperationException extends Exception {
    public DataBaseOperationException(String message) {
        super(message);
    }
    public DataBaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
