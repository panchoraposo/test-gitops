package com.neuralbank.application.service.impl;

import com.neuralbank.application.mapper.CreditMapper;
import com.neuralbank.application.service.CreditApprovalService;
import com.neuralbank.application.validation.CreditValidator;
import com.neuralbank.domain.entity.Credit;
import com.neuralbank.domain.entity.Customer;
import com.neuralbank.domain.enums.CreditStatus;
import com.neuralbank.domain.repository.CreditRepository;
import com.neuralbank.domain.repository.CustomerRepository;
import com.neuralbank.infrastructure.exception.CreditNotFoundException;
import com.neuralbank.infrastructure.exception.CustomerNotFoundException;
import com.neuralbank.infrastructure.rest.dto.request.CreditApprovalRequest;
import com.neuralbank.infrastructure.rest.dto.request.CreateCreditRequest;
import com.neuralbank.infrastructure.rest.dto.response.CreditApprovalResponse;
import com.neuralbank.infrastructure.rest.dto.response.CreditResponse;
import com.neuralbank.shared.util.CreditApprovalEngine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CreditApprovalServiceImpl implements CreditApprovalService {
    
    @Inject
    CreditRepository creditRepository;
    
    @Inject
    CustomerRepository customerRepository;
    
    @Inject
    CreditMapper creditMapper;
    
    @Inject
    CreditValidator creditValidator;
    
    @Inject
    CreditApprovalEngine approvalEngine;
    
    @Override
    @Transactional
    public CreditApprovalResponse evaluateCreditApproval(CreditApprovalRequest request) {
        // Obtener crédito - si no hay creditId, buscar por clienteId (último crédito solicitado)
        Credit credit;
        if (request.creditId != null) {
            credit = creditRepository.findByIdOptional(request.creditId)
                    .orElseThrow(() -> new CreditNotFoundException("Crédito con ID " + request.creditId + " no encontrado"));
        } else if (request.clienteId != null) {
            // Buscar el último crédito solicitado del cliente
            List<Credit> credits = creditRepository.findByClienteId(request.clienteId);
            credit = credits.stream()
                    .filter(c -> c.estado == CreditStatus.Solicitado || c.estado == CreditStatus.EnRevision)
                    .findFirst()
                    .orElseThrow(() -> new CreditNotFoundException("No se encontró crédito pendiente para el cliente " + request.clienteId));
        } else {
            throw new IllegalArgumentException("Se requiere creditId o clienteId");
        }
        
        // Obtener cliente
        Customer customer = customerRepository.findByIdOptional(credit.clienteId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente con ID " + credit.clienteId + " no encontrado"));
        
        // Evaluar aprobación
        CreditApprovalEngine.ApprovalResult result = approvalEngine.evaluateCreditApproval(
                credit,
                customer,
                request.salarioMensual
        );
        
        // Convertir a response
        CreditApprovalResponse response = toApprovalResponse(result);
        
        // Si está aprobado, actualizar el estado del crédito
        if (result.approved) {
            credit.estado = CreditStatus.Aprobado;
            credit.fechaAprobacion = LocalDate.now();
            credit.montoAprobado = credit.montoSolicitado;
            credit.scoreAprobacion = result.creditScore;
        }
        
        return response;
    }
    
    @Override
    @Transactional
    public CreditApprovalResponse approveCredit(Long creditId) {
        Credit credit = creditRepository.findByIdOptional(creditId)
                .orElseThrow(() -> new CreditNotFoundException("Crédito con ID " + creditId + " no encontrado"));
        
        Customer customer = customerRepository.findByIdOptional(credit.clienteId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado"));
        
        // Evaluar aprobación
        CreditApprovalEngine.ApprovalResult result = approvalEngine.evaluateCreditApproval(
                credit,
                customer,
                null
        );
        
        if (!result.approved) {
            throw new IllegalStateException("El crédito no puede ser aprobado: " + result.getRejectionReason());
        }
        
        // Aprobar crédito
        credit.estado = CreditStatus.Aprobado;
        credit.fechaAprobacion = LocalDate.now();
        credit.montoAprobado = credit.montoSolicitado;
        credit.scoreAprobacion = result.creditScore;
        
        return toApprovalResponse(result);
    }
    
    @Override
    @Transactional
    public CreditApprovalResponse rejectCredit(Long creditId, String reason) {
        Credit credit = creditRepository.findByIdOptional(creditId)
                .orElseThrow(() -> new CreditNotFoundException("Crédito con ID " + creditId + " no encontrado"));
        
        credit.estado = CreditStatus.Rechazado;
        
        if (credit.metadata == null) {
            credit.metadata = new java.util.HashMap<>();
        }
        credit.metadata.put("rejection_reason", reason);
        credit.metadata.put("rejection_date", java.time.LocalDateTime.now().toString());
        
        CreditApprovalResponse response = new CreditApprovalResponse();
        response.creditId = creditId;
        response.clienteId = credit.clienteId;
        response.approved = false;
        response.rejectionReason = reason;
        
        return response;
    }
    
    @Override
    @Transactional
    public CreditResponse createCreditRequest(CreateCreditRequest request) {
        creditValidator.validateCreateRequest(request);
        
        Credit credit = creditMapper.toEntity(request);
        creditRepository.persist(credit);
        
        return creditMapper.toResponse(credit);
    }
    
    @Override
    public CreditResponse getCreditById(Long creditId) {
        Credit credit = creditRepository.findByIdOptional(creditId)
                .orElseThrow(() -> new CreditNotFoundException("Crédito con ID " + creditId + " no encontrado"));
        return creditMapper.toResponse(credit);
    }
    
    @Override
    public List<CreditResponse> getCreditsByClienteId(Long clienteId) {
        List<Credit> credits = creditRepository.findByClienteId(clienteId);
        return credits.stream()
                .map(creditMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CreditResponse> getCreditsByStatus(String status) {
        CreditStatus creditStatus = CreditStatus.valueOf(status);
        List<Credit> credits = creditRepository.findByEstado(creditStatus);
        return credits.stream()
                .map(creditMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CreditApprovalResponse recalculateApproval(Long creditId) {
        CreditApprovalRequest request = new CreditApprovalRequest();
        request.creditId = creditId;
        return evaluateCreditApproval(request);
    }
    
    private CreditApprovalResponse toApprovalResponse(CreditApprovalEngine.ApprovalResult result) {
        CreditApprovalResponse response = new CreditApprovalResponse();
        response.creditId = result.creditId;
        response.clienteId = result.clienteId;
        response.approved = result.approved;
        response.rejectionReason = result.getRejectionReason();
        response.montoCuota = result.montoCuota;
        response.salarioMensual = result.salarioMensual;
        response.debtToIncomeRatio = result.debtToIncomeRatio;
        response.capacityRuleResult = result.capacityRuleResult;
        response.capacityRuleMessage = result.capacityRuleResult == com.neuralbank.domain.enums.ApprovalRuleResult.APPROVED 
                ? "Regla #1 aprobada: Debt to Income Ratio < 50%" 
                : "Regla #1 rechazada: Debt to Income Ratio >= 50%";
        response.willingnessToPayRuleResult = result.willingnessToPayRuleResult;
        response.willingnessToPayRuleMessage = result.willingnessToPayRuleResult == com.neuralbank.domain.enums.ApprovalRuleResult.APPROVED 
                ? "Regla #2 aprobada: Credit Score > 500" 
                : "Regla #2 rechazada: Credit Score <= 500";
        response.creditScore = result.creditScore;
        return response;
    }
}

