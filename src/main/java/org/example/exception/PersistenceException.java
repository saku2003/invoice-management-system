package org.example.exception;

public class PersistenceException extends ApplicationException {
    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(String message, String errorCode) {
        super(message, errorCode);
    }
}
