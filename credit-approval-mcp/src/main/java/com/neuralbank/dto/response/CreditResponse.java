package com.neuralbank.dto.response;

import com.neuralbank.enums.CreditStatus;
import com.neuralbank.enums.CreditType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class CreditResponse {
    
    public Long id;
    public Long clienteId;
    public Long cuentaDesembolsoId;
    public String numeroCredito;
    public CreditType tipoCredito;
    public Long monedaId;
    public BigDecimal montoSolicitado;
    public BigDecimal montoAprobado;
    public BigDecimal tasaInteresAnual;
    public Integer plazoMeses;
    public LocalDate fechaSolicitud;
    public LocalDate fechaAprobacion;
    public LocalDate fechaDesembolso;
    public LocalDate fechaPrimerVencimiento;
    public CreditStatus estado;
    public String destinoCredito;
    public BigDecimal scoreAprobacion;
    public Long ejecutivoId;
    public Map<String, Object> metadata;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}

