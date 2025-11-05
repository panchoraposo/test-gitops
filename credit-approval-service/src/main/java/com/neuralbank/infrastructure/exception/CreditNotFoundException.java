package com.neuralbank.infrastructure.exception;

public class CreditNotFoundException extends RuntimeException {
    
    public CreditNotFoundException(String message) {
        super(message);
    }
}

