package com.neuralbank.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entidad mínima de Account para acceso desde el servicio de aprobación.
 * Solo contiene los campos necesarios para calcular la capacidad de pago.
 */
@Entity
@Table(name = "cuenta", schema = "core")
public class Account extends PanacheEntityBase {
    
    @Id
    @Column(name = "cuenta_id")
    public Long id;
    
    @Column(name = "cliente_id", nullable = false)
    public Long clienteId;
    
    @Column(name = "saldo_actual", precision = 18, scale = 2)
    public BigDecimal saldoActual;
}

