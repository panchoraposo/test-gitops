package com.neuralbank.tools;

import com.neuralbank.client.CreditApprovalClient;
import com.neuralbank.dto.request.CreateCreditRequest;
import com.neuralbank.dto.request.CreditApprovalRequest;
import com.neuralbank.dto.response.CreditApprovalResponse;
import com.neuralbank.dto.response.CreditResponse;
import com.neuralbank.enums.CreditType;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CreditApprovalTools {

    @RestClient
    CreditApprovalClient creditApprovalClient;

    // ============================================
    // CREDIT APPROVAL OPERATIONS
    // ============================================

    @Tool(description = "Evaluate credit approval for a credit request. Applies two rules: Capacity (Debt to Income Ratio) and Willingness to Pay (Credit Score). Returns detailed evaluation results including approval status and reasons.")
    public String evaluateCreditApproval(
            @ToolArg(description = "Credit ID to evaluate. Can be passed as a number (e.g., 1) or string (e.g., \"1\")", required = false) String creditId,
            @ToolArg(description = "Customer ID. Can be passed as a number (e.g., 2) or string (e.g., \"2\")", required = false) String clienteId,
            @ToolArg(description = "Monthly salary of the customer. Optional - if not provided, it will be calculated from account balances. Can be passed as a number (e.g., 1500.50) or string (e.g., \"1500.50\")", required = false) String salarioMensual
    ) {
        try {
            CreditApprovalRequest request = new CreditApprovalRequest();
            
            if (creditId != null) {
                request.creditId = parseLong(creditId, "creditId");
            }
            
            if (clienteId != null) {
                request.clienteId = parseLong(clienteId, "clienteId");
            }
            
            if (salarioMensual != null) {
                Double salario = parseDouble(salarioMensual, "salarioMensual");
                request.salarioMensual = BigDecimal.valueOf(salario);
            }
            
            CreditApprovalResponse response = creditApprovalClient.evaluateApproval(request);
            return formatCreditApprovalResponse(response);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Approve a credit request. This will automatically evaluate the credit and approve it if it meets all approval rules (Capacity and Willingness to Pay).")
    public String approveCredit(
            @ToolArg(description = "Credit ID to approve. Can be passed as a number (e.g., 1) or string (e.g., \"1\")") String creditId
    ) {
        try {
            Long creditIdLong = parseLong(creditId, "creditId");
            CreditApprovalResponse response = creditApprovalClient.approveCredit(creditIdLong);
            return formatCreditApprovalResponse(response);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Reject a credit request. Requires a reason for rejection.")
    public String rejectCredit(
            @ToolArg(description = "Credit ID to reject. Can be passed as a number (e.g., 1) or string (e.g., \"1\")") String creditId,
            @ToolArg(description = "Reason for rejection") String reason
    ) {
        try {
            Long creditIdLong = parseLong(creditId, "creditId");
            Map<String, String> request = new HashMap<>();
            request.put("reason", reason);
            CreditApprovalResponse response = creditApprovalClient.rejectCredit(creditIdLong, request);
            return formatCreditApprovalResponse(response);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Recalculate credit approval for an existing credit. Useful when customer data or credit conditions have changed.")
    public String recalculateApproval(
            @ToolArg(description = "Credit ID to recalculate. Can be passed as a number (e.g., 1) or string (e.g., \"1\")") String creditId
    ) {
        try {
            Long creditIdLong = parseLong(creditId, "creditId");
            CreditApprovalResponse response = creditApprovalClient.recalculateApproval(creditIdLong);
            return formatCreditApprovalResponse(response);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    // ============================================
    // CREDIT CRUD OPERATIONS
    // ============================================

    @Tool(description = "Create a new credit request. This creates a credit application that can then be evaluated for approval.")
    public String createCreditRequest(
            @ToolArg(description = "Customer ID requesting the credit. Can be passed as a number (e.g., 2) or string (e.g., \"2\")") String clienteId,
            @ToolArg(description = "Credit type: Consumo, Hipotecario, Comercial, Automotriz, Microempresa, or Personal") String tipoCredito,
            @ToolArg(description = "Currency ID (1 for USD, 2 for EUR, etc). Can be passed as a number or string") String monedaId,
            @ToolArg(description = "Requested credit amount. Can be passed as a number (e.g., 50000.00) or string (e.g., \"50000.00\")") String montoSolicitado,
            @ToolArg(description = "Annual interest rate (e.g., 12.5 for 12.5%). Can be passed as a number or string") String tasaInteresAnual,
            @ToolArg(description = "Loan term in months. Can be passed as a number (e.g., 36) or string (e.g., \"36\")") String plazoMeses,
            @ToolArg(description = "Account ID for disbursement. Optional. Can be passed as a number or string", required = false) String cuentaDesembolsoId,
            @ToolArg(description = "Credit number. Optional - will be auto-generated if not provided", required = false) String numeroCredito,
            @ToolArg(description = "Purpose of the credit", required = false) String destinoCredito,
            @ToolArg(description = "Executive ID handling the credit. Optional. Can be passed as a number or string", required = false) String ejecutivoId,
            @ToolArg(description = "Request date (YYYY-MM-DD). Optional - defaults to today if not provided", required = false) String fechaSolicitud
    ) {
        try {
            CreateCreditRequest request = new CreateCreditRequest();
            request.clienteId = parseLong(clienteId, "clienteId");
            request.tipoCredito = CreditType.valueOf(tipoCredito);
            request.monedaId = parseLong(monedaId, "monedaId");
            
            Double monto = parseDouble(montoSolicitado, "montoSolicitado");
            request.montoSolicitado = BigDecimal.valueOf(monto);
            
            Double tasa = parseDouble(tasaInteresAnual, "tasaInteresAnual");
            request.tasaInteresAnual = BigDecimal.valueOf(tasa);
            
            request.plazoMeses = parseInt(plazoMeses, "plazoMeses");
            
            if (cuentaDesembolsoId != null) {
                request.cuentaDesembolsoId = parseLong(cuentaDesembolsoId, "cuentaDesembolsoId");
            }
            
            request.numeroCredito = numeroCredito;
            request.destinoCredito = destinoCredito;
            
            if (ejecutivoId != null) {
                request.ejecutivoId = parseLong(ejecutivoId, "ejecutivoId");
            }
            
            if (fechaSolicitud != null) {
                request.fechaSolicitud = LocalDate.parse(fechaSolicitud);
            } else {
                request.fechaSolicitud = LocalDate.now();
            }
            
            CreditResponse response = creditApprovalClient.createCreditRequest(request);
            return formatCreditResponse(response);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Get complete credit information by credit ID.")
    public String getCredit(
            @ToolArg(description = "Credit ID. Can be passed as a number (e.g., 1) or string (e.g., \"1\")") String creditId
    ) {
        try {
            Long creditIdLong = parseLong(creditId, "creditId");
            CreditResponse response = creditApprovalClient.getCreditById(creditIdLong);
            return formatCreditResponse(response);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Get all credits for a specific customer.")
    public String getCreditsByCliente(
            @ToolArg(description = "Customer ID. Can be passed as a number (e.g., 2) or string (e.g., \"2\")") String clienteId
    ) {
        try {
            Long clienteIdLong = parseLong(clienteId, "clienteId");
            List<CreditResponse> credits = creditApprovalClient.getCreditsByCliente(clienteIdLong);
            return formatCreditList(credits);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool(description = "Get all credits by status. Status can be: Solicitado, EnRevision, Aprobado, Rechazado, Desembolsado, Vigente, Cancelado, Vencido, or Refinanciado")
    public String getCreditsByStatus(
            @ToolArg(description = "Credit status: Solicitado, EnRevision, Aprobado, Rechazado, Desembolsado, Vigente, Cancelado, Vencido, or Refinanciado") String status
    ) {
        try {
            List<CreditResponse> credits = creditApprovalClient.getCreditsByStatus(status);
            return formatCreditList(credits);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    // ============================================
    // HELPER PARSING METHODS
    // ============================================

    private Long parseLong(String value, String paramName) {
        if (value == null) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + paramName + ": must be a valid number. Provided value: " + value);
        }
    }

    private Integer parseInt(String value, String paramName) {
        if (value == null) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + paramName + ": must be a valid number. Provided value: " + value);
        }
    }

    private Double parseDouble(String value, String paramName) {
        if (value == null) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + paramName + ": must be a valid number. Provided value: " + value);
        }
    }

    // ============================================
    // HELPER FORMATTING METHODS
    // ============================================

    private String formatCreditApprovalResponse(CreditApprovalResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append("Credit Approval Evaluation:\n");
        sb.append("===========================\n");
        sb.append("Credit ID: ").append(response.creditId).append("\n");
        sb.append("Customer ID: ").append(response.clienteId).append("\n");
        sb.append("Approved: ").append(response.approved ? "YES" : "NO").append("\n");
        
        if (!response.approved && response.rejectionReason != null) {
            sb.append("Rejection Reason: ").append(response.rejectionReason).append("\n");
        }
        
        sb.append("\nEvaluation Details:\n");
        sb.append("-------------------\n");
        
        if (response.montoCuota != null) {
            sb.append("Monthly Payment: ").append(response.montoCuota).append("\n");
        }
        
        if (response.salarioMensual != null) {
            sb.append("Monthly Salary: ").append(response.salarioMensual).append("\n");
        }
        
        if (response.debtToIncomeRatio != null) {
            sb.append("Debt to Income Ratio: ").append(String.format("%.2f%%", response.debtToIncomeRatio.multiply(BigDecimal.valueOf(100)).doubleValue())).append("\n");
        }
        
        sb.append("\nRule #1 - Capacity (Debt to Income):\n");
        sb.append("  Result: ").append(response.capacityRuleResult).append("\n");
        if (response.capacityRuleMessage != null) {
            sb.append("  ").append(response.capacityRuleMessage).append("\n");
        }
        
        sb.append("\nRule #2 - Willingness to Pay (Credit Score):\n");
        if (response.creditScore != null) {
            sb.append("  Credit Score: ").append(response.creditScore).append("\n");
        }
        sb.append("  Result: ").append(response.willingnessToPayRuleResult).append("\n");
        if (response.willingnessToPayRuleMessage != null) {
            sb.append("  ").append(response.willingnessToPayRuleMessage).append("\n");
        }
        
        return sb.toString();
    }

    private String formatCreditResponse(CreditResponse credit) {
        StringBuilder sb = new StringBuilder();
        sb.append("Credit Information:\n");
        sb.append("==================\n");
        sb.append("ID: ").append(credit.id).append("\n");
        sb.append("Credit Number: ").append(credit.numeroCredito).append("\n");
        sb.append("Customer ID: ").append(credit.clienteId).append("\n");
        sb.append("Type: ").append(credit.tipoCredito).append("\n");
        sb.append("Status: ").append(credit.estado).append("\n");
        
        if (credit.montoSolicitado != null) {
            sb.append("Requested Amount: ").append(credit.montoSolicitado).append("\n");
        }
        
        if (credit.montoAprobado != null) {
            sb.append("Approved Amount: ").append(credit.montoAprobado).append("\n");
        }
        
        if (credit.tasaInteresAnual != null) {
            sb.append("Annual Interest Rate: ").append(credit.tasaInteresAnual).append("%\n");
        }
        
        if (credit.plazoMeses != null) {
            sb.append("Term (months): ").append(credit.plazoMeses).append("\n");
        }
        
        if (credit.fechaSolicitud != null) {
            sb.append("Request Date: ").append(credit.fechaSolicitud).append("\n");
        }
        
        if (credit.fechaAprobacion != null) {
            sb.append("Approval Date: ").append(credit.fechaAprobacion).append("\n");
        }
        
        if (credit.destinoCredito != null) {
            sb.append("Purpose: ").append(credit.destinoCredito).append("\n");
        }
        
        if (credit.scoreAprobacion != null) {
            sb.append("Approval Score: ").append(credit.scoreAprobacion).append("\n");
        }
        
        return sb.toString();
    }

    private String formatCreditList(List<CreditResponse> credits) {
        StringBuilder sb = new StringBuilder();
        sb.append("Credit List:\n");
        sb.append("============\n");
        sb.append("Total Credits: ").append(credits.size()).append("\n\n");
        
        if (credits.isEmpty()) {
            sb.append("No credits found.\n");
            return sb.toString();
        }
        
        for (int i = 0; i < credits.size(); i++) {
            CreditResponse credit = credits.get(i);
            sb.append((i + 1)).append(". Credit ID: ").append(credit.id);
            sb.append(" - Number: ").append(credit.numeroCredito);
            sb.append(" - Customer: ").append(credit.clienteId);
            sb.append(" - Type: ").append(credit.tipoCredito);
            sb.append(" - Status: ").append(credit.estado);
            if (credit.montoSolicitado != null) {
                sb.append(" - Amount: ").append(credit.montoSolicitado);
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}

