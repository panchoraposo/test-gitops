package com.neuralbank.infrastructure.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreditScoreResponse {
    
    public Long customerId;
    public BigDecimal score;
    public String nivelRiesgo;
    public String evaluacion;
    public LocalDateTime calculatedAt;
    
    public CreditScoreResponse() {
        this.calculatedAt = LocalDateTime.now();
    }
}