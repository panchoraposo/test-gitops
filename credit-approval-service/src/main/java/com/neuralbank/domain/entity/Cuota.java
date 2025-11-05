package com.neuralbank.domain.entity;

import com.neuralbank.domain.enums.CuotaStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "cuota", schema = "core")
public class Cuota extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuota_id")
    public Long id;
    
    @Column(name = "credito_id", nullable = false)
    public Long creditoId;
    
    @Column(name = "numero_cuota", nullable = false)
    public Integer numeroCuota;
    
    @Column(name = "fecha_vencimiento", nullable = false)
    public LocalDate fechaVencimiento;
    
    @Column(name = "monto_cuota", nullable = false, precision = 18, scale = 2)
    public BigDecimal montoCuota;
    
    @Column(name = "monto_capital", nullable = false, precision = 18, scale = 2)
    public BigDecimal montoCapital;
    
    @Column(name = "monto_interes", nullable = false, precision = 18, scale = 2)
    public BigDecimal montoInteres;
    
    @Column(name = "monto_otros_cargos", precision = 18, scale = 2)
    public BigDecimal montoOtrosCargos = BigDecimal.ZERO;
    
    @Column(name = "saldo_capital", nullable = false, precision = 18, scale = 2)
    public BigDecimal saldoCapital;
    
    @Column(name = "fecha_pago")
    public LocalDate fechaPago;
    
    @Column(name = "monto_pagado", precision = 18, scale = 2)
    public BigDecimal montoPagado = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    public CuotaStatus estado = CuotaStatus.Pendiente;
    
    @Column(name = "dias_mora")
    public Integer diasMora = 0;
    
    @Column(name = "monto_mora", precision = 18, scale = 2)
    public BigDecimal montoMora = BigDecimal.ZERO;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    public Map<String, Object> metadata = new HashMap<>();
    
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    public LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

