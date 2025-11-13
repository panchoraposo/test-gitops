package com.neuralbank.infrastructure.rest.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    
    public String message;
    public int status;
    public LocalDateTime timestamp;
    public String path;
    public List<String> errors;
    
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String message, int status, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}