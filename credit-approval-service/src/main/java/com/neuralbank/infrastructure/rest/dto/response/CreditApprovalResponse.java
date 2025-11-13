package com.neuralbank.infrastructure.rest.dto.response;

import com.neuralbank.domain.enums.ApprovalRuleResult;

import java.math.BigDecimal;

public class CreditApprovalResponse {
    
    public Long creditId;
    public Long clienteId;
    public boolean approved;
    public String rejectionReason;
    
    // Detalles de evaluación
    public BigDecimal montoCuota;
    public BigDecimal salarioMensual;
    public BigDecimal debtToIncomeRatio;
    public ApprovalRuleResult capacityRuleResult;
    public String capacityRuleMessage;
    public ApprovalRuleResult willingnessToPayRuleResult;
    public String willingnessToPayRuleMessage;
    public BigDecimal creditScore;
}

