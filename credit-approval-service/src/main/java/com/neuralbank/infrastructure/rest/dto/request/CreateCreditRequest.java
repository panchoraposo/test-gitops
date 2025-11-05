package com.neuralbank.infrastructure.rest.dto.request;

import com.neuralbank.domain.enums.CreditType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateCreditRequest {
    
    public Long clienteId;
    public Long cuentaDesembolsoId;
    public String numeroCredito;
    public CreditType tipoCredito;
    public Long monedaId;
    public BigDecimal montoSolicitado;
    public BigDecimal tasaInteresAnual;
    public Integer plazoMeses;
    public LocalDate fechaSolicitud;
    public String destinoCredito;
    public Long ejecutivoId;
}

