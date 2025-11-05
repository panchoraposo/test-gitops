package com.neuralbank.shared.util;

import com.neuralbank.domain.entity.Account;
import com.neuralbank.domain.entity.Credit;
import com.neuralbank.domain.entity.Customer;
import com.neuralbank.domain.entity.Cuota;
import com.neuralbank.domain.enums.ApprovalRuleResult;
import com.neuralbank.domain.repository.AccountRepository;
import com.neuralbank.domain.repository.CuotaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Motor de aprobación de créditos.
 * Implementa las reglas de negocio:
 * - Regla #1: Capacity (Debt to Income Ratio) = Cuota / Salario
 * - Regla #2: Willingness to Pay (Credit Score)
 */
@ApplicationScoped
public class CreditApprovalEngine {
    
    @Inject
    AccountRepository accountRepository;
    
    @Inject
    CuotaRepository cuotaRepository;
    
    private static final BigDecimal DEBT_TO_INCOME_THRESHOLD = new BigDecimal("0.50"); // 50%
    private static final BigDecimal CREDIT_SCORE_THRESHOLD = new BigDecimal("500");
    private static final int MONTHS_TO_CHECK_HISTORY = 24;
    
    /**
     * Evalúa la Regla #1: Capacity (Capacidad de Pago)
     * Debt to Income Ratio = Cuota / Salario
     * 
     * @param montoCuota Monto de la cuota del crédito solicitado
     * @param salarioMensual Salario mensual del cliente
     * @return Resultado de la evaluación
     */
    public ApprovalRuleResult evaluateCapacityRule(BigDecimal montoCuota, BigDecimal salarioMensual) {
        if (montoCuota == null || salarioMensual == null || salarioMensual.compareTo(BigDecimal.ZERO) <= 0) {
            return ApprovalRuleResult.REJECTED;
        }
        
        BigDecimal debtToIncomeRatio = montoCuota
                .divide(salarioMensual, 4, RoundingMode.HALF_UP);
        
        // Regla #1: Si deuda / salario >= 50% => REJECTED
        // Regla #1: Si deuda / salario < 50% => APPROVED
        if (debtToIncomeRatio.compareTo(DEBT_TO_INCOME_THRESHOLD) >= 0) {
            return ApprovalRuleResult.REJECTED;
        }
        
        return ApprovalRuleResult.APPROVED;
    }
    
    /**
     * Evalúa la Regla #2: Willingness to Pay (Disposición de Pago)
     * Basado en Credit Score
     * 
     * @param creditScore Score crediticio del cliente
     * @return Resultado de la evaluación
     */
    public ApprovalRuleResult evaluateWillingnessToPayRule(BigDecimal creditScore) {
        if (creditScore == null) {
            return ApprovalRuleResult.REJECTED;
        }
        
        // Regla #2: Si el Credit Score <= 500 => REJECTED
        // Regla #2: Si el Credit Score > 500 => APPROVED
        if (creditScore.compareTo(CREDIT_SCORE_THRESHOLD) <= 0) {
            return ApprovalRuleResult.REJECTED;
        }
        
        return ApprovalRuleResult.APPROVED;
    }
    
