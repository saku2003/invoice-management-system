package org.example.exception;

public class BusinessRuleException extends ApplicationException {
    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, String errorCode) {
        super(message, errorCode);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
