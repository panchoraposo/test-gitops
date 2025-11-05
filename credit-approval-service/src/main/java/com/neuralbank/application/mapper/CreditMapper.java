package com.neuralbank.application.mapper;

import com.neuralbank.domain.entity.Credit;
import com.neuralbank.domain.enums.CreditStatus;
import com.neuralbank.infrastructure.rest.dto.request.CreateCreditRequest;
import com.neuralbank.infrastructure.rest.dto.response.CreditResponse;
import com.neuralbank.shared.util.CreditApprovalEngine;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.UUID;

@ApplicationScoped
public class CreditMapper {
    
    public Credit toEntity(CreateCreditRequest request) {
        Credit credit = new Credit();
        
        credit.clienteId = request.clienteId;
        credit.cuentaDesembolsoId = request.cuentaDesembolsoId;
        credit.numeroCredito = request.numeroCredito != null 
                ? request.numeroCredito 
                : generateCreditNumber();
        credit.tipoCredito = request.tipoCredito;
        credit.monedaId = request.monedaId;
        credit.montoSolicitado = request.montoSolicitado;
        credit.tasaInteresAnual = request.tasaInteresAnual;
        credit.plazoMeses = request.plazoMeses;
        credit.fechaSolicitud = request.fechaSolicitud != null 
                ? request.fechaSolicitud 
                : LocalDate.now();
        credit.destinoCredito = request.destinoCredito;
        credit.ejecutivoId = request.ejecutivoId;
        credit.estado = CreditStatus.Solicitado;
        
        return credit;
    }
    
    public CreditResponse toResponse(Credit credit) {
        CreditResponse response = new CreditResponse();
        
        response.id = credit.id;
        response.clienteId = credit.clienteId;
        response.cuentaDesembolsoId = credit.cuentaDesembolsoId;
        response.numeroCredito = credit.numeroCredito;
        response.tipoCredito = credit.tipoCredito;
        response.monedaId = credit.monedaId;
        response.montoSolicitado = credit.montoSolicitado;
        response.montoAprobado = credit.montoAprobado;
        response.tasaInteresAnual = credit.tasaInteresAnual;
        response.plazoMeses = credit.plazoMeses;
        response.fechaSolicitud = credit.fechaSolicitud;
        response.fechaAprobacion = credit.fechaAprobacion;
        response.fechaDesembolso = credit.fechaDesembolso;
        response.fechaPrimerVencimiento = credit.fechaPrimerVencimiento;
        response.estado = credit.estado;
        response.destinoCredito = credit.destinoCredito;
        response.scoreAprobacion = credit.scoreAprobacion;
        response.ejecutivoId = credit.ejecutivoId;
        response.metadata = credit.metadata;
        response.createdAt = credit.createdAt;
        response.updatedAt = credit.updatedAt;
        
        return response;
    }
    
    private String generateCreditNumber() {
        return "CRED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