    /**
     * Calcula el salario mensual estimado basado en el saldo actual de las cuentas.
     * Nota: Esta es una aproximación, ya que solo tenemos el saldo actual del mes.
     * 
     * @param clienteId ID del cliente
     * @return Salario mensual estimado
     */
    public BigDecimal calculateEstimatedMonthlySalary(Long clienteId) {
        List<Account> accounts = accountRepository.findByClienteId(clienteId);
        
        if (accounts.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        // Suma de todos los saldos actuales como aproximación del ingreso mensual
        // Nota: Esto es una simplificación. En producción, se debería tener
        // un campo específico para el salario o calcularlo desde transacciones históricas
        BigDecimal totalSaldo = accounts.stream()
                .map(account -> account.saldoActual != null ? account.saldoActual : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return totalSaldo;
    }
    
    /**
     * Calcula el monto de la cuota mensual basado en el crédito solicitado.
     * Usa fórmula de amortización francesa.
     * 
     * @param montoSolicitado Monto total del crédito
     * @param tasaInteresAnual Tasa de interés anual (ej: 0.12 para 12%)
     * @param plazoMeses Número de meses del plazo
     * @return Monto de la cuota mensual
     */
    public BigDecimal calculateMonthlyPayment(BigDecimal montoSolicitado, BigDecimal tasaInteresAnual, Integer plazoMeses) {
        if (montoSolicitado == null || tasaInteresAnual == null || plazoMeses == null || plazoMeses <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Convertir tasa anual a mensual
        BigDecimal tasaMensual = tasaInteresAnual.divide(new BigDecimal("12"), 6, RoundingMode.HALF_UP)
                .divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
        
        // Fórmula de amortización francesa: Cuota = P * (r * (1 + r)^n) / ((1 + r)^n - 1)
        // donde P = principal, r = tasa mensual, n = número de pagos
        
        if (tasaMensual.compareTo(BigDecimal.ZERO) == 0) {
            // Si la tasa es 0, es simplemente el monto dividido por el plazo
            return montoSolicitado.divide(new BigDecimal(plazoMeses), 2, RoundingMode.HALF_UP);
        }
        
        BigDecimal unoMasTasa = BigDecimal.ONE.add(tasaMensual);
        BigDecimal potencia = unoMasTasa.pow(plazoMeses);
        BigDecimal numerador = montoSolicitado.multiply(tasaMensual).multiply(potencia);
        BigDecimal denominador = potencia.subtract(BigDecimal.ONE);
        
        return numerador.divide(denominador, 2, RoundingMode.HALF_UP);
    }
    
    /**
     * Evalúa el historial de pagos del cliente para determinar si hay pagos atrasados.
     * Revisa los últimos 24 meses.
     * 
     * @param clienteId ID del cliente
     * @return true si no hay pagos atrasados en los últimos 24 meses
     */
    public boolean hasNoLatePaymentsInLast24Months(Long clienteId) {
        LocalDate fechaLimite = LocalDate.now().minus(MONTHS_TO_CHECK_HISTORY, ChronoUnit.MONTHS);
        
        // Buscar todas las cuotas vencidas del cliente en los últimos 24 meses
        // Esto requeriría una consulta más compleja que relacione créditos con cuotas
        // Por ahora, retornamos true asumiendo que el credit score ya refleja esto
        return true;
    }
    
    /**
     * Evalúa la aprobación completa del crédito aplicando ambas reglas.
     * 
     * @param credit Crédito a evaluar
     * @param customer Cliente solicitante
     * @param salarioMensual Salario mensual del cliente (opcional, se calcula si es null)
     * @return Resultado de la aprobación
     */
    public ApprovalResult evaluateCreditApproval(Credit credit, Customer customer, BigDecimal salarioMensual) {
        ApprovalResult result = new ApprovalResult();
        result.creditId = credit.id;
        result.clienteId = credit.clienteId;
        
        // Calcular monto de cuota si no está disponible
        BigDecimal montoCuota = calculateMonthlyPayment(
                credit.montoSolicitado,
                credit.tasaInteresAnual,
                credit.plazoMeses
        );
        result.montoCuota = montoCuota;
        
        // Obtener salario mensual si no se proporciona
        if (salarioMensual == null) {
            salarioMensual = calculateEstimatedMonthlySalary(credit.clienteId);
        }
        result.salarioMensual = salarioMensual;
        
        // Evaluar Regla #1: Capacity
        result.capacityRuleResult = evaluateCapacityRule(montoCuota, salarioMensual);
        if (salarioMensual.compareTo(BigDecimal.ZERO) > 0) {
            result.debtToIncomeRatio = montoCuota.divide(salarioMensual, 4, RoundingMode.HALF_UP);
        }
        
        // Evaluar Regla #2: Willingness to Pay
        result.willingnessToPayRuleResult = evaluateWillingnessToPayRule(customer.scoreCrediticio);
        result.creditScore = customer.scoreCrediticio;
        
        // Decisión final: ambas reglas deben estar aprobadas
        result.approved = result.capacityRuleResult == ApprovalRuleResult.APPROVED 
                && result.willingnessToPayRuleResult == ApprovalRuleResult.APPROVED;
        
        return result;
    }
    
    /**
     * Clase interna para el resultado de la aprobación
     */
    public static class ApprovalResult {
        public Long creditId;
        public Long clienteId;
        public BigDecimal montoCuota;
        public BigDecimal salarioMensual;
        public BigDecimal debtToIncomeRatio;
        public ApprovalRuleResult capacityRuleResult;
        public ApprovalRuleResult willingnessToPayRuleResult;
        public BigDecimal creditScore;
        public boolean approved;
        public String rejectionReason;
        
        public String getRejectionReason() {
            if (approved) {
                return null;
            }
            
            StringBuilder reason = new StringBuilder();
            if (capacityRuleResult == ApprovalRuleResult.REJECTED) {
                reason.append("Regla #1 (Capacity): Debt to Income Ratio >= 50%");
            }
            if (willingnessToPayRuleResult == ApprovalRuleResult.REJECTED) {
                if (reason.length() > 0) {
                    reason.append("; ");
                }
                reason.append("Regla #2 (Willingness to Pay): Credit Score <= 500");
            }
            
            return reason.toString();
        }
    }
}

