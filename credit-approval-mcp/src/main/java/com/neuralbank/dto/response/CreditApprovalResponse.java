package com.neuralbank.dto.response;

import com.neuralbank.enums.ApprovalRuleResult;

import java.math.BigDecimal;

public class CreditApprovalResponse {
    
    public Long creditId;
    public Long clienteId;
    public boolean approved;
    public String rejectionReason;
    
    public BigDecimal montoCuota;
    public BigDecimal salarioMensual;
    public BigDecimal debtToIncomeRatio;
    public ApprovalRuleResult capacityRuleResult;
    public String capacityRuleMessage;
    public ApprovalRuleResult willingnessToPayRuleResult;
    public String willingnessToPayRuleMessage;
    public BigDecimal creditScore;
}

