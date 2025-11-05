package com.neuralbank.infrastructure.rest.dto.request;

import com.neuralbank.domain.enums.CustomerType;
import jakarta.ws.rs.QueryParam;

import java.math.BigDecimal;

public class CustomerSearchRequest {
    
    @QueryParam("tipoCliente")
    public CustomerType tipoCliente;
    
    @QueryParam("paisId")
    public Long paisId;
    
    @QueryParam("ciudad")
    public String ciudad;
    
    @QueryParam("scoreMin")
    public BigDecimal scoreMin;
    
    @QueryParam("scoreMax")
    public BigDecimal scoreMax;
    
    @QueryParam("nivelRiesgo")
    public String nivelRiesgo;
    
    @QueryParam("sucursalId")
    public Long sucursalId;
    
    @QueryParam("ejecutivoId")
    public Long ejecutivoId;
    
    @QueryParam("activo")
    public Boolean activo;
    
    @QueryParam("search")
    public String search;
}