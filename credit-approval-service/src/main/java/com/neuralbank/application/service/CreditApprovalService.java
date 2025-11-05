package com.neuralbank.application.service;

import com.neuralbank.infrastructure.rest.dto.request.CreditApprovalRequest;
import com.neuralbank.infrastructure.rest.dto.request.CreateCreditRequest;
import com.neuralbank.infrastructure.rest.dto.response.CreditApprovalResponse;
import com.neuralbank.infrastructure.rest.dto.response.CreditResponse;

import java.util.List;

public interface CreditApprovalService {
    
    CreditApprovalResponse evaluateCreditApproval(CreditApprovalRequest request);
    
    CreditApprovalResponse approveCredit(Long creditId);
    
    CreditApprovalResponse rejectCredit(Long creditId, String reason);
    
    CreditResponse createCreditRequest(CreateCreditRequest request);
    
    CreditResponse getCreditById(Long creditId);
    
    List<CreditResponse> getCreditsByClienteId(Long clienteId);
    
    List<CreditResponse> getCreditsByStatus(String status);
    
    CreditApprovalResponse recalculateApproval(Long creditId);
}

