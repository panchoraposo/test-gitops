package com.neuralbank.infrastructure.rest.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class CustomerSummaryResponse {
    
    public CustomerResponse customer;
    public int totalCuentas;
    public int totalCreditos;
    public BigDecimal saldoTotal;
    public CreditScoreInfo creditScore;
    public List<RecentActivity> recentActivity;
    
    public static class CreditScoreInfo {
        public BigDecimal score;
        public String nivelRiesgo;
        public String evaluacion;
    }
    
    public static class RecentActivity {
        public String tipo;
        public String descripcion;
        public String fecha;
        public BigDecimal monto;
    }
}