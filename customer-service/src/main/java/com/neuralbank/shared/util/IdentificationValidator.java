package com.neuralbank.shared.util;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.regex.Pattern;

@ApplicationScoped
public class IdentificationValidator {
    
    public boolean validate(String identificacion, String tipoIdentificacion) {
        if (identificacion == null || tipoIdentificacion == null) {
            return false;
        }
        
        switch (tipoIdentificacion.toUpperCase()) {
            case "SSN":
                return validateSSN(identificacion);
            case "RFC":
                return validateRFC(identificacion);
            case "CURP":
                return validateCURP(identificacion);
            case "CPF":
                return validateCPF(identificacion);
            case "CUIT":
                return validateCUIT(identificacion);
            case "RUT":
            case "RUN":
                return validateRUT(identificacion);
            case "DNI":
                return validateDNI(identificacion);
            case "PASSPORT":
                return validatePassport(identificacion);
            case "NINO":
                return validateNINO(identificacion);
            case "EIN":
                return validateEIN(identificacion);
            default:
                return true; // Otros tipos sin validación específica
        }
    }
    
    private boolean validateSSN(String ssn) {
        // Formato: 123-45-6789 o 123456789
        String pattern = "^\\d{3}-?\\d{2}-?\\d{4}$";
        return Pattern.matches(pattern, ssn);
    }
    
    private boolean validateRFC(String rfc) {
        // Formato: AAAA123456XXX (13 caracteres)
        String pattern = "^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$";
        return Pattern.matches(pattern, rfc.toUpperCase());
    }
    
    private boolean validateCURP(String curp) {
        // Formato: 18 caracteres alfanuméricos
        String pattern = "^[A-Z]{4}\\d{6}[HM][A-Z]{5}[0-9A-Z]\\d$";
        return Pattern.matches(pattern, curp.toUpperCase());
    }
    
    private boolean validateCPF(String cpf) {
        // Formato: 123.456.789-00 o 12345678900
        String cleanCPF = cpf.replaceAll("[.\\-]", "");
        return Pattern.matches("^\\d{11}$", cleanCPF);
    }
    
    private boolean validateCUIT(String cuit) {
        // Formato: 20-12345678-9 o 20123456789
        String cleanCUIT = cuit.replaceAll("-", "");
        return Pattern.matches("^\\d{11}$", cleanCUIT);
    }
    
    private boolean validateRUT(String rut) {
        // Formato: 12.345.678-9 o 12345678-9
        String pattern = "^\\d{1,2}\\.?\\d{3}\\.?\\d{3}-?[\\dkK]$";
        return Pattern.matches(pattern, rut);
    }
    
    private boolean validateDNI(String dni) {
        // Formato: 12345678A (España) o solo números
        String pattern = "^\\d{7,9}[A-Z]?$";
        return Pattern.matches(pattern, dni.toUpperCase());
    }
    
    private boolean validatePassport(String passport) {
        // Formato genérico: 6-9 caracteres alfanuméricos
        return passport.length() >= 6 && passport.length() <= 9;
    }
    
    private boolean validateNINO(String nino) {
        // Formato UK: AB123456C
        String pattern = "^[A-Z]{2}\\d{6}[A-D]$";
        return Pattern.matches(pattern, nino.toUpperCase());
    }
    
    private boolean validateEIN(String ein) {
        // Formato: 12-3456789
        String pattern = "^\\d{2}-?\\d{7}$";
        return Pattern.matches(pattern, ein);
    }
}