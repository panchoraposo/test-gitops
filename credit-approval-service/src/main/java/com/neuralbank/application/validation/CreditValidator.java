package com.neuralbank.application.validation;

import com.neuralbank.infrastructure.exception.InvalidCreditDataException;
import com.neuralbank.infrastructure.rest.dto.request.CreateCreditRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CreditValidator {
    
    public void validateCreateRequest(CreateCreditRequest request) {
        List<String> errors = new ArrayList<>();
        
        if (request.clienteId == null) {
            errors.add("Cliente ID es requerido");
        }
        
        if (request.tipoCredito == null) {
            errors.add("Tipo de crédito es requerido");
        }
        
        if (request.monedaId == null) {
            errors.add("Moneda ID es requerido");
        }
        
        if (request.montoSolicitado == null || request.montoSolicitado.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Monto solicitado debe ser mayor a cero");
        }
        
        if (request.tasaInteresAnual == null || request.tasaInteresAnual.compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Tasa de interés anual debe ser mayor o igual a cero");
        }
        
        if (request.plazoMeses == null || request.plazoMeses <= 0) {
            errors.add("Plazo en meses debe ser mayor a cero");
        }
        
        if (!errors.isEmpty()) {
            throw new InvalidCreditDataException("Errores de validación: " + String.join(", ", errors));
        }
    }
}

