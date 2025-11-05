package com.neuralbank.infrastructure.rest.dto.request;

import com.neuralbank.domain.enums.CreditType;

import java.math.BigDecimal;

public class CreditApprovalRequest {
    
    public Long creditId;
    public Long clienteId;
    public BigDecimal salarioMensual; // Opcional, se calcula si no se proporciona
}

