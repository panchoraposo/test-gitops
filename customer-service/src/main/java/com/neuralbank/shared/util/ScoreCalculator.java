package com.neuralbank.shared.util;

import com.neuralbank.domain.entity.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@ApplicationScoped
public class ScoreCalculator {
    
    public BigDecimal calculate(Customer customer) {
        // Algoritmo simplificado de scoring crediticio
        // En producción esto sería mucho más complejo con ML
        
        BigDecimal score = BigDecimal.valueOf(500); // Score base
        
        // Factor 1: Antigüedad del cliente (hasta +100 puntos)
        if (customer.fechaRegistro != null) {
            Period period = Period.between(customer.fechaRegistro, LocalDate.now());
            int meses = period.getYears() * 12 + period.getMonths();
            score = score.add(BigDecimal.valueOf(Math.min(meses * 2, 100)));
        }
        
        // Factor 2: Información completa (+50 puntos)
        int completeness = 0;
        if (customer.email != null && !customer.email.isEmpty()) completeness += 10;
        if (customer.telefono != null && !customer.telefono.isEmpty()) completeness += 10;
        if (customer.direccion != null && !customer.direccion.isEmpty()) completeness += 10;
        if (customer.fechaNacimiento != null) completeness += 10;
        if (customer.ejecutivoId != null) completeness += 10;
        score = score.add(BigDecimal.valueOf(completeness));
        
        // Factor 3: Tipo de cliente
        switch (customer.tipoCliente) {
            case CORPORATIVO:
                score = score.add(BigDecimal.valueOf(100));
                break;
            case EMPRESARIAL:
                score = score.add(BigDecimal.valueOf(50));
                break;
            case PERSONAL:
                score = score.add(BigDecimal.valueOf(25));
                break;
        }
        
        // Factor 4: Edad (clientes entre 30-50 años tienen mejor score)
        if (customer.fechaNacimiento != null) {
            int edad = Period.between(customer.fechaNacimiento, LocalDate.now()).getYears();
            if (edad >= 30 && edad <= 50) {
                score = score.add(BigDecimal.valueOf(50));
            } else if (edad >= 25 && edad < 30) {
                score = score.add(BigDecimal.valueOf(25));
            } else if (edad > 50 && edad <= 65) {
                score = score.add(BigDecimal.valueOf(25));
            }
        }
        
        // Asegurar que el score esté entre 300 y 850
        if (score.compareTo(BigDecimal.valueOf(300)) < 0) {
            score = BigDecimal.valueOf(300);
        }
        if (score.compareTo(BigDecimal.valueOf(850)) > 0) {
            score = BigDecimal.valueOf(850);
        }
        
        return score;
    }
    
    public String determineRiskLevel(BigDecimal score) {
        if (score == null) {
            return "No Evaluado";
        }
        
        if (score.compareTo(BigDecimal.valueOf(750)) >= 0) {
            return "Bajo";
        } else if (score.compareTo(BigDecimal.valueOf(650)) >= 0) {
            return "Medio";
        } else if (score.compareTo(BigDecimal.valueOf(550)) >= 0) {
            return "Alto";
        } else {
            return "Muy Alto";
        }
    }
}