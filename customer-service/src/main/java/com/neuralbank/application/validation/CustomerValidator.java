package com.neuralbank.application.validation;

import com.neuralbank.infrastructure.exception.InvalidCustomerDataException;
import com.neuralbank.infrastructure.rest.dto.request.CreateCustomerRequest;
import com.neuralbank.infrastructure.rest.dto.request.UpdateCustomerRequest;
import com.neuralbank.shared.util.IdentificationValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CustomerValidator {
    
    @Inject
    IdentificationValidator identificationValidator;
    
    public void validateCreateRequest(CreateCustomerRequest request) {
        List<String> errors = new ArrayList<>();
        
        if (request.identificacion == null || request.identificacion.trim().isEmpty()) {
            errors.add("Identificación es requerida");
        }
        
        if (request.tipoIdentificacion == null || request.tipoIdentificacion.trim().isEmpty()) {
            errors.add("Tipo de identificación es requerido");
        }
        
        if (request.nombre == null || request.nombre.trim().isEmpty()) {
            errors.add("Nombre es requerido");
        }
        
        if (request.paisId == null) {
            errors.add("País es requerido");
        }
        
        if (request.email != null && !isValidEmail(request.email)) {
            errors.add("Email inválido");
        }
        
        if (request.identificacion != null && request.tipoIdentificacion != null) {
            if (!identificationValidator.validate(request.identificacion, request.tipoIdentificacion)) {
                errors.add("Formato de identificación inválido para el tipo especificado");
            }
        }
        
        if (!errors.isEmpty()) {
            throw new InvalidCustomerDataException("Errores de validación: " + String.join(", ", errors));
        }
    }
    
    public void validateUpdateRequest(UpdateCustomerRequest request) {
        List<String> errors = new ArrayList<>();
        
        if (request.email != null && !isValidEmail(request.email)) {
            errors.add("Email inválido");
        }
        
        if (!errors.isEmpty()) {
            throw new InvalidCustomerDataException("Errores de validación: " + String.join(", ", errors));
        }
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}