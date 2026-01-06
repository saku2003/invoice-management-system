package org.example.exception;

public class AuthorizationException extends ApplicationException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, String errorCode) {
        super(message, errorCode);
    }
}
