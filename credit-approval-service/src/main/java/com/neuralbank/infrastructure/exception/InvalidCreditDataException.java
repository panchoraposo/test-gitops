package com.neuralbank.infrastructure.exception;

public class InvalidCreditDataException extends RuntimeException {
    
    public InvalidCreditDataException(String message) {
        super(message);
    }
}

